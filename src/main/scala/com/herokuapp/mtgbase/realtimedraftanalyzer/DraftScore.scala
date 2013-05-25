package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.text.SimpleDateFormat
import java.util.Locale
import scala.util.matching.Regex.Match

class DraftScore(path: String) {
  private[this] def throwIAE(line: String = "") = {
    throw new IllegalArgumentException("入力されたテキストファイルのフォーマットが不正です:" + line)
  }

  private[this] def getParam(regex: String, destination: String): Match = {
    regex.r.findFirstMatchIn(destination) match {
      case Some(_match) => _match
      case None => throwIAE(destination)
    }
  }

  private[this] def getFirstParam(regex: String, destination: String): String = {
    getParam(regex, destination).group(1)
  }

  private[this] val it = Source.fromFile(path).getLines // itってミュータブルなんやね

  val eventNumber = getFirstParam("""Event #: (\d+)""", it.next).toInt
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

  private[this] val boosterPackName = getFirstParam("^------ (.+) ------$", it.next)
  it.next // 空行を読み捨て
  private[this] def parseAPick(it: Iterator[String]): Pick = {
    val g = getParam("""^Pack (\d+) pick (\d+):""", it.next)
    val (packNumber, pickNumber) = (g.group(1).toInt, g.group(2).toInt)
    //if (packNumber != 1)
    //  throwIAE("pack番号が矛盾します。期待:1 実際:" + packNumber)
    //if (pickNumber != 1)
    //  throwIAE("pick番号が矛盾します。期待:1 実際:" + pickNumber)

    def parseCards(it: Iterator[String]): List[Card] = {
      val picked_card = "^--> (.+)$".r
      val other_card = "^    (.+)$".r
      it.next match {
        case picked_card(card_name) => Card(card_name, true)::parseCards(it)
        case other_card(card_name) => Card(card_name, false)::parseCards(it)
        case " " => List.empty[Card]
        case other => throwIAE(other)
      }
    }
    Pick(packNumber, pickNumber, parseCards(it))
  }

  private[this] val picklist = (1 to 15).map(_ => parseAPick(it)).toList
  val firstPack = PicksOfAPack(boosterPackName, picklist)
  println(firstPack)
}
