import internal._
import internal.cards.Pool._
import internal.cards.Card._
import io.Operator

import cats.effect._

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val c = pool.add[ATK, 1].add[ATK, 3].add[NP, 1]
    for
      _ <- Operator.TestOperator.start(c.build)
    yield ()
