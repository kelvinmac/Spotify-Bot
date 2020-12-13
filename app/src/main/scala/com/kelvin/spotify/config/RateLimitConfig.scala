package com.kelvin.spotify.config
import scala.concurrent.duration.FiniteDuration

case class RateLimitConfig(rate: Int, duration: FiniteDuration)
