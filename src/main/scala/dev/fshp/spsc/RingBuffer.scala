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
  @volatile private var readIndex: Int = 0 // next write index
  @volatile private var writeIndex: Int = 0 // next read index

  def isReadable: Boolean = {
    readIndex != writeIndex
  }

  def isWritable: Boolean = {
    (writeIndex + 1) % size != readIndex
  }

  def read(): Int = {
    val value = buffer(readIndex)
    readIndex = (readIndex + 1) % size
    value
  }

  def write(value: Int): Unit = {
    buffer(writeIndex) = value
    writeIndex = (writeIndex + 1) % size
  }
}
