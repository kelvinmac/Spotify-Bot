package com.kelvin.spotify

import com.github.nscala_time.time.Imports.richReadableInstant
import com.kelvin.spotify.models.AccessTokenResponse
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Filter, Service, SimpleFilter}
import com.twitter.util.Future
import org.joda.time.DateTime
import cats.syntax.all._

class SpotifyTokenFilter(authApi: AuthApi) extends SimpleFilter[Request, Response] {
  private var expiresAt: DateTime = DateTime.now()
  private var token: String       = ""

  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    if (DateTime.now() < expiresAt) {
      return handleRequest(request, service)
    }

    updateToken
      .flatMap { tokenResponse =>
        token = tokenResponse.access_token
        expiresAt = DateTime.now().plusSeconds(tokenResponse.expires_in)

        handleRequest(request, service)
      }
  }

  private def handleRequest(request: Request, service: Service[Request, Response]): Future[Response] = {
    request.headerMap.add("Authorization", s"${token}")
    service(request)
  }

  def updateToken: Future[AccessTokenResponse] = {
    authApi.getToken.value.flatMap {
      case Right(token) => Future(token)
      case Left(err)    => Future.exception[AccessTokenResponse](new Exception(err))
    }
  }
}
