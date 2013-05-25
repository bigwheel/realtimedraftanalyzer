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
    cardNumber match {
      case 1 => List(Card("適当なカード名", picked = true))
      case _ => Card("適当なカード名")::createDummyCardList(cardNumber - 1)
    }
  }

  "accept valid Pick List" in {
    def Z[A,B](f:(A=>B)=>(A=>B)):A=>B = (x:A) => f(Z(f))(x)
    val l = Z( (f: Int => List[Pick]) => (pickNumber: Int) => pickNumber match {
      case pickNumber if 15 < pickNumber => List.empty[Pick]
      case pickNumber => {
        Pick(1, pickNumber, createDummyCardList(16 - pickNumber))::f(pickNumber + 1)
      }
    })(1)
    PicksOfAPack("適当なエキスパンション名", l) must not be throwAn[Exception]
  }

  "not accept" in {
    "if every Pick packNumbers is not same" in {
      PicksOfAPack("適当なエキスパンション名", List(
        Pick(1, 14, createDummyCardList(2)),
        Pick(2, 15, createDummyCardList(1))
      )) must throwAn[Exception]
    }

    "if pickNumbers are not contiguous" in {
      PicksOfAPack("適当なエキスパンション名", List(
        Pick(1, 1, createDummyCardList(15)),
        Pick(1, 3, createDummyCardList(13))
      )) must throwAn[Exception]
    }
  }
}