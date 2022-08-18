package io

import io.Operation
import io.Driver._
import internal.turns.Turn.WithCard

import org.typelevel.log4cats.Logger
import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream

trait Operator[F[_]: Sync](val driver: Driver[F])(using Logger[F]):
  def operate(op: Operation): F[Unit]

  final def start(turns: WithCard[F]): F[Unit] =
    turns.build.evalTap(operate).compile.drain

object Operator:

  def AdbOperator[F[_]: Sync](
      adbDriver: Driver[F],
      shift: Int = 0
  )(using Logger[F]): Operator[F] =
    new Operator[F](adbDriver) {
      import internal.cards.Card._
      import cats.effect.std.Random

      private val atks =
        List((190, 760), (560, 760), (960, 760), (1340, 760), (1740, 760))
      private val nps = List((620, 320), (970, 320), (1310, 320))
      private def shiftPoint(p: (Int, Int)): F[(Int, Int)] =
        if shift == 0 then p.pure
        else
          for
            rnd <- Random.scalaUtilRandom[F]
            x <- rnd.betweenInt(-shift, shift)
            y <- rnd.betweenInt(-shift, shift)
          yield (p._1 + x, p._2 + y)

      override def operate(op: Operation): F[Unit] =
        op match
          case Operation.CardOp(ATK(), i) =>
            shiftPoint(atks(i - 1)).flatMap(driver.tap.tupled)
          case Operation.CardOp(NP(), i) =>
            shiftPoint(nps(i - 1)).flatMap(driver.tap.tupled)
          case Operation.SkillOp(_) => Sync[F].unit
    }

  def TestOperator[F[_]: Sync](using Logger[F]): Operator[F] =
    new Operator[F](null) {
      override def operate(op: Operation): F[Unit] =
        Logger[F].info(s"select ${op.show}")
    }

end Operator
