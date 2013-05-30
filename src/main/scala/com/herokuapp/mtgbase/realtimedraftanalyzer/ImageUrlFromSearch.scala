package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source
import java.net.URLEncoder
import scala.util.parsing.json.JSON

object ImageUrlFromSearch {
  def apply(cardName: String): Option[String] = {
    val encordedCardName = URLEncoder.encode(cardName, "UTF-8")
    val resultString = Source.fromURL(
      "http://mtgbase.herokuapp.com/search?card_name=" + encordedCardName).
      getLines.mkString
    val resultJson = JSON.parseFull(resultString)
    val map = resultJson.get.asInstanceOf[List[Any]].head.
      asInstanceOf[Map[String, Option[Any]]]
    Some(map.get("card_image_url").get.asInstanceOf[String])
  }
}
