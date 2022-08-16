package io

import io.Operation
import io.Driver._

import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream

trait Operator[F[_]: Sync](drv: Driver[F]):
  def operate(op: Operation): F[Unit]

  final def driver: Driver[F] = drv
  final def start(ops: Stream[F, Operation]): F[Unit] =
    ops.evalTap(operate).compile.drain

object Operator:

  def AdbOperator[F[_]: Sync] = new Operator[F](AdbDriver[F]) {
    override def operate(op: Operation): F[Unit] =
      op match
        case Operation.CardOp(_, _) => driver.tap(1, 1)
        case Operation.SkillOp()    => Sync[F].unit

  }

  val TestOperator = new Operator[IO](TestDriver) {
    override def operate(op: Operation): IO[Unit] =
      IO.println(op) *> TestDriver.tap(1, 1)
  }

end Operator
