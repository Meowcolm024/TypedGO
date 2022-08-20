package internal.skills

enum SkillType:
  case Normal
  case Target[A](t: A)
  case OrderChange[A, B](f: A, t: B)
    