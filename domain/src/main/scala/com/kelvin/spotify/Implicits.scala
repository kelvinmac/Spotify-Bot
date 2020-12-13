package com.kelvin.spotify

import com.kelvin.spotify.models.Playlist
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object Implicits {
  implicit val SpotifyPlaylistDecoder: Decoder[Playlist] = deriveDecoder[Playlist]
  implicit val SpotifyPlaylistEncoder: Encoder[Playlist] = deriveEncoder[Playlist]

}
