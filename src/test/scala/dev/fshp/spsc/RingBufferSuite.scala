package dev.fshp.spsc

import munit._

class RingBufferSuite extends FunSuite {
  test("validate read / write") {
    val buffer = new RingBuffer(1024)

    for (i <- 0 until buffer.capacity) {
      assertEquals(buffer.isReadable, i > 0)
      assertEquals(buffer.isWritable, i < buffer.capacity)
      buffer.write(i)
    }

    for (i <- 0 until buffer.capacity) {
      assertEquals(buffer.isWritable, i > 0)
      assertEquals(buffer.isReadable, i < buffer.capacity)
      buffer.read()
    }
  }
}
