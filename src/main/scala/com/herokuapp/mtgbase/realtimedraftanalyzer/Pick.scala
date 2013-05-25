package com.herokuapp.mtgbase.realtimedraftanalyzer

case class Card(name: String, picked: Boolean = false)

case class Pick(packNumber: Int, pickNumber: Int, cards: List[Card]) {
  if (packNumber < 1)
    throw new IllegalArgumentException("packNumberが1未満(" + packNumber + ")です")
  if (pickNumber < 1)
    throw new IllegalArgumentException("pickNumberが1未満(" + pickNumber + ")です")
  if (cards.count(a => a.picked) != 1)
    throw new IllegalArgumentException("pickされたカードが１枚ではありません")
}

case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  // ここでpick中にカードの枚数が増えたりしてないかチェック
}
