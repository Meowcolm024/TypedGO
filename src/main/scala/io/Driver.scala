package io

import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream
import scala.concurrent.duration._

trait Driver[F[_]: Sync]:
  def tap(x: Int, y: Int): F[Unit]
  def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit]

object Driver:

  def AdbDriver[F[_]: Sync]: Driver[F] = new Driver[F] {
    import scala.sys.process._
    override def tap(x: Int, y: Int): F[Unit] =
      Sync[F]
        .handleErrorWith(Sync[F].blocking(s"adb shell input tap $x $y".!!<))(
          Sync[F].raiseError(_)
        )
        .map(s => Sync[F].blocking(println(s)))
    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit] =
      Sync[F]
        .handleErrorWith(
          Sync[F].blocking(
            s"adb shell input swipe ${x._1} ${x._2} ${y._1} ${y._2} ${t.toMicros}".!!<
          )
        )(Sync[F].raiseError(_))
        .map(s => Sync[F].blocking(println(s)))
  }

  val TestDriver: Driver[IO] = new Driver[IO] {
    override def tap(x: Int, y: Int): IO[Unit] =
      IO.println(s"tap ($x, $y)")
    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): IO[Unit] =
      IO.println(s"swipe from $x to $y within $t")
  }

end Driver
