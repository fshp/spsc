package dev.fshp.spsc.consumer

import java.util.concurrent.atomic.LongAdder
import scala.concurrent.{Future, Promise}
import scala.util.Random

class Context(maxIters: Int, parkPeriod: Int, exceptionPeriod: Int) {
  private val adder: LongAdder = new LongAdder
  private val promise: Promise[Long] = Promise[Long]
  private var curIter: Int = 0
  private var parkIter: Int = 0
  private var exceptionIter: Int = 0
  private val random = new Random

  private[consumer] def next(): Unit = {
    if (parkPeriod > 1) {
      parkIter = (parkIter + 1) % parkPeriod
    }

    if (exceptionPeriod > 1) {
      exceptionIter = (exceptionIter + 1) % exceptionPeriod
    }
  }

  private[consumer] def write(value: Int): Unit = {
    curIter += 1
    adder.add(value)

    if (isDone) {
      promise.success(adder.sum())
    }
  }

  private[consumer] def needPark: Boolean = parkIter == 0 && parkPeriod > 1

  private[consumer] def parkTime(): Long = random.nextLong(50)

  private[consumer] def needThrow: Boolean = exceptionIter == 0 && exceptionPeriod > 1

  private[consumer] def isDone: Boolean = curIter >= maxIters

  def future: Future[Long] = promise.future
}