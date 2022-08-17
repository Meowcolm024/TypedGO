package io

import internal.cards.Card

enum Operation:
  case CardOp(card: Card[Nothing], idx: Int)
  case SkillOp(ty: SkillType)

enum SkillType:
  case Normal()
  case Target[A]()
  case OrderChange[A, B]()
