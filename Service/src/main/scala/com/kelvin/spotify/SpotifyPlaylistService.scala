package com.kelvin.spotify
import cats.effect.{IO, Resource}
import Implicits._
import cats._
import cats.data._
import cats.implicits._
import com.kelvin.spotify.config.{SpotifyEndpointConfig, SpotifyUserConfig}
import com.kelvin.spotify.models.{Playlist, PlaylistDetails, PlaylistTrack, SpotifyApiError}
import com.twitter.finagle.http.Fields.ContentType
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{MediaType, Request, RequestBuilder, Response, Status}
import com.twitter.io.Buf
import io.circe._
import io.catbird.util.effect._
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps

class SpotifyPlaylistService(endpoint: String, spotifyService: Service[Request, Response]) extends PlaylistService {
  import SpotifyPlaylistService._

  override def createNew(playlist: Playlist, user: SpotifyUserConfig): IO[Either[SpotifyApiError, Playlist]] = {
    val buf = Buf.Utf8(playlist.asJson.noSpaces)
    val url = s"$endpoint/v1/users/${user.userId}/playlists"

    val request = RequestBuilder()
      .url(url)
      .addHeader(ContentType, MediaType.Json)
      .buildPost(buf)

    futureToAsync[IO, Response](spotifyService(request))
      .map(decodeCreateResponse)
  }

  override def addTrack(playlistId: String, track: PlaylistTrack): Unit = ???

  override def updateDetails(playlistId: String, playlistDetails: PlaylistDetails): Unit = ???

  override def updateCoverPhoto(playlistId: String, newCover: List[Byte]): Unit = ???
}

object SpotifyPlaylistService {
  def apply(
      endpointConfig: SpotifyEndpointConfig,
      spotifyTokenFilter: SpotifyTokenFilter
  ): Resource[IO, SpotifyPlaylistService] = {
    val acquire = IO { spotifyTokenFilter andThen Http.client.newService(endpointConfig.endpoint) }
    val release = (svc: Service[Request, Response]) => futureToAsync[IO, Unit](svc.close())

    Resource.make(acquire)(release).map(service => new SpotifyPlaylistService(endpointConfig.endpoint, service))
  }

  def decodeCreateResponse(response: Response): Either[SpotifyApiError, Playlist] = {
    response match {
      case response if response.status == Status.Ok =>
        jawn
          .decode[Playlist](response.contentString)
          .leftMap(failure =>
            SpotifyApiError(
              errorDescription = s"There was error decoding spotify payload:  ${failure.getMessage}",
              httpStatus = response.status.code
            )
          )

      case response: Response =>
        val content  = response.contentString
        val httpCode = response.status.code

        Left(SpotifyApiError(errorDescription = content, httpStatus = httpCode))
    }
  }
}
