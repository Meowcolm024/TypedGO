package internal.cards

import internal.util.Helper._
import io.Operation

import fs2.Stream
import cats.effect.Sync

private[internal] case class Pool[A](i: A)

object Pool:
  def empty: Pool[TNil] = Pool[TNil](TNil())

  extension [N1, N2, N3, K1 <: Card[N1], K2 <: Card[N2], K3 <: Card[N3]](
      p: Pool[K1 |>: K2 |>: K3 |>: TNil]
  )(using ValueOf[N1], ValueOf[N2], ValueOf[N3])
    def build[F[_]: Sync]: Stream[F, Operation] = p.i match
      case k1 |>: k2 |>: k3 |>: _ =>
        Stream(k1.asOp, k2.asOp, k3.asOp)

  extension (p: Pool[TNil])
    def add[F[_], N](using Valid[F, N]): Pool[F[N] |>: TNil] =
      Pool(|>:(Valid.inst, TNil()))

  extension [X[_], A](p: Pool[X[A] |>: TNil])(using Valid[X, A])
    def add[F[_], N](using Valid[F, N])(using
        X[A] =!= F[N]
    ): Pool[X[A] |>: F[N] |>: TNil] = Pool(|>:(p.i.h, |>:(Valid.inst, TNil())))

  extension [X[_], A, Y[_], B](
      p: Pool[X[A] |>: Y[B] |>: TNil]
  )(using Valid[X, A], Valid[Y, B])
    def add[F[_], N](using Valid[F, N])(using
        X[A] =!= Y[B],
        X[A] =!= F[N],
        Y[B] =!= F[N]
    ): Pool[X[A] |>: Y[B] |>: F[N] |>: TNil] = Pool(
      |>:(p.i.h, |>:(p.i.t.h, |>:(Valid.inst, TNil())))
    )
