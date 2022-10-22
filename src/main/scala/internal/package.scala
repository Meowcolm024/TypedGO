import internal.cards.{Pool, Card, Inst, ValidCard}
import internal.turns.Turn.Start
import internal.skills.{Performer, Skill}
import internal.skills.Valid._
import internal.util.Helper._

import io.Operation

import fs2.Stream
import cats.effect.Sync

package object internal:

  type Turn = internal.turns.Turn

  type Card[A] = internal.cards.Card[A]
  type ATK[A] = internal.cards.Card.ATK[A]
  type NP[A] = internal.cards.Card.NP[A]

  type Performer = internal.skills.Performer
  type Servant[A] = internal.skills.Performer.Servant[A]
  type Master = internal.skills.Performer.Master

  type SkillType = internal.skills.SkillType
  type Normal = internal.skills.SkillType.Normal
  type Target[A] = internal.skills.SkillType.Target[A]
  type OrderChange[A, B] = internal.skills.SkillType.OrderChange[A, B]

  def turn[F[_]: Sync]: Start[F] = Start(Stream.empty)

  def card: Pool[TNil] = Pool.empty

  def skill: Skill = Skill(LazyList.empty)

  /* typeclass instances */

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

  given vs1: ValidServant[Servant[1]] with
    override def inst(ty: SkillType): Servant[1] = Performer.Servant[1](ty)

  given vs2: ValidServant[Servant[2]] with
    override def inst(ty: SkillType): Servant[2] = Performer.Servant[2](ty)

  given vs3: ValidServant[Servant[3]] with
    override def inst(ty: SkillType): Servant[3] = Performer.Servant[3](ty)

  given vm: ValidServant[Master] with
    override def inst(ty: SkillType): Master = Performer.Master(ty)

  given vsn: ValidSkillType[Normal] with
    override def inst: Normal = internal.skills.SkillType.Normal()

  given vst[N](using
      ValidServant[Servant[N]],
      ValueOf[N]
  ): ValidSkillType[Target[N]] with
    override def inst: Target[N] = internal.skills.SkillType.Target[N]()

  given vso[A, B](using
      ValidServant[Servant[A]],
      ValidServant[Servant[B]],
      ValueOf[A],
      ValueOf[B]
  ): ValidSkillType[OrderChange[A, B]] with
    override def inst: OrderChange[A, B] =
      internal.skills.SkillType.OrderChange[A, B]()

  given Inst[ATK] with
    override def inst[N]: ATK[N] = Card.ATK[N]()

  given Inst[NP] with
    override def inst[N]: NP[N] = Card.NP[N]()

end internal
