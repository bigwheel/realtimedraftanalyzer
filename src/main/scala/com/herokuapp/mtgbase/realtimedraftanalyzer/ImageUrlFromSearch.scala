package com.herokuapp.mtgbase.realtimedraftanalyzer

object ImageUrlFromSearch {
  def apply(cardName: String): Option[String] = {
    Some("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=253536&type=card")
  }
}
