import internal.{given, _}
import internal.util.Helper._
import io.Operation
import io.Operation._

import cats.effect.IO
import cats.implicits._
import fs2.Stream

class Spec extends munit.CatsEffectSuite:

  def compile[N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
      cards: internal.cards.Pool[K1 |>: K2 |>: K3 |>: TNil]
  )(using ValueOf[N1], ValueOf[N2], ValueOf[N3]): List[Operation] =
    cards.i match
      case k1 |>: k2 |>: k3 |>: _ => List(k1.asOp, k2.asOp, k3.asOp)

  test("card selection (simple)") {
    import internal.cards.Card._
    val selected = compile(card.select[ATK, 2].select[NP, 2].select[NP, 1])
    val expected = List(CardOp(ATK(), 2), CardOp(NP(), 2), CardOp(NP(), 1))
    IO(assertEquals(selected, expected))
  }

  test("skill selection (simple)") {
    import internal.skills.SkillType._
    import internal.skills.Performer._
    val selected = skill
      .select[Servant[1], 1, Normal]
      .select[Servant[2], 2, Target[1]]
      .select[Master, 3, OrderChange[1, 1]]
      .ops
      .reverse
      .toList
    val expected: List[SkillOp] = List(
      SkillOp(Servant[1](Normal()), 1),
      SkillOp(Servant[2](Target[1]()), 2),
      SkillOp(Master(OrderChange[1, 1]()), 3)
    )
    IO(assertEquals(selected, expected))
  }

  test("skill & card selection") {
    import unsafe.turns.Turn._
    import internal.cards.Card._
    import internal.skills.SkillType._
    import internal.skills.Performer._
    val selected = turn[IO]
      .selectSkills(
        skill
          .select[Servant[1], 1, Normal]
          .select[Servant[2], 2, Target[1]]
          .select[Master, 3, OrderChange[2, 1]]
          .select[Servant[2], 1, Normal]
      )
      .selectCards(card.select[ATK, 1].select[ATK, 2].select[ATK, 3])
    val expected = turn[IO]
      .unsafeSelectSkills(
        unsafe.skills.Skill.unsafeSelectSkills(
          Servant[1](Normal()) -> 1,
          Servant[2](Target[1]()) -> 2,
          Master(OrderChange[2, 1]()) -> 3,
          Servant[2](Normal()) -> 1
        )
      )
      .unsafeSelectCards(
        unsafe.cards.Card.unsafeSelectCards(ATK[1](), ATK[2](), ATK[3]())
      )
    for
      s <- selected.build.compile.toList
      e <- expected.build.compile.toList
    yield assertEquals(s, e)
  }
