package io

import io.Operation
import io.Driver._

import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream

trait Operator[F[_]: Sync](val driver: Driver[F]):
  def operate(op: Operation): F[Unit]

  final def start(ops: Stream[F, Operation]): F[Unit] =
    ops.evalTap(operate).compile.drain

object Operator:

  class AdbOperator[F[_]: Sync] extends Operator[F](AdbDriver[F]) {
    override def operate(op: Operation): F[Unit] =
      op match
        case Operation.CardOp(_, _) => driver.tap(1, 1)
        case Operation.SkillOp(_)   => Sync[F].unit

  }

  object TestOperator extends Operator[IO](TestDriver) {
    override def operate(op: Operation): IO[Unit] =
      IO.println(s"select ${op.show}")
  }

end Operator
