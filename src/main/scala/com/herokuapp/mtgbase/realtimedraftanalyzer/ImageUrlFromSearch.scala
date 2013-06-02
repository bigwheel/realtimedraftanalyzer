package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.net.URLEncoder
import scala.util.parsing.json.JSON
import scala.collection.mutable

object ImageUrlFromSearch {
  private[this] val cache = mutable.Map.empty[(String, String), Option[String]]
  def apply(cardName: String, expansionCode: String = ""): Option[String] = {
    cache.getOrElseUpdate((cardName, expansionCode), getUrl(cardName, expansionCode))
  }

  private[this] def getUrl(cardName: String, expansionCode: String = ""): Option[String] = {
    val encordedCardName = URLEncoder.encode(cardName, "UTF-8")
    val expansionName: Option[String] = if (expansionCode == "")
      None
    else {
      try {
        val response = Source.fromURL("http://mtgbase.herokuapp.com" +
          "/expansion_code_to_fullname?code=" + expansionCode)
        Some(response.getLines.mkString)
      } catch {
        case _ => None
      }
    }
    val resultString = Source.fromURL(
      "http://mtgbase.herokuapp.com/search?card_name=" + encordedCardName +
        (if (expansionName == None) "" else "&expansion=" + URLEncoder.encode(expansionName.get, "UTF-8"))).
      getLines.mkString
    val resultJson = JSON.parseFull(resultString)
    val resultList = resultJson.get.asInstanceOf[List[Any]]
    if (resultList.size == 0) {
      None
    } else {
      val map = resultList.head.asInstanceOf[Map[String, Option[Any]]]
      Some(map.get("card_image_url").get.asInstanceOf[String])
    }
  }
}
