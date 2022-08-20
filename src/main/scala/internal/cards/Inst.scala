package internal.cards

trait Inst[F[_]]:
  def inst[N]: F[N]
