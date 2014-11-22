package pl.szymonmatejczyk.collatz

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
class CollatzLord extends Actor with ActorLogging {
  var nextToCompute = BigInt(1)

  var knownPeasants = Set[ActorRef]()

  override def receive: Receive = {
    case IamHereToServeYouLord =>
      sender ! Compute(nextToCompute)
      nextToCompute += 1
      peasantRegistered(sender)
    case Result(n: BigInt, steps: BigInt) =>
      addResult(n, steps)
  }

  def peasantRegistered(actor: ActorRef): Unit = {
    if (!knownPeasants.contains(actor)) {
      log.info(s"New peasant ${actor.path}")
      knownPeasants += actor
    }
  }

  def addResult(n: BigInt, steps: BigInt): Unit = {
    if (n.intValue() % 10000 == 0) {
      log.info(s"$n reached (result: $steps) - (${knownPeasants.size} peasants known")
    }
  }
}

object CollatzLord {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("CollatzLord", ConfigFactory.load("arbiter"))
    system.actorOf(Props[CollatzLord], "lord")
  }
}
