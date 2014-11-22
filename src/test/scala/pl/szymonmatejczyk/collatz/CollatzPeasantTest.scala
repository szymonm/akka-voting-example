package pl.szymonmatejczyk.collatz

import akka.actor.ActorSystem
import akka.testkit.{TestKit, ImplicitSender, TestActorRef}
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
class CollatzPeasantTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def worker = {
    TestActorRef(new CollatzPeasant)
  }

  "CollatzWorker" should {
    "compute length of collatz sequence from 7" in {
      worker ! Compute(BigInt(7))
      expectMsg(Result(7, 17))
    }
    "compute length of Collatz sequence from 3" in {
      worker ! Compute(BigInt(3))
      expectMsg(Result(3, 8))
    }
  }

}
