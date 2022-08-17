import internal.{_, given}
import internal.cards.Card._

import cats.effect._

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val cards = card.select[ATK, 1].select[ATK, 3].select[NP, 3]
    val turns = turn[IO].selectSkills().selectCards(cards)
    for
      _ <- IO.println("example turn in FGO")
      _ <- io.Operator.AdbOperator[IO](io.Driver.TestDriver, 1).start(turns)
      _ <- io.Operator.TestOperator.start(turns)
    yield ()
