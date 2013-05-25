package com.herokuapp.mtgbase.realtimedraftanalyzer

case class Card(name: String, picked: Boolean = false)

case class Pick(cards: List[Card]) {
  if (cards.count(a => a.picked) != 1)
    throw new IllegalArgumentException("pickされたカードが１枚ではありません")
}

case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  // ここでpick中にカードの枚数が増えたりしてないかチェック
}
