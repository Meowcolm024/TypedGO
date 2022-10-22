package io

import io.Operation
import io.Driver._
import internal.turns.Turn.Done
import internal.skills.SkillType

import org.typelevel.log4cats.Logger
import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream

trait Operator[F[_]: Sync](val driver: Driver[F])(using Logger[F]):
  def operate(op: Operation): F[Unit]

  final def start(turns: Done[F]): F[Unit] =
    turns.build.evalTap(operate).compile.drain

object Operator:

  def AdbOperator[F[_]: Sync](
      adbDriver: Driver[F],
      shift: Int = 0
  )(using Logger[F]): Operator[F] =
    new Operator[F](adbDriver) {
      import internal.cards.Card._
      import internal.skills.Performer._
      import cats.effect.std.Random

      private val atks =
        List((190, 760), (560, 760), (960, 760), (1340, 760), (1740, 760))
      private val nps = List((620, 320), (970, 320), (1310, 320))

      private val ssk =
        List(
          List((100, 850), (250, 850), (380, 850)),
          List((580, 850), (730, 850), (860, 850)),
          List((1060, 850), (1200, 850), (1340, 850))
        )
      private val msk = List((1360, 470), (1490, 470), (1630, 470))

      private val targets = List((500, 680), (960, 680), (1440, 680))
      private val change = List(
        List((200, 530), (520, 530), (820, 530)),
        List((1110, 530), (1400, 530), (1710, 530))
      )

      private def shiftPoint(p: Option[(Int, Int)]): F[(Int, Int)] =
        val pt = Sync[F].fromOption(p, new Exception("Invalid operation"))
        if shift == 0 then pt
        else
          for
            p <- pt
            rnd <- Random.scalaUtilRandom[F]
            x <- rnd.betweenInt(-shift, shift)
            y <- rnd.betweenInt(-shift, shift)
          yield (p._1 + x, p._2 + y)

      private def selectCard(pos: Option[(Int, Int)]): F[Unit] =
        for
          card <- shiftPoint(pos)
          _ <- driver.tap.tupled(card)
        yield ()

      private def selectSkill(pos: Option[(Int, Int)], st: SkillType): F[Unit] =
        st match
          case n @ SkillType.Normal() =>
            shiftPoint(pos).flatMap(driver.tap.tupled)
          case t @ SkillType.Target() =>
            for
              pt <- shiftPoint(pos)
              _ <- driver.tap.tupled(pt)
              tar <- shiftPoint(targets.get(t.target - 1))
              _ <- driver.tap.tupled(tar)
            yield ()
          case o @ SkillType.OrderChange() =>
            for
              pt <- shiftPoint(pos)
              _ <- driver.tap.tupled(pt)
              f <- shiftPoint(change.get(0).flatMap(_.get(o.from - 1)))
              _ <- driver.tap.tupled(f)
              t <- shiftPoint(change.get(1).flatMap(_.get(o.to - 1)))
              _ <- driver.tap.tupled(t)
            yield ()

      override def operate(op: Operation): F[Unit] =
        op match
          case Operation.Attack() =>
            shiftPoint(Some((1700, 900))).flatMap(driver.tap.tupled)
          case Operation.CardOp(ATK(), i) => selectCard(atks.get(i - 1))
          case Operation.CardOp(NP(), i)  => selectCard(nps.get(i - 1))
          case Operation.SkillOp(s @ Servant(ty), idx) =>
            selectSkill(ssk.get(s.pos - 1).flatMap(_.get(idx - 1)), ty)
          case Operation.SkillOp(Master(ty), idx) =>
            for
              pt <- shiftPoint(Some((1780, 470))) // unfold master skills
              _ <- driver.tap.tupled(pt)
              _ <- selectSkill(msk.get(idx), ty)
            yield ()
    }

  def TestOperator[F[_]: Sync](using Logger[F]): Operator[F] =
    new Operator[F](null) {
      override def operate(op: Operation): F[Unit] =
        Logger[F].info(s"select ${op.show}")
    }

end Operator
