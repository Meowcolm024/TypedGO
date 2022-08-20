package io

import internal.skills.{Performer, SkillType}
import internal.cards.Card

import cats.Show

enum Operation:
  case CardOp(card: Card[Nothing], idx: Int)
  case SkillOp(pf: Performer, idx: Int)

given Show[Operation] with
  def show(op: Operation): String = op match
    case Operation.CardOp(card, idx) =>
      card match
        case Card.ATK() => s"[card] ATK $idx"
        case Card.NP()  => s"[card] NP $idx"
    case Operation.SkillOp(pf, idx) =>
      pf match
        case p: Performer.Servant[_] => s"[skill] Servant ${p.pos} [$idx] ${p.ty}"
        case p: Performer.Master     => s"[skill] Master [$idx] ${p.ty}"
