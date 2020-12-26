package dev.fshp.spsc.producer

import dev.fshp.spsc.exceptions.DummyException
import dev.fshp.spsc.Writable

class Producer private (context: Context, buffer: Writable) extends Runnable {
  override def run(): Unit = {
    while (!context.isDone) {
      if (buffer.isWritable) {
        context.next()
        
        if (context.needThrow) {
          throw new DummyException("Producer")
        }
        
        buffer.write(context.read())
      } else {
        Thread.`yield`()
      }
    }
  }
}

object Producer {
  def apply(buffer: Writable): Context => Producer = c => new Producer(c, buffer)
}
