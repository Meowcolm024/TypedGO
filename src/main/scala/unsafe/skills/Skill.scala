package unsafe.skills

import internal.skills.Performer
import unsafe.Unsafe
import io.Operation

import fs2.Stream
import cats.effect.Sync

object Skill:

  def unsafeSelectSkills[F[_]: Sync](
      skill: (Performer, Int)*
  ): Unsafe ?=> Stream[F, Operation] =
    Stream(skill.map(Operation.SkillOp(_, _)): _*)

end Skill
