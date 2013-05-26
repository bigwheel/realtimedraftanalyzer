package com.herokuapp.mtgbase.realtimedraftanalyzer

/**
 * Created with IntelliJ IDEA.
 * User: kbigwheel
 * Date: 13/05/26
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
class PicksOfAPackSpec extends Specification {
  def dummyCardList(cardNumber: Int): List[Card] = {
    cardNumber match {
      case 1 => List(Card("適当なカード名", picked = true))
      case _ => Card("適当なカード名")::dummyCardList(cardNumber - 1)
    }
  }

  "accept valid Pick List" in {
    PicksOfAPack("適当なエキスパンション名", List(
      Pick(1, 1, dummyCardList(15)), Pick(1, 2, dummyCardList(14)),
      Pick(1, 3, dummyCardList(13)), Pick(1, 4, dummyCardList(12)),
      Pick(1, 5, dummyCardList(11)), Pick(1, 6, dummyCardList(10)),
      Pick(1, 7, dummyCardList(9)), Pick(1, 8, dummyCardList(8)),
      Pick(1, 9, dummyCardList(7)), Pick(1, 10, dummyCardList(6)),
      Pick(1, 11, dummyCardList(5)), Pick(1, 12, dummyCardList(4)),
      Pick(1, 13, dummyCardList(3)), Pick(1, 14, dummyCardList(2)),
      Pick(1, 15, dummyCardList(1))
    )) must not be throwAn[Exception]
  }

  "accept valid Pick List" in {
    PicksOfAPack("適当なエキスパンション名", List(
      Pick(1, 1, dummyCardList(15))
    )) must not be throwAn[Exception]
  }

  "not accept" in {
    "if every Pick packNumbers is not same" in {
      PicksOfAPack("適当なエキスパンション名", List(
        Pick(1, 1, dummyCardList(15)),
        Pick(2, 2, dummyCardList(14))
      )) must throwAn[IllegalArgumentException](message = "packNumberの値がすべて同じではありません")
    }

    "if pickNumbers are not contiguous" in {
      PicksOfAPack("適当なエキスパンション名", List(
        Pick(1, 1, dummyCardList(15)), /*Pick(1, 2, dummyCardList(14)),*/
        Pick(1, 3, dummyCardList(13))
      )) must throwAn[IllegalArgumentException](message = "pickNumberが1~Nで連続ではありません")
    }

    "if pickNumbers are not started with 1" in {
      PicksOfAPack("適当なエキスパンション名", List(
        Pick(1, 2, dummyCardList(14)), Pick(1, 3, dummyCardList(13))
      )) must throwAn[IllegalArgumentException](message = "pickNumberが1~Nで連続ではありません")
    }
  }
}
