package io

import internal.cards.Card

import cats.Show

enum Operation:
  case CardOp(card: Card[Nothing], idx: Int)
  case SkillOp(ty: SkillType)

enum SkillType:
  case Normal()
  case Target[A]()
  case OrderChange[A, B]()

given Show[Operation] with
  def show(op: Operation): String = op match
    case Operation.CardOp(card, idx) =>
      card match
        case Card.ATK() => s"[card] ATK $idx"
        case Card.NP()  => s"[card] NP $idx"
    case Operation.SkillOp(ty) => s"[skill] $ty"
