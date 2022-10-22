package unsafe.turns

import internal.turns.Turn.WithSkill
import io.Operation
import unsafe.Unsafe

import fs2.Stream
import cats.effect.Sync
import internal.turns.Turn._

object Turn:

  extension [F[_]: Sync](turn: WithSkill[F])
    def unsafeSelectCards(cards: Unsafe ?=> Stream[F, Operation]): Done[F] =
      given Unsafe = unsafe.unsafely
      Done(turn.ops ++ cards)

  extension [F[_]: Sync](turn: Start[F])
    def unsafeSelectSkills(
        skills: Unsafe ?=> Stream[F, Operation]
    ): WithSkill[F] =
      given Unsafe = unsafe.unsafely
      WithSkill(turn.ops ++ skills)

end Turn
