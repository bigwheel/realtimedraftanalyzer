package com.herokuapp.mtgbase.realtimedraftanalyzer

case class Card(name: String, picked: Boolean = false)

case class Pick(packNumber: Int, pickNumber: Int, cards: List[Card]) {
  if (packNumber < 1)
    throw new IllegalArgumentException("packNumberが1未満(" + packNumber + ")です")
  if (pickNumber + cards.size != 16)
    throw new IllegalArgumentException("pickNumberとカードの枚数が矛盾します " +
    "pickNumber:" + pickNumber + " カードの枚数:" + cards.size)
  if (cards.count(a => a.picked) != 1)
    throw new IllegalArgumentException("pickされたカードが１枚ではありません")
}

case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  if (picks.groupBy(_.packNumber).size != 1)
    throw new IllegalArgumentException("packNumberの値がすべて同じではありません")
}
