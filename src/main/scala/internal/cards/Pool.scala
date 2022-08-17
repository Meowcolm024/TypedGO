package internal.cards

import internal.util.Helper._
import io.Operation

private[internal] case class Pool[A](i: A)

object Pool:
  def empty: Pool[TNil] = Pool[TNil](TNil())

  extension (p: Pool[TNil])
    def select[F[_], N](using Valid[F, N])(using Inst[F]): Pool[F[N] |>: TNil] =
      Pool(|>:(Valid.inst, TNil()))

  extension [X[_], A](p: Pool[X[A] |>: TNil])(using Valid[X, A])
    def select[F[_], N](using Valid[F, N])(using Inst[F])(using
        X[A] =!= F[N]
    ): Pool[X[A] |>: F[N] |>: TNil] = Pool(|>:(p.i.h, |>:(Valid.inst, TNil())))

  extension [X[_], A, Y[_], B](
      p: Pool[X[A] |>: Y[B] |>: TNil]
  )(using Valid[X, A], Valid[Y, B])
    def select[F[_], N](using Valid[F, N])(using Inst[F])(using
        X[A] =!= Y[B],
        X[A] =!= F[N],
        Y[B] =!= F[N]
    ): Pool[X[A] |>: Y[B] |>: F[N] |>: TNil] = Pool(
      |>:(p.i.h, |>:(p.i.t.h, |>:(Valid.inst, TNil())))
    )
