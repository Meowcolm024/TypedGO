import internal.{_, given}
import internal.cards.Card._
import io.Operator

import cats.effect._

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val cards = card.select[ATK, 1].select[ATK, 3].select[NP, 3]
    val turns = turn[IO].selectSkills().selectCards(cards)
    for
      _ <- IO.println("example turn in FGO")
      _ <- Operator.TestOperator.start(turns)
    yield ()
