package com.kelvin.spotify.fixture

import com.kelvin.spotify.models.Playlist

trait SpotifyPlaylistFixture {
  val PlaylistFixture: Playlist =
    Playlist(
      name = "Mixing pot",
      id = "2LH7BRO3MaUShYtGKVCk0a",
      collaborative = true,
      public = false,
      description = "by Randin and Kevo",
      uri = "https://open.spotify.com/playlist/2LH7BRO3MaUShYtGKVCk0a?si=YXOs4KNTQEC_N9YonHD_XQ"
    )
}
