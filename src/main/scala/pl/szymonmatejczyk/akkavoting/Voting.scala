package pl.szymonmatejczyk.akkavoting

import akka.actor.ActorRef

/**
 * Created by szymonmatejczyk on 22.10.14.
 */
trait Voting

case class WannaPlay(name: String) extends Voting
case object StartVoting extends Voting
case class VotingStart(time: Int) extends Voting
case class Vote(name: String, option: Int) extends Voting
case class VotingResult(gained: Int) extends Voting
case object FinishVoting extends Voting

trait PeerLookup

case object GetPeers extends PeerLookup
case class Peers(peers: List[ActorRef]) extends PeerLookup
