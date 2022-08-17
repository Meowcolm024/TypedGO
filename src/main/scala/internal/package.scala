import internal.cards.{Pool, Inst, Card}
import internal.cards.Card._
import internal.turns.Turn.WithCard
import internal.util.Helper._
import io.Operation

import fs2.Stream
import cats.effect.Sync

package object internal:

  def turn[F[_]: Sync]: WithCard[F] = WithCard(Stream.empty)

  def card: Pool[TNil] = Pool.empty

  given Inst[ATK] with
    override def inst[N]: ATK[N] = ATK[N]()

  given Inst[NP] with
    override def inst[N]: NP[N] = NP[N]()

end internal
