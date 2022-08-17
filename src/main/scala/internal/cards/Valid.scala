package internal.cards

import Card._

trait Inst[F[_]] { def inst[N]: F[N] }

trait Valid[F[_], N] {
  private[cards] def inst(using i: Inst[F]): F[N] = i.inst
}

object Valid:

  def inst[F[_], N](using v: Valid[F, N])(using Inst[F]) = v.inst

  given bc1: Valid[ATK, 1] with {}

  given bc2: Valid[ATK, 2] with {}

  given bc3: Valid[ATK, 3] with {}

  given bc4: Valid[ATK, 4] with {}

  given bc5: Valid[ATK, 5] with {}

  given bn1: Valid[NP, 1] with {}

  given bn2: Valid[NP, 2] with {}

  given bn3: Valid[NP, 3] with {}

end Valid
