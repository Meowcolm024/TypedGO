import internal.{_, given}
import internal.cards.Card._
import internal.util.Helper._
import io.Operation
import io.Operation.CardOp

import cats.implicits._
import fs2.Stream

class Spec extends munit.FunSuite {

  def compile[N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
      cards: Pool[K1 |>: K2 |>: K3 |>: TNil]
  )(using ValueOf[N1], ValueOf[N2], ValueOf[N3]): List[Operation] =
    cards.i match
      case k1 |>: k2 |>: k3 |>: _ => List(k1.asOp, k2.asOp, k3.asOp)

  test("card selection 1") {
    val selected = compile(card.select[ATK, 2].select[NP, 2].select[NP, 1])
    val expected = List(CardOp(ATK(), 2), CardOp(NP(), 2), CardOp(NP(), 1))
    assertEquals(selected, expected)
  }

  test("card selection 2") {
    val selected = compile(card.select[ATK, 2].select[ATK, 3].select[ATK, 1])
    val expected = List(CardOp(ATK(), 2), CardOp(ATK(), 3), CardOp(ATK(), 1))
    assertEquals(selected, expected)
  }
}
