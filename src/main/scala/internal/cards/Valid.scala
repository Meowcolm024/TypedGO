package internal.cards

import Card._

trait Valid[F[_], N] { private[cards] def inst: F[N] }

object Valid:

  def inst[F[_], N](using v: Valid[F, N]) = v.inst

  given bc1: Valid[ATK, 1] with
    override def inst: ATK[1] = ATK[1]()

  given bc2: Valid[ATK, 2] with
    override def inst: ATK[2] = ATK[2]()

  given bc3: Valid[ATK, 3] with
    override def inst: ATK[3] = ATK[3]()

  given bc4: Valid[ATK, 4] with
    override def inst: ATK[4] = ATK[4]()

  given bc5: Valid[ATK, 5] with
    override def inst: ATK[5] = ATK[5]()

  given bn1: Valid[NP, 1] with
    override def inst: NP[1] = NP[1]()

  given bn2: Valid[NP, 2] with
    override def inst: NP[2] = NP[2]()

  given bn3: Valid[NP, 3] with
    override def inst: NP[3] = NP[3]()

end Valid
