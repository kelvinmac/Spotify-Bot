package com.kelvin.spotify

import cats.data.EitherT
import com.kelvin.spotify.models.AccessTokenResponse
import com.twitter.util.Future

trait AuthApi {
  def getToken: EitherT[Future, String, AccessTokenResponse]
}
