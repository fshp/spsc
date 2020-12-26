package dev.fshp

import dev.fshp.spsc.consumer.Consumer
import dev.fshp.spsc.producer.Producer
import dev.fshp.spsc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object App {
  def main(args: Array[String]): Unit = {
    val begin = System.currentTimeMillis()
    val k = 1 << 20
    val buffer = new RingBuffer(128)

    val (pContext, cContext) = ContextFactory.create(k, 1000, 500, 300)

    val cSupervisor = new Supervisor("Consumer", cContext, Consumer(buffer))
    val pSupervisor = new Supervisor("Producer", pContext, Producer(buffer))

    // onComplete is not stable, because ExecutionContext threads are daemons
    val result = Await.result(cContext.future, Duration.Inf)

    println(s"Value: $result")
    println(s"Valid result: ${result == (0L until k).sum}")
    println(s"Time: ${System.currentTimeMillis() - begin} ms")
  }
}
