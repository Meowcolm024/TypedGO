package internal.cards

import io.Operation

sealed trait Card[A]:
  def asOp(using ValueOf[A]): Operation

object Card:

  case class ATK[A]() extends Card[A]:
    def asOp(using ValueOf[A]): Operation =
      Operation.CardOp(ATK[Nothing](), valueOf[A].asInstanceOf[Int])

  case class NP[A]() extends Card[A]:
    def asOp(using ValueOf[A]): Operation =
      Operation.CardOp(NP[Nothing](), valueOf[A].asInstanceOf[Int])
      
end Card
