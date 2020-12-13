package com.kelvin.spotify

import java.net.URI

import cats.effect.{IO, Resource}
import com.kelvin.spotify.fixture.{SpotifyPlaylistFixture, SpotifyUserConfigFixture}
import com.kelvin.spotify.models.Playlist
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Closable.close
import io.catbird.util.effect.futureToAsync
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto.exportEncoder
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.finch.{Ok, Output}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{EitherValues, OptionValues}

class SpotifyPlaylistManagerSpec
    extends AnyWordSpec
    with Matchers
    with SpotifyPlaylistFixture
    with SpotifyUserConfigFixture
    with EitherValues {
  import SpotifyPlaylistManagerSpec._

  "SpotifyPlaylistManager" should {
    "POST create new playlist" in {
      val jsonPayload = PlaylistFixture.asJson.noSpaces

      withSpotifyService(jsonPayload) { service =>
        val playlist = service.createNew(PlaylistFixture, ValidSpotifyUserFixture).unsafeRunSync()

        playlist.right.value mustEqual PlaylistFixture
      }
    }
  }
}

object SpotifyPlaylistManagerSpec {
  private def withSpotifyService[T](response: String)(testFn: SpotifyPlaylistService => T): T = {
    SpotifyServiceMock.withSpotifyService(Ok(response)) { port =>
      val endpoint = new URI(s"http://localhost:$port")
      val acquire  = IO(Http.client.newService(s"${endpoint.getHost}:${endpoint.getPort}"))
      val release  = (s: Service[Request, Response]) => futureToAsync[IO, Unit](close(s))

      val spotifyServiceResource = Resource.make(acquire)(release).map { service =>
        new SpotifyPlaylistService(endpoint.toString, service)
      }

      spotifyServiceResource
        .use { spotifyService =>
          IO(testFn(spotifyService))
        }
        .unsafeRunSync()
    }
  }

  val playlistEncoder: Encoder[Playlist] = deriveEncoder[Playlist]
  val playlistDecoder: Decoder[Playlist] = deriveDecoder[Playlist]
}
