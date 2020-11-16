package com.kelvin.spotify.config

import scala.concurrent.duration.Duration

case class SpotifyEndpointConfig(endpoint: String, timeout: Duration)
