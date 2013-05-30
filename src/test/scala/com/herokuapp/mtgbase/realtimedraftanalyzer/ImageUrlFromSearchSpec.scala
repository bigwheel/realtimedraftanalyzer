package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification
import org.specs2.specification.AllExpectations

class ImageUrlFromSearchSpec extends Specification with AllExpectations {
  "ImageUrlFromSearch" should {
    def url(id: String) =
      "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + id + "&type=card"
    val validPairs = List(("Horncaller's Chant", url("253536")),
      ("Goblin Electromancer", url("253548")),
      ("Nivmagus Elemental", url("290526")),
      ("Æther Adept", url("205020"))
    )

    "return valid Image Url When valid name given" in {
      for (validPair <- validPairs)
        ImageUrlFromSearch(validPair._1) must_== Some(validPair._2)
    }

    "return None When invalid name given" in {
      ImageUrlFromSearch("MacGuffin") must_== None
    }
  }
}
