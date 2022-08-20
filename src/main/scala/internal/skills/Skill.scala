package internal.skills

import Valid._

object Skill:

  def skill[P <: Performer, N](ty: SkillType)(using ValidServant[P])(using
      ValidSkill[N]
  ) = ???

end Skill
