package com.herokuapp.mtgbase.realtimedraftanalyzer

case class Card(name: String, picked: Boolean = false)

case class Pick(cards: List[Card] = List.empty[Card]) {
  // ここに引数をとって、このオブジェクトが不正な状態にならないかのチェックコード（そして例外raiseコード)を書く
  def +(card: Card): Pick = {
    Pick(card::this.cards)
  }
}

case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  // ここでpick中にカードの枚数が増えたりしてないかチェック
}
