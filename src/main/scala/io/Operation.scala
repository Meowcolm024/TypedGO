package io

import internal.cards.Card

enum Operation:
  case CardOp(card: Card[_], idx: Int)
  case SkillOp()
