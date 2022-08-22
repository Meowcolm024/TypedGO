package io

import internal.skills.Performer
import internal.cards.Card

import cats.Show
import cats.implicits._

enum Operation:
  case Attack()
  case CardOp(card: Card[Nothing], idx: Int)
  case SkillOp(pf: Performer, idx: Int)

given Show[Operation] with
  def show(op: Operation): String = op match
    case Operation.Attack() => "[attack]"
    case Operation.CardOp(card, idx) =>
      card match
        case Card.ATK() => s"[card] ATK $idx"
        case Card.NP()  => s"[card] NP $idx"
    case Operation.SkillOp(pf, idx) =>
      pf match
        case p: Performer.Servant[_] =>
          s"[skill] Servant ${p.pos} Skill $idx ${p.ty.show}"
        case p: Performer.Master => s"[skill] Master Skill $idx ${p.ty.show}"
