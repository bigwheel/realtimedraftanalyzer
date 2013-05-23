package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.text.SimpleDateFormat
import java.util.Locale

class DraftScore(path: String) {
  private[this] def throwIAE(line: String = "") = {
    throw new IllegalArgumentException("入力されたテキストファイルのフォーマットが不正です:" + line)
  }

  private[this] val it = Source.fromFile(path).getLines // itってミュータブルなんやね

  val eventNumber = """Event #: (\d+)""".r.
      findFirstMatchIn(it.next).get.group(1).toInt
  val date = new SimpleDateFormat(
    "'Time:    'MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH).parse(it.next)


  val players = if (it.next == "Players: ") {
    def parsePlayers(it: Iterator[String]): Players = {
      val recording_player = "^--> (.+)$".r
      val other_player = "^    (.+)$".r
      it.next match {
        case recording_player(name) => Players(name, parsePlayers(it).others)
        case other_player(name) => parsePlayers(it).appendOtherPlayer(name)
        case " " => Players()
        case other => throwIAE(other)
      }
    }
    parsePlayers(it)
  } else
    throwIAE()

}
