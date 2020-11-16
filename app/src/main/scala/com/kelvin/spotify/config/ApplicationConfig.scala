package com.kelvin.spotify.config

case class ApplicationConfig(spotifyApiConfig: SpotifyApiConfig)

case class SpotifyApiConfig(
    url: String,
    sourcePlaylist: String,
    destinationPlaylist: String,
    authConfig: AuthConfig,
    rateLimit: RateLimitConfig
)
