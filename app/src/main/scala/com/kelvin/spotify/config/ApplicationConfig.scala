package com.kelvin.spotify.config

final case class ApplicationConfig(spotifyApiConfig: SpotifyApiConfig)

final case class SpotifyApiConfig(
    url: String,
    sourcePlaylist: String,
    destinationPlaylist: String,
    authConfig: AuthConfig,
    rateLimit: RateLimitConfig
)
