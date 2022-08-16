package io

import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream
import scala.concurrent.duration._

trait Driver[F[_]: Sync]:
  def tap(x: Int, y: Int): F[Unit]
  def swipe(x: (Int, Int), y: (Int, Int), t: Duration = 1.second): F[Unit]

object Driver:

  def AdbDriver[F[_]: Sync]: Driver[F] = new Driver[F] {
    import scala.sys.process._
    override def tap(x: Int, y: Int): F[Unit] =
      Sync[F].blocking(s"adb shell input tap $x $y".!).map(_ => ())
    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): F[Unit] =
      Sync[F]
        .blocking(
          s"adb shell input swipe ${x._1} ${x._2}${y._1} ${y._2} ${t.toMicros}".!
        )
        .map(_ => ())
  }

  val TestDriver: Driver[IO] = new Driver[IO] {
    override def tap(x: Int, y: Int): IO[Unit] =
      IO.println(s"taped($x, $y)")
    override def swipe(x: (Int, Int), y: (Int, Int), t: Duration): IO[Unit] =
      IO.println(s"swipe from $x to $y, within $t")
  }

end Driver
