package io

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.Sync

object Logging:

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

end Logging
