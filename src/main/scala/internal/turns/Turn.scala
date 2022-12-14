package internal.turns

import io.Operation
import internal.cards._
import internal.skills._
import internal.util.Helper._

import fs2.Stream
import cats.effect.Sync

sealed trait Turn

object Turn:

  case class Start[F[_]: Sync](ops: Stream[F, Operation]) extends Turn {
    def selectSkills(): WithSkill[F] = WithSkill(ops)
    def selectSkills(skills: Skill): WithSkill[F] =
      WithSkill(ops ++ Stream(skills.ops.reverse: _*))
  }

  case class WithSkill[F[_]: Sync](ops: Stream[F, Operation]) extends Turn {
    def selectCards[N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
        cards: Pool[K1 |>: K2 |>: K3 |>: TNil]
    )(using ValueOf[N1], ValueOf[N2], ValueOf[N3]): Done[F] = cards.i match
      case k1 |>: k2 |>: k3 |>: _ =>
        Done(ops ++ Stream(Operation.Attack(), k1.asOp, k2.asOp, k3.asOp))
  }

  case class WithCard[F[_]: Sync](ops: Stream[F, Operation]) extends Turn {}

  case class Done[F[_]: Sync](ops: Stream[F, Operation]) extends Turn {
    def build: Stream[F, Operation] = ops
  }

end Turn
