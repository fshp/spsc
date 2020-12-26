package dev.fshp.spsc.exceptions

import scala.util.control.NoStackTrace

final class DummyException(message: String) extends Exception(message) with NoStackTrace
