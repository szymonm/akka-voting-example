package pl.szymonmatejczyk.akkavoting

import akka.actor._
import akka.actor.Actor.Receive
import akka.event.Logging
import akka.actor.ActorIdentity
import scala.Some
import akka.actor.Identify
import scala.concurrent.duration._
import scala.util.Random

/**
 * Created by szymonmatejczyk on 22.10.14.
 */

case object SayHello

class VotingPlayer(name: String, arbiterPath: String) extends Actor {
  val log = Logging(context.system, this)

  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(arbiterPath) ! Identify(arbiterPath)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  def receive = identifying

  def identifying: Actor.Receive = {
    case ActorIdentity(`arbiterPath`, Some(actor)) =>
      context.watch(actor)
      context.become(playing(actor))
      self ! SayHello
    case ActorIdentity(`arbiterPath`, None) => println(s"Remote actor not available: $arbiterPath")
    case ReceiveTimeout              => sendIdentifyRequest()
    case _                           => println("Not ready yet")
  }

  var gained = 0

  def addGains(g: Int) {
    log.info(s"Ha! I gained another $g")
    gained += g
  }

  def playing(arbiter: ActorRef): Receive = {
    case SayHello =>
      arbiter ! WannaPlay(name)
    case VotingStart(time: Int) =>
      sender ! Vote(name, Random.nextInt(3))
    case VotingResult(g: Int) =>
      addGains(g)
    case Terminated(`arbiter`) =>
      println("Arbiter terminated")
      sendIdentifyRequest()
      context.become(identifying)
    case ReceiveTimeout =>
    // ignore
  }
}
