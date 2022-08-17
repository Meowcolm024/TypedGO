package io

import io.Operation
import io.Driver._
import internal.turns.Turn.WithCard

import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream

trait Operator[F[_]: Sync](val driver: Driver[F]):
  def operate(op: Operation): F[Unit]

  final def start(turns: WithCard[F]): F[Unit] =
    turns.build.evalTap(operate).compile.drain

object Operator:

  class AdbOperator[F[_]: Sync](val shift: Int = 0)
      extends Operator[F](AdbDriver[F]) {
    import internal.cards.Card._
    import cats.effect.std.Random

    private val atks =
      List((190, 760), (560, 760), (960, 760), (1340, 760), (1740, 760))
    private val nps = List((620, 320), (970, 320), (1310, 320))
    private def shiftPoint(p: (Int, Int)): F[(Int, Int)] =
      for
        rnd <- Random.scalaUtilRandom[F]
        x <- rnd.betweenInt(-shift, shift)
        y <- rnd.betweenInt(-shift, shift)
      yield (p._1 + x, p._2 + y)

    override def operate(op: Operation): F[Unit] =
      op match
        case Operation.CardOp(ATK(), i) =>
          shiftPoint(atks(i)).flatMap(driver.tap.tupled)
        case Operation.CardOp(NP(), i) =>
          shiftPoint(nps(i)).flatMap(driver.tap.tupled)
        case Operation.SkillOp(_) => Sync[F].unit

  }

  object TestOperator extends Operator[IO](TestDriver) {
    override def operate(op: Operation): IO[Unit] =
      IO.println(s"select ${op.show}")
  }

end Operator
