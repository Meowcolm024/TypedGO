package internal.util

object Helper:

  trait =!=[A, B]
  object =!= {
    implicit def ev[A, B]: A =!= B = null

    @annotation.implicitAmbiguous("Could not prove ${A} =!= ${A}")
    implicit def err1[A]: A =!= A = null
    implicit def err2[A]: A =!= A = null
  }

  sealed trait TList
  case class TNil() extends TList
  case class |>:[H, T <: TList](h: H, t: T) extends TList

  sealed trait HList
  class HNil extends HList
  class ::[H, T <: HList] extends HList

  sealed trait Nat
  class Z extends Nat
  class S[A <: Nat] extends Nat

  object Nat {
    type _0 = Z
    type _1 = S[_0]
    type _2 = S[_1]
    type _3 = S[_2]
    type _4 = S[_3]
    type _5 = S[_4]
    type _6 = S[_5]
    type _7 = S[_6]
    type _8 = S[_7]
  }

  trait =<=[A, B]
  object =<= {
    implicit def leZ[A <: Nat]: Z =<= A = null
    implicit def leS[A <: Nat, B <: Nat](using A =<= B): S[A] =<= S[B] = null
  }

end Helper
