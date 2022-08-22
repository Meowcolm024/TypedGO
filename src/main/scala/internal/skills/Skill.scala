package internal.skills

import internal.SkillType
import Valid._

import io.Operation

case class Skill(ops: LazyList[Operation.SkillOp]) {

  def select[P <: Performer, N, T <: SkillType](using
      pf: ValidServant[P],
      sk: ValidSkill[N],
      st: ValidSkillType[T],
      vl: ValueOf[N]
  ): Skill = Skill(Operation.SkillOp(pf.inst(st.inst), sk.inst) #:: ops)

}
