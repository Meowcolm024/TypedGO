package unsafe.turns

import internal.turns.Turn.WithSkill
import io.Operation
import unsafe.Unsafe

import fs2.Stream
import cats.effect.Sync
import internal.turns.Turn._

object Turn:

  extension [F[_]: Sync](turn: WithSkill[F])
    def unsafeSelectCards(cards: Unsafe[Stream[F, Operation]]): WithCard[F] =
      WithCard(turn.ops ++ cards.get)

  extension [F[_]: Sync](turn: WithCard[F])
    def unsafeSelectSkills(): WithSkill[F] =
      WithSkill(turn.ops)

end Turn
