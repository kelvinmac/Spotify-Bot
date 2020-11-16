package com.kelvin.spotify

import cats.effect.{ExitCode, IO}
import com.kelvin.spotify.config.ApplicationConfig

class Startup {}

object Startup {
  def startup(config: ApplicationConfig): IO[ExitCode] = {
    val
    IO(ExitCode.Success)
  }
}
