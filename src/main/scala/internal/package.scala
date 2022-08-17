import internal.cards.{Pool, Inst, Card}
import internal.cards.Card._
import internal.util.Helper._
import io.Operation

import fs2.Stream
import cats.effect.Sync

package object internal:

  def pool: Pool[TNil] = Pool.empty

  extension [N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
      p: Pool[K1 |>: K2 |>: K3 |>: TNil]
  )(using ValueOf[N1], ValueOf[N2], ValueOf[N3])
    def build[F[_]: Sync]: Stream[F, Operation] = p.i match
      case k1 |>: k2 |>: k3 |>: _ =>
        Stream(k1.asOp, k2.asOp, k3.asOp)

  given Inst[ATK] with
    override def inst[N]: ATK[N] = ATK[N]()

  given Inst[NP] with
    override def inst[N]: NP[N] = NP[N]()

end internal
