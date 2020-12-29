package dev.fshp.spsc

import dev.fshp.spsc.consumer.Consumer
import dev.fshp.spsc.producer.Producer
import munit.ScalaCheckSuite
import org.scalacheck.Prop._
import org.scalacheck.Gen

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ProduceConsumerSuite extends ScalaCheckSuite {

  override def scalaCheckTestParameters =
    super.scalaCheckTestParameters
      .withMinSuccessfulTests(10000)

  private val bufferSize: Int = 128
  private val kGen: Gen[Int] = Gen.choose(bufferSize + 1, bufferSize << 6)
  private val nGen: Gen[Int] = Gen.choose(100, 2000)
  private val tGen: Gen[Int] = Gen.choose(200, 1000)
  private val pGen: Gen[Int] = Gen.choose(300, 500)

  property("consumer calculate") {
    forAll(kGen, nGen, tGen, pGen) { (k: Int, n: Int, t: Int, p: Int) =>
      val buffer = new RingBuffer(bufferSize)

      val (pContext, cContext) = ContextFactory.create(k, n, t, p)

      val pSupervisor = new Supervisor("Producer", pContext, Producer(buffer))
      val cSupervisor = new Supervisor("Consumer", cContext, Consumer(buffer))

      assertEquals(
        Await.result(cContext.future, Duration.Inf),
        (0L until k).sum
      )
    }
  }
}
