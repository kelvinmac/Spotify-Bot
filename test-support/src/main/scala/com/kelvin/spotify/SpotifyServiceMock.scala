package com.kelvin.spotify

import cats.effect.{ContextShift, IO, Timer}
import com.kelvin.spotify.http.HttpMockSupport
import com.kelvin.spotify.models.Playlist
import com.twitter.finagle.http.Status
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.finch.Application.Json
import io.circe.parser.decode
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

import scala.concurrent.ExecutionContext

object SpotifyServiceMock extends HttpMockSupport {
  private implicit val timer: Timer[IO]               = IO.timer(ExecutionContext.global)
  private implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  private implicit val playListDecoder: Decoder[Playlist] = deriveDecoder[Playlist]
  private implicit val playlistEncoder: Encoder[Playlist] = deriveEncoder[Playlist]

  private def createNewEndpoint(response: Output[String]): Endpoint[IO, Playlist] =
    post("v1" :: "users" :: path[String] :: "playlists") { _: String =>
      val content = decode[Playlist](response.value).right.get
      Output.payload(content, response.status)
    }

  private def addTrackEndpoint(response: Output[String]): Endpoint[IO, String] =
    get("v1" :: "playlists" :: path[String] :: "tracks")((_: String) => response)

  private def updateDetailsEndpoint(response: Output[String]): Endpoint[IO, String] =
    get("v1" :: "playlists" :: path[String])((_: String) => response)

  private def updateCoverPhotoEndpoint(response: Output[String]): Endpoint[IO, String] =
    get("v1" :: "playlists" :: path[String] :: "images")((_: String) => response)

  def withSpotifyService[T](
      response: Output[String] = Output.empty(Status.NotImplemented)
  )(testFn: Int => T): T = {
    val endpoints = createNewEndpoint(response) :+: addTrackEndpoint(response) :+: updateDetailsEndpoint(response) :+: updateDetailsEndpoint(
      response
    )
    val finagleService = endpoints.toServiceAs[Json]

    withMockHttpService(finagleService) { port =>
      testFn(port)
    }
  }
}
