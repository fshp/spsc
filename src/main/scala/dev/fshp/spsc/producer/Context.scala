package dev.fshp.spsc.producer

import java.util.concurrent.atomic.LongAdder
import scala.concurrent.{Future, Promise}
import scala.util.Random

class Context(maxIters: Int, exceptionPeriod: Int) {
  private var curIter: Int = 0
  private var exceptionIter: Int = 0

  private[producer] def next(): Unit = {
    if (exceptionPeriod > 1) {
      exceptionIter = (exceptionIter + 1) % exceptionPeriod
    }
  }

  private[producer] def read(): Int = {
    val value = curIter
    curIter += 1
    value
  }

  private[producer] def needThrow: Boolean =
    exceptionIter == 0 && exceptionPeriod > 0

  private[producer] def isDone: Boolean = curIter >= maxIters
}
