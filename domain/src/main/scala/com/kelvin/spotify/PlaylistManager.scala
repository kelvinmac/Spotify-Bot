package com.kelvin.spotify

import com.kelvin.spotify.models._

trait PlaylistManager {
  def createNew(playListName: String)
  def addTrack(playlistId: String, track: PlaylistTrack)
  def updateDetails(playlistId: String, playlistDetails: PlaylistDetails)
  def updateCoverPhoto(playlistId: String, newCover: List[Byte])
}
