package pl.szymonmatejczyk.collatz

/**
 * Created by szymonmatejczyk on 22.11.14.
 */
class CollatzSequence(val iterator: Iterator[BigInt]) {
  def size(): BigInt = {
    var size = BigInt(0)
    iterator.find {n  =>
      size += 1
      n.intValue() == 1
    }
    size
  }
}

object CollatzSequence {
  def from(n: BigInt): CollatzSequence = {
    def step(n : BigInt) = {
      if (n.intValue() == 1)
        BigInt(1)
      else
        if ((n % 2).intValue() == 0)
          n / 2
        else
          3 * n + 1
    }
    def stream(n: BigInt): Stream[BigInt] = n #:: stream(step(n))
    new CollatzSequence(stream(n).iterator)
  }
}
