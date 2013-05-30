package com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure

import scala.io.Source
import java.text.SimpleDateFormat
import java.util.Locale
import scala.util.matching.Regex.Match
import com.herokuapp.mtgbase.realtimedraftanalyzer.Players

/**
 * ドラフトピック譜を表現するクラスだが、リアルタイムでピック譜を扱いたい関係上
 * 記述中のピック譜も許容しなければいけない
 */
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

  private[this] val lines = Source.fromFile(path).getLines.toList
  private[this] def inner(lines: List[String]): (List[String], List[String]) = {
    lines match {
      case Nil => (List.empty[String], List.empty[String])
      case " "::xs => (List.empty[String], xs)
      case x::xs => {
        val (block, restList) = inner(xs)
        (x::block, restList)
      }
    }
  }
  private[this] def outer(lines: List[String]): List[List[String]] = {
    lines match {
      case Nil => List.empty[List[String]]
      case _ => {
        val (block, restList) = inner(lines)
        block::outer(restList)
      }
    }
  }
  private[this] val blocks = outer(lines)

  private[this] val summary = blocks(0)
  val eventNumber = getFirstParam("""Event #: (\d+)""", summary(0)).toInt
  val date = new SimpleDateFormat(
    "'Time:    'MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH).parse(summary(1))

  val players = if (summary(2) == "Players: ") {
    def parsePlayers(playerList: List[String]): Players = {
      if (playerList == Nil)
        Players()
      else {
        val recording_player = "^--> (.+)$".r
        val other_player = "^    (.+)$".r
        playerList.head match {
          case recording_player(name) => Players(name, parsePlayers(playerList.tail).others)
          case other_player(name) => parsePlayers(playerList.tail).appendOtherPlayer(name)
          case other => throwIAE(other)
        }
      }
    }
    parsePlayers(summary.drop(3))
  } else
    throwIAE()

  private[this] val blockI = blocks.drop(1).iterator

  private[this] def parseAPick(it: Iterator[String]): Pick = {
    val g = getParam("""^Pack (\d+) pick (\d+):""", it.next)
    val (packNumber, pickNumber) = (g.group(1).toInt, g.group(2).toInt)

    def parseCards(it: Iterator[String]): List[Card] = {
      if (it.hasNext) {
        val picked_card = "^--> (.+?)(?: \\(FOIL\\))?$".r
        val other_card = "^    (.+?)(?: \\(FOIL\\))?$".r
        it.next match {
          case picked_card(card_name) => Card(card_name, true)::parseCards(it)
          case other_card(card_name) => Card(card_name, false)::parseCards(it)
          case other => throwIAE(other)
        }
      } else
        List.empty[Card]
    }
    Pick(packNumber, pickNumber, parseCards(it))
  }

  private[this] def parsePicks(i: Iterator[List[String]], maxRestCount: Int): List[Pick] = {
    if (maxRestCount == 0 || !i.hasNext)
      List.empty[Pick]
    else
      parseAPick(i.next.iterator) :: parsePicks(i, maxRestCount - 1)
  }

  var packs = List.empty[PicksOfAPack]
  while (blockI.hasNext) {
    val packName = blockI.next
    val boosterPackName = getFirstParam("^------ (.+) ------$", packName(0))
    packs = PicksOfAPack(boosterPackName, parsePicks(blockI, 15)) :: packs
  }
  packs = packs.reverse
}
