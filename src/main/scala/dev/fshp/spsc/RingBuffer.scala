package dev.fshp.spsc

import java.util.concurrent.atomic.AtomicInteger

trait Readable {
  def isReadable: Boolean
  def read(): Int
}

trait Writable {
  def isWritable: Boolean
  def write(value: Int): Unit
}

final class RingBuffer(val size: Int) extends Readable with Writable {
  assert(size > 1, "Size should be greater than 1")

  private val buffer: Array[Int] = new Array[Int](size)
  @volatile private var head: Int = 0 // next write index
  @volatile private var tail: Int = 0 // next read index

  def isReadable: Boolean = {
    head != tail
  }

  def isWritable: Boolean = {
    (tail + 1) % size != head
  }

  def read(): Int = {
    val value = buffer(head)
    head = (head + 1) % size
    value
  }

  def write(value: Int): Unit = {
    buffer(tail) = value
    tail = (tail + 1) % size
  }
}
