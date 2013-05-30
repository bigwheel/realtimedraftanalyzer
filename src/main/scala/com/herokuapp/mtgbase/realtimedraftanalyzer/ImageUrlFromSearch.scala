package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.net.URLEncoder
import scala.util.parsing.json.JSON

object ImageUrlFromSearch {
  def apply(cardName: String/*, expansionAcronym: String*/): Option[String] = {
    val encordedCardName = URLEncoder.encode(cardName, "UTF-8")
    //val encordedExpansionName = URLEncoder.encode("Return to Ravnica", "UTF-8")
    val resultString = Source.fromURL(
      "http://mtgbase.herokuapp.com/search?card_name=" + encordedCardName/* +
        "&expansion=" + encordedExpansionName*/).
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
