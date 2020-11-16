package com.kelvin.spotify
import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.data.EitherT
import com.kelvin.spotify.config.AuthApiConfig
import com.kelvin.spotify.models.AccessTokenResponse
import com.twitter.finagle.http._
import com.twitter.finagle.http.service.HttpResponseClassifier
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, RequestBuilder, Response}
import com.twitter.io.Buf
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, jawn}
import cats.syntax.all._
import com.twitter.util.Future

class SpotifyAuthApi(authConfig: AuthApiConfig, api: Service[Request, Response]) extends AuthApi {
  import SpotifyAuthApi._

  private val authenticationRequest =
    RequestBuilder()
      .url(s"${authConfig.authEndpoint}")
      .addHeader("Authorization", s"Basic ${encodeBase64(s"${authConfig.clientId}:${authConfig.clientSecret}")}")
      .addHeader("Content-Type", "application/x-www-form-urlencoded")
      .buildPost(Buf.Utf8("grant_type: client_credentials"))

  private def encodeBase64(str: String): String =
    Base64.getEncoder.encodeToString(str.getBytes(StandardCharsets.UTF_8))

  override def getToken: EitherT[Future, String, AccessTokenResponse] = {
    EitherT(
      api(authenticationRequest).map {
        case response if response.status == Status.Ok =>
          jawn.decode[AccessTokenResponse](response.contentString).leftMap { err =>
            s"Failed to decode response from authentication server with error [error=${err.getMessage}]"
          }
        case response: Response =>
          Left(
            s"Requesting new token failed with [http-status=${response.status}] and [response-body=${response.contentString}]"
          )
      }
    )
  }
}

object SpotifyAuthApi {
  implicit val accessTokenResponseDecoder: Decoder[AccessTokenResponse] = deriveDecoder[AccessTokenResponse]

  def apply(authConfig: AuthApiConfig): SpotifyAuthApi = {
    val service = Http.client
      .withLabel("Spotify-Auth-Api")
      .withDecompression(true)
      .withResponseClassifier(HttpResponseClassifier.ServerErrorsAsFailures)
      .withSessionPool
      .maxSize(10)
      .newService(authConfig.authEndpoint.toString)

    new SpotifyAuthApi(authConfig, service)
  }
}
