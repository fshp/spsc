package dev.fshp.spsc

import dev.fshp.spsc.consumer
import dev.fshp.spsc.producer

object ContextFactory {
  def create(
      k: Int,
      n: Int,
      t: Int,
      p: Int
  ): (producer.Context, consumer.Context) = {
    (new producer.Context(k, n), new consumer.Context(k, t, p))
  }
}
