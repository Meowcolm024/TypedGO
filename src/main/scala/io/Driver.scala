package io

import org.typelevel.log4cats.Logger
import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream
import scala.concurrent.duration._

trait Driver[F[_]: Sync](using Logger[F]):
  def tap(x: Int, y: Int): F[Unit]
  def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit]

  final def waitUntil(p: F[Boolean]): F[Unit] =
    for c <- p
    yield if c then Sync[F].unit else sleep(100.millis)

  final def sleep(time: FiniteDuration): F[Unit] =
    Sync[F].delay(Thread.sleep(time.toMillis))

object Driver:

  def AdbDriver[F[_]: Sync](using Logger[F]): Driver[F] = new Driver[F] {
    import scala.sys.process._
    import cats.effect.std.Random

    private def adbWait: F[Unit] =
      for
        rnd <- Random.scalaUtilRandom[F]
        time <- rnd.betweenInt(400, 600)
        _ <- sleep(time.millis)
      yield ()

    override def tap(x: Int, y: Int): F[Unit] =
      Sync[F]
        .handleErrorWith(Sync[F].blocking(s"adb shell input tap $x $y".!!<))(
          Sync[F].raiseError(_)
        )
        .map(s => Logger[F].info(s))
        >> adbWait

    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit] =
      Sync[F]
        .handleErrorWith(
          Sync[F].blocking(
            s"adb shell input swipe ${x._1} ${x._2} ${y._1} ${y._2} ${t.toMicros}".!!<
          )
        )(Sync[F].raiseError(_))
        .map(s => Logger[F].info(s))
        >> adbWait
  }

  def TestDriver[F[_]: Sync](using Logger[F]): Driver[F] = new Driver[F] {
    override def tap(x: Int, y: Int): F[Unit] =
      Logger[F].info(s"tap ($x, $y)") >> sleep(500.millis)

    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit] =
      Logger[F].info(s"swipe from $x to $y within $t") >> sleep(500.millis)
  }

end Driver
