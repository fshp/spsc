package dev.fshp.spsc

import java.util.concurrent.atomic.AtomicBoolean

final class Supervisor[T](name: String, context: T, task: T => Runnable)
    extends Thread.UncaughtExceptionHandler { self =>
  private val isDone: AtomicBoolean = new AtomicBoolean(false)
  private var workersCounter: Int = 0

  private val getName: String = s"""Supervisor "$name""""

  override def uncaughtException(t: Thread, e: Throwable): Unit = {
    isDone.set(false)
  }

  locally {
    val runnable = new Runnable {
      override def run(): Unit = {
        while (!isDone.getAndSet(true)) {
          val worker =
            new Thread(task(context), s"$getName: Worker $workersCounter")
          workersCounter += 1

          worker.setUncaughtExceptionHandler(self)
          worker.start()
          worker.join()
        }
      }
    }

    new Thread(runnable, getName).start()
  }
}
