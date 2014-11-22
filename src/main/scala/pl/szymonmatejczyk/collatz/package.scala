package pl.szymonmatejczyk

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
package object collatz {
  trait CollatzMessage
  case class Compute(n: BigInt) extends CollatzMessage
  case class Result(n: BigInt, steps: BigInt) extends CollatzMessage
  case object IamHereToServeYouLord extends CollatzMessage
}
