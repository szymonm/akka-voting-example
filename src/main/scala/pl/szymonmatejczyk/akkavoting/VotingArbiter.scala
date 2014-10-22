package pl.szymonmatejczyk.akkavoting

import akka.actor.{ActorRef, Actor}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import akka.event.Logging

/**
 * Created by szymonmatejczyk on 22.10.14.
 */

case class Player(ref: ActorRef, name: String)

class VotingArbiter extends Actor {
  val log = Logging(context.system, this)

  val moneyPerRound = 100

  val roundTimeSec = 10

  val minPlayers = 1

  var players = Set[Player]()

  var votes = Map[Player, Int]()

  var gains = Map[Player, Int]()

  def info(s: String) = println(s)

  def receive = {
    case StartVoting =>
      import context.dispatcher
      if (players.size >= minPlayers) {
        info("Starting new voting")
        players.foreach(
          p => p.ref ! VotingStart(roundTimeSec)
        )
        context.system.scheduler.scheduleOnce(Duration.create(roundTimeSec,
          TimeUnit.SECONDS), self, FinishVoting)
      } else {
        info("Not enough players found - waiting sad")
        context.system.scheduler.scheduleOnce(Duration.create(5,
          TimeUnit.SECONDS), self, StartVoting)
      }

    case Vote(name: String, vote: Int) =>
      val player = Player(sender(), name)
      if (!votes.contains(player)) {
        votes = votes + ((player, vote))
      }
    case FinishVoting =>
      info("Finishing voting")
      val moneyDivision = divideMoney(votes, moneyPerRound)
      clearVotes
      addGains(moneyDivision)
      info("Current gains")
      printGains
      self ! StartVoting
    case WannaPlay(name: String) =>
      info(s"Welcoming $name")
      players = players + Player(sender(), name)
    case GetPeers =>
      sender ! Peers(players.map(_.ref))
  }


  def divideMoney(votes: Map[Player, Int], money: Int): Map[Player, Int] = {
    if (votes.size > 0) {
      val votesGrouped = votes.groupBy(_._2).mapValues(_.size)
      val maxVotes: Int = votesGrouped.maxBy(_._2)._2
      val optionsChosen = votesGrouped.filter(_._2 == maxVotes).map(_._1).toSet
      val winners = votes.filter(x => optionsChosen.contains(x._2)).map(_._1)
      Map() ++ winners.map(winner => winner -> money / winners.size)
    } else Map()
  }

  def addGains(roundGains: Map[Player, Int]) {
    roundGains.foreach {
      case (player, gain) =>
        val alreadyGained = gains.getOrElse(player, 0)
        gains = gains + ((player, alreadyGained + gain))
    }
  }

  def printGains {
    info("Gains:")
    gains.toList.sortBy(_._2).foreach(g => info(s"${g._1}: ${g._2}"))
  }


  def clearVotes {
    votes = Map[Player, Int]()
  }
}
