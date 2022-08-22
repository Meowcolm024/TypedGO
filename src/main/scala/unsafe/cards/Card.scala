package unsafe.cards

import internal.cards
import io.Operation
import unsafe.Unsafe

import fs2.Stream
import cats.effect.Sync

object Card:

  def unsafeSelectCards[F[_]: Sync, N1, N2, N3](
      k1: cards.Card[N1],
      k2: cards.Card[N2],
      k3: cards.Card[N3]
  )(using
      ValueOf[N1],
      ValueOf[N2],
      ValueOf[N3]
  ): Unsafe ?=> Stream[F, Operation] =
    Stream(Operation.Attack(), k1.asOp, k2.asOp, k3.asOp)

end Card
