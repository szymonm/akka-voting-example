package pl.szymonmatejczyk.akkavoting

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

/**
 * Created by szymonmatejczyk on 22.10.14.
 */
object VotingApp {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "Arbiter")
      startArbiter()
    if (!args.isEmpty && args.head != "Arbiter")
      startPlayer(args.head)
  }

  def startArbiter() {
    val system = ActorSystem("VotingArbiter",
      ConfigFactory.load("arbiter"))
    val arbiter = system.actorOf(Props[VotingArbiter], "arbiter")

    println("Started Voting Game - waiting for messages")
    import system.dispatcher
    system.scheduler.scheduleOnce(1.second, arbiter, StartVoting)
  }

  def startPlayer(name: String) {
    val system =
      ActorSystem("VotingPlayer", ConfigFactory.load("player"))
    val remotePath =
      "akka.tcp://VotingArbiter@127.0.0.1:2552/user/arbiter"
    system.actorOf(Props(classOf[VotingPlayer], name, remotePath), name)
  }

}
