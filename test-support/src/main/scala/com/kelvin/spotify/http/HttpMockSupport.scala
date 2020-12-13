package com.kelvin.spotify.http

import java.net.ServerSocket

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await

trait HttpMockSupport {
  protected def withMockHttpService[T](service: Service[Request, Response])(testFn: Int => T): T = {
    withMockHttpServiceAndPort(_ => service)(testFn)
  }
  protected def withMockHttpServiceAndPort[T](serviceF: Int => Service[Request, Response])(
      testFn: Int => T
  ): T = {
    val port    = getFreePort
    val service = serviceF(port)
    val server  = Http.server.serve(s":$port", service)

    try {
      testFn(port)
    } finally {
      Await.ready(
        for {
          _ <- server.close()
          _ <- service.close()
        } yield ()
      )
      ()
    }
  }

  protected def getFreePort: Int = {
    val socket = new ServerSocket(0)
    socket.setReuseAddress(true)

    val localPort = socket.getLocalPort
    socket.close()

    localPort
  }
}
