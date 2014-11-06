package pl.szymonmatejczyk.akkavoting

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

/**
 * Created by szymonmatejczyk on 22.10.14.
 */
object VotingApp {
  def main(args: Array[String]): Unit = {
    if (!args.isEmpty && args.head == "Arbiter")
      startArbiter()
    else {
      val conf = ConfigFactory.load("player")
      val names = Seq(conf.getString("akka.player.name"))
      startPlayer(names)
    }
  }

  def startArbiter() {
    val system = ActorSystem("VotingArbiter",
      ConfigFactory.load("arbiter"))
    val arbiter = system.actorOf(Props[VotingArbiter], "arbiter")

    println("Started Voting Game - waiting for messages")
    import system.dispatcher
    system.scheduler.scheduleOnce(1.second, arbiter, StartVoting)
  }

  val arbiterSystemIp = "127.0.0.1"
  def startPlayer(names: Seq[String]) {
    val system =
      ActorSystem("VotingPlayer", ConfigFactory.load("player"))
    val remotePath =
      s"akka.tcp://VotingArbiter@$arbiterSystemIp:2552/user/arbiter"
    names.foreach {
      name =>
        system.actorOf(Props(classOf[VotingPlayer], name, remotePath), name)
    }
  }

}
