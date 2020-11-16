package com.kelvin.spotify.config

import java.net.URI

case class AuthApiConfig(authEndpoint: URI, clientSecret: String, clientId: String)
