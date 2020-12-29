package dev.fshp.spsc

import munit._
import org.scalacheck.Gen
import org.scalacheck.Prop._

class RingBufferSuite extends ScalaCheckSuite {

  private val sizeGen: Gen[Int] = Gen.choose(2, 1024)
  
  property("validate read / write") {
    forAll (sizeGen) { size =>
      val buffer = new RingBuffer(size)

      for (i <- 0 until buffer.size - 1) {
        assertEquals(buffer.isReadable, i > 0)
        assertEquals(buffer.isWritable, i < buffer.size - 1)
        buffer.write(i)
      }

      for (i <- 0 until buffer.size - 1) {
        assertEquals(buffer.isWritable, i > 0)
        assertEquals(buffer.isReadable, i < buffer.size - 1)
        buffer.read()
      }
    }
  }
}
