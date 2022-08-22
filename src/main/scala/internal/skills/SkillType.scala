package internal.skills

import cats.Show

sealed trait SkillType

object SkillType:

  case class Normal() extends SkillType

  case class Target[A]()(using ValueOf[A]) extends SkillType {
    def target: Int = valueOf[A].asInstanceOf[Int]
  }

  case class OrderChange[A, B]()(using ValueOf[A], ValueOf[B])
      extends SkillType {
    def from: Int = valueOf[A].asInstanceOf[Int]
    def to: Int = valueOf[B].asInstanceOf[Int]
  }

  given Show[SkillType] with
    override def show(t: SkillType): String = t match
      case Normal()          => "Normal"
      case t @ Target()      => s"Target: Servant ${t.target}"
      case o @ OrderChange() => s"Change Servant ${o.from} to Servant ${o.to}"

end SkillType
