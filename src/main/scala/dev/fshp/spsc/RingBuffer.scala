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

final class RingBuffer(size: Int) extends Readable with Writable {
  assert(size > 1, "Size should be greater than 1")
  
  private val buffer: Array[Int] = new Array[Int](size)
  private val readIndex: AtomicInteger = new AtomicInteger(0) // next write index
  private val writeIndex: AtomicInteger = new AtomicInteger(0) // next read index

  def capacity: Int = size - 1
  
  def isReadable: Boolean = {
    readIndex.get() != writeIndex.get()
  }

  def isWritable: Boolean = {
    (writeIndex.get() + 1) % size != readIndex.get()
  }

  def read(): Int = {
    val index = readIndex.get()
    val value = buffer(index)
    readIndex.set((index + 1) % size)
    value
  }

  def write(value: Int): Unit = {
    val index = writeIndex.get()
    buffer(index) = value
    writeIndex.set((index + 1) % size)
  }
}
