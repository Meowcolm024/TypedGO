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

end Helper
