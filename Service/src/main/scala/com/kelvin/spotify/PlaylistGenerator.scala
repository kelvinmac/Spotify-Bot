package com.kelvin.spotify

class PlaylistGenerator(sourcePlaylistId: String, destinationPlaylistId: String, playlistCreator: PlaylistService) {}

object PlaylistGenerator {
  def apply(
      sourcePlaylistId: String,
      destinationPlaylistId: String,
      apiToken: String
  ): PlaylistGenerator = ???

}
