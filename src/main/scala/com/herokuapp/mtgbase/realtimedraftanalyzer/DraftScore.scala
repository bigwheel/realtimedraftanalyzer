package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source

class DraftScore(path: String) {
  val text = Source.fromFile(path).mkString
  val it = Source.fromFile(path).getLines
  val FirstLine = """Event #: (\d+)""".r
  val FirstLine(eventNumber) = it.next
  println(eventNumber)
}
