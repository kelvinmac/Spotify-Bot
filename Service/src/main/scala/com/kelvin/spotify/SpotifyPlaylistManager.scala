package com.kelvin.spotify
import cats.effect.{IO, Resource}
import com.kelvin.spotify.config.SpotifyEndpointConfig
import com.kelvin.spotify.models.{PlaylistDetails, PlaylistTrack}
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import io.catbird.util.effect._

class SpotifyPlaylistManager(spotifyService: Service[Request, Response]) extends PlaylistManager {
  override def createNew(playListName: String): Unit = ???

  override def addTrack(playlistId: String, track: PlaylistTrack): Unit = ???

  override def updateDetails(playlistId: String, playlistDetails: PlaylistDetails): Unit = ???

  override def updateCoverPhoto(playlistId: String, newCover: List[Byte]): Unit = ???
}

object SpotifyPlaylistManager {
  def apply(
      endpointConfig: SpotifyEndpointConfig,
      spotifyTokenFilter: SpotifyTokenFilter
  ): Resource[IO, SpotifyPlaylistManager] = {
    val acquire = IO { spotifyTokenFilter andThen Http.client.newService(endpointConfig.endpoint) }
    val release = (svc: Service[Request, Response]) => futureToAsync[IO, Unit](svc.close())

    Resource.make(acquire)(release).map(service => new SpotifyPlaylistManager(service))
  }
}
