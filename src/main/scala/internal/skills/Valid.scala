package internal.skills

import internal.SkillType

object Valid:

  trait ValidSkill[N] {
    def inst(using ValueOf[N]): Int = valueOf[N].asInstanceOf[Int]
  }

  trait ValidServant[S] { def inst(ty: SkillType): S }

  trait ValidSkillType[S] { def inst: S }

end Valid
