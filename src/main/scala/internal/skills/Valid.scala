package internal.skills

import internal.SkillType
import scala.annotation.implicitNotFound

object Valid:

  @implicitNotFound("Invalid skill: Skill#${N}")
  trait ValidSkill[N] {
    def inst(using ValueOf[N]): Int = valueOf[N].asInstanceOf[Int]
  }

  @implicitNotFound("Invalid servant: Servant#${S}")
  trait ValidServant[S] { def inst(ty: SkillType): S }

  @implicitNotFound("Invalid skill type: SkillType#${S}")
  trait ValidSkillType[S] { def inst: S }

end Valid
