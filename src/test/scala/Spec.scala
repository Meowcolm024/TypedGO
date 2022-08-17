import internal.{given, _}
import internal.cards.Card._
import internal.util.Helper._
import io.Operation
import io.Operation.CardOp

import cats.effect.IO
import cats.implicits._
import fs2.Stream

class Spec extends munit.CatsEffectSuite {

  def compile[N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
      cards: Pool[K1 |>: K2 |>: K3 |>: TNil]
  )(using ValueOf[N1], ValueOf[N2], ValueOf[N3]): List[Operation] =
    cards.i match
      case k1 |>: k2 |>: k3 |>: _ => List(k1.asOp, k2.asOp, k3.asOp)

  test("card selection (simple)") {
    val selected = compile(card.select[ATK, 2].select[NP, 2].select[NP, 1])
    val expected = List(CardOp(ATK(), 2), CardOp(NP(), 2), CardOp(NP(), 1))
    IO(assertEquals(selected, expected))
  }

  test("card selection (turn)") {
    import unsafe.turns.Turn._
    val selected = turn[IO]
      .selectSkills()
      .selectCards(card.select[ATK, 1].select[ATK, 2].select[ATK, 3])
    val expected = turn[IO]
      .selectSkills()
      .unsafeSelectCards(
        unsafe.cards.Card.unsafeSelectCards(ATK[1](), ATK[2](), ATK[3]())
      )
    for
      s <- selected.build.compile.toList
      e <- expected.build.compile.toList
    yield assertEquals(s, e)
  }
}
