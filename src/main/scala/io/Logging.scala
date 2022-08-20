package io

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.Sync

object Logging:

  implicit def default[F[_]: Sync]: Logger[F] =
    Slf4jLogger.getLoggerFromName[F]("TypedGO")

  implicit def unused[F[_]: Sync]: Logger[F] =
    new Logger[F] {
      def debug(message: => String): F[Unit] = Sync[F].unit
      def error(message: => String): F[Unit] = Sync[F].unit
      def info(message: => String): F[Unit] = Sync[F].unit
      def trace(message: => String): F[Unit] = Sync[F].unit
      def warn(message: => String): F[Unit] = Sync[F].unit
      def debug(t: Throwable)(message: => String): F[Unit] = Sync[F].unit
      def error(t: Throwable)(message: => String): F[Unit] = Sync[F].unit
      def info(t: Throwable)(message: => String): F[Unit] = Sync[F].unit
      def trace(t: Throwable)(message: => String): F[Unit] = Sync[F].unit
      def warn(t: Throwable)(message: => String): F[Unit] = Sync[F].unit
    }

end Logging
