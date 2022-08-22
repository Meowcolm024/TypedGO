import internal.{_, given}
import io.Logging.default

import cats.effect._

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    val cards = card.select[ATK, 1].select[ATK, 3].select[NP, 3]
    val skills =
      skill.select[Servant[1], 1, Normal].select[Servant[2], 1, Target[1]]
    val turns = turn[IO].selectSkills(skills).selectCards(cards)
    for
      _ <- IO.println("example turn in FGO")
      _ <- io.Operator.AdbOperator[IO](io.Driver.TestDriver, 5).start(turns)
      _ <- io.Operator.TestOperator[IO].start(turns)
    yield ()
