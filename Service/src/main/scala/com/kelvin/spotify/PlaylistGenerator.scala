package com.kelvin.spotify

class PlaylistGenerator(sourcePlaylistId: String, destinationPlaylistId: String, playlistCreator: PlaylistManager) {}

object PlaylistGenerator {
  def apply(
      sourcePlaylistId: String,
      destinationPlaylistId: String,
      apiToken: String
  ): PlaylistGenerator = ???

}
