package com.kelvin.spotify.models

import java.net.URI

final case class Playlist(
    name: String,
    id: String,
    collaborative: Boolean,
    public: Boolean,
    description: String,
    uri: URI
)
