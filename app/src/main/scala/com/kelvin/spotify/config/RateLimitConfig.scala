package com.kelvin.spotify.config
import scala.concurrent.duration.Duration

case class RateLimitConfig(rate: Integer, duration: Duration)
