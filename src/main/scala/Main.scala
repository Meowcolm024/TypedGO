import internal.{_, given}
import internal.cards.Card._
import io.Operator

import cats.effect._

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val c = pool.select[ATK, 1].select[ATK, 3].select[NP, 3]
    for _ <- Operator.AdbOperator[IO].start(c.build)
    yield ()
