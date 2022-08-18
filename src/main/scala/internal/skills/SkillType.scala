package internal.skills

enum SkillType:
  case Normal()
  case Target[A]()
  case OrderChange[A, B]()
