package internal.skills

sealed trait Performer:
  def ty: SkillType

object Performer:

  type Performer = internal.skills.Performer

  case class Servant[A](ty: SkillType)(using ValueOf[A]) extends Performer {
    def pos: Int = valueOf[A].asInstanceOf[Int]
  }
  case class Master(ty: SkillType) extends Performer

end Performer
