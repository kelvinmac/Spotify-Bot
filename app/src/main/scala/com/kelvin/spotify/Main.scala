package com.kelvin.spotify

import cats.effect._
import config.ApplicationConfig
import com.typesafe.scalalogging.LazyLogging
import pureconfig.ConfigSource

object Main extends IOApp with LazyLogging {
  def run(args: List[String]): IO[ExitCode] = {
    for {
      config <- loadConfig
      result <- Startup.startup(config)
    } yield result
  }

  def loadConfig: IO[ApplicationConfig] = {
    IO {
      ConfigSource.default.loadOrThrow[ApplicationConfig]
    }
  }
}
