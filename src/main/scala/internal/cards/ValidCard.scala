package internal.cards

import scala.annotation.implicitNotFound

@implicitNotFound("Invalid card: ${F}_${N}")
trait ValidCard[F[_], N]:
  private def inst(using i: Inst[F]): F[N] = i.inst

object ValidCard:

  def inst[F[_], N](using v: ValidCard[F, N])(using Inst[F]) = v.inst

end ValidCard
