package dev.fshp.spsc.consumer

import dev.fshp.spsc.exceptions.DummyException
import dev.fshp.spsc.Readable

import java.util.concurrent.locks.LockSupport

class Consumer private (context: Context, buffer: Readable) extends Runnable {
  override def run(): Unit = {
    while (!context.isDone) {
      if (buffer.isReadable) {
        context.next()

        if (context.needPark) {
          LockSupport.parkNanos(context.parkTime())
        }

        if (context.needThrow) {
          throw new DummyException("Consumer")
        }
        
        context.write(buffer.read())
        
      } else {
        Thread.`yield`()
      }
    }
  }
}

object Consumer {
  def apply(buffer: Readable): Context => Consumer = c => new Consumer(c, buffer)
}
