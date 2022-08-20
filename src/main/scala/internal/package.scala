import internal.cards.{Pool, Card, Inst, ValidCard}
import internal.cards.Card._
import internal.turns.Turn.WithCard
import internal.util.Helper._
import internal.skills.Performer._
import internal.skills.Valid._

import io.Operation

import fs2.Stream
import cats.effect.Sync

package object internal:

  type Card[A] = internal.cards.Card[A]
  type Turn = internal.turns.Turn
  type Pool[A] = internal.cards.Pool[A]

  def turn[F[_]: Sync]: WithCard[F] = WithCard(Stream.empty)

  def card: Pool[TNil] = Pool.empty

  given vc1: ValidCard[ATK, 1] with {}
  given vc2: ValidCard[ATK, 2] with {}
  given vc3: ValidCard[ATK, 3] with {}
  given vc4: ValidCard[ATK, 4] with {}
  given vc5: ValidCard[ATK, 5] with {}

  given vn1: ValidCard[NP, 1] with {}
  given vn2: ValidCard[NP, 2] with {}
  given vn3: ValidCard[NP, 3] with {}

  given vk1: ValidSkill[1] with {}
  given vk2: ValidSkill[2] with {}
  given vk3: ValidSkill[3] with {}

  given vs1: ValidServant[Servant[1]] with {}
  given vs2: ValidServant[Servant[2]] with {}
  given vs3: ValidServant[Servant[3]] with {}
  given vm: ValidServant[Master] with {}

  given Inst[ATK] with
    override def inst[N]: ATK[N] = ATK[N]()

  given Inst[NP] with
    override def inst[N]: NP[N] = NP[N]()

end internal
