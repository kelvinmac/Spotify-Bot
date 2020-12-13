package com.kelvin.spotify.fixture

import com.kelvin.spotify.config.SpotifyUserConfig

trait SpotifyUserConfigFixture {
  val ValidSpotifyUserFixture: SpotifyUserConfig =
    SpotifyUserConfig(username = "kevomacartney", userId = "af4b7ad6-17a0-4416-8bfa-7ed7c6873226")
}
