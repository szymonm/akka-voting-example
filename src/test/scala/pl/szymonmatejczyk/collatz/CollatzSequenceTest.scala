package pl.szymonmatejczyk.collatz

import org.scalatest.{Matchers, WordSpec}

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
class CollatzSequenceTest extends WordSpec with Matchers {
  "CollatzSequence" should {
    "reach 1" when {
      "starting from 1 in first step" in {
        CollatzSequence.from(1).iterator.slice(0, 10).toArray should be (Array.fill(10)(BigInt(1)))
      }
      "starting from 2 in second step" in {
        val sequence = CollatzSequence.from(2)
        sequence.iterator.next() should be (BigInt(2))

        sequence.iterator.slice(0, 10).toArray should be (Array.fill(10)(BigInt(1)))
      }
      "starting from 3" in {
        val sequence = CollatzSequence.from(3)

        sequence.iterator.slice(100, 105).toArray should be (Array.fill(5)(BigInt(1)))
      }
      "starting from 7" in {
        CollatzSequence.from(7).iterator.slice(25, 30).toArray should be (Array.fill(5)(BigInt(1)))
      }
      "starting from a big number" in {
        CollatzSequence.from(BigInt(8731827L)).iterator.slice(9000, 9005).toArray should be (Array.fill(5)(BigInt(1)))
      }
    }

    "calculate correct length" when {
      "starting from 1" in {
        CollatzSequence.from(1).size() should be (1)
      }
      "starting from 2" in {
        CollatzSequence.from(2).size() should be (2)
      }
      "starting from 3" in {
        CollatzSequence.from(3).size() should be (8)
      }
      "starting form 15" in {
        CollatzSequence.from(15).size() should be (18)
      }
      "starting from a big number (power of 2)" in {
        CollatzSequence.from(BigInt(2).pow(15)).size() should be (16)
      }
    }
  }
}
