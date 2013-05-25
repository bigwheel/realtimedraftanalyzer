package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification

class PickSpec extends Specification {
  "accept a valid cardlist" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority", picked = true),
      Card("Swamp")
    )) must not be throwAn[Exception]
  }

  "not accept more than one picked flag" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage", picked = true),
      Card("Righteous Authority", picked = true),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }

  "not accept picked flag nothing" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority"),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }

  "not accept less than 1 as packNumber" in {
    Pick(0, 14, List(
      Card("Drainpipe Vermin"),
      Card("Righteous Authority", picked = true)
    )) must throwAn[Exception]
  }

  "have just card number depend on pickNumber" in {
    "accept case" in {
      Pick(1, 14, List(
        Card("Drainpipe Vermin"),
        Card("Righteous Authority", picked = true)
      )) must not be throwAn[Exception]
    }

    "dont accept case" in {
      Pick(1, 14, List(
        Card("Righteous Authority", picked = true)
      )) must throwAn[Exception]
    }
  }
}

class PicksOfAPackSpec extends Specification {
  def createDummyCardList(cardNumber: Int): List[Card] = {
    List(Card("適当なカード名", picked = true))
  }

  "accept valid Pick List" in {
    PicksOfAPack("適当なエキスパンション名", List(
      Pick(1, 1, createDummyCardList(15))
    )) must not be throwAn[Exception]
  }
}