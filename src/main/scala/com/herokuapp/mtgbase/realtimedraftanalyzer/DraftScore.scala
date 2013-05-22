package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.text.SimpleDateFormat
import java.util.Locale

class DraftScore(path: String) {
  private[this] val it = Source.fromFile(path).getLines // itってミュータブルなんやね
  private[this] val eventNumber = """Event #: (\d+)""".r.
      findFirstMatchIn(it.next).get.group(1).toInt
  private[this] val date = new SimpleDateFormat(
    "'Time:    'MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH).parse(it.next)
  println(date)
}
