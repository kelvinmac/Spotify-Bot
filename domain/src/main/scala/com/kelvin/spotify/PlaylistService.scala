package com.kelvin.spotify

import cats.effect.IO
import com.kelvin.spotify.config.SpotifyUserConfig
import com.kelvin.spotify.models._

trait PlaylistService {
  def createNew(playlist: Playlist, user: SpotifyUserConfig): IO[Either[SpotifyApiError, Playlist]]
  def addTrack(playlistId: String, track: PlaylistTrack)
  def updateDetails(playlistId: String, playlistDetails: PlaylistDetails)
  def updateCoverPhoto(playlistId: String, newCover: List[Byte])
}
