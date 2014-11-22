package pl.szymonmatejczyk.collatz

import akka.actor.Actor.Receive
import akka.actor._
import com.typesafe.config.ConfigFactory
import pl.szymonmatejczyk.akkavoting.VotingPlayer

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
class CollatzPeasant extends Actor with ActorLogging {


  def computeCollatzLength(n: BigInt) = {
    CollatzSequence.from(n).size()
  }

  override def receive: Receive = {
    case CollatzPeasant.Start(arbiterPath) =>
      context.actorSelection(arbiterPath) ! IamHereToServeYouLord
    case Compute(n: BigInt) =>
      sender ! Result(n, computeCollatzLength(n))
      log.info("working hard...")
      sender ! IamHereToServeYouLord
  }
}

object CollatzPeasant {
  case class Start(arbiterPath: String)

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load("peasant")
    val system = ActorSystem("CollatzPeasant", conf)
    val arbiterSystemIp = conf.getString("akka.lord.hostname")
    val name = conf.getString("akka.peasant.name")
    val remotePath =
      s"akka.tcp://CollatzLord@$arbiterSystemIp:2552/user/lord"
    val actor = system.actorOf(Props(classOf[CollatzPeasant]), name)
    actor ! Start(remotePath)
  }
}
