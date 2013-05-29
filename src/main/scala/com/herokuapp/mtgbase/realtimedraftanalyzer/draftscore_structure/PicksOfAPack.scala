package com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure

case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  if (picks.size != 0 && picks.groupBy(_.packNumber).size != 1)
    throw new IllegalArgumentException("packNumberの値がすべて同じではありません")
  if (picks.map(_.pickNumber) != Range(1, picks.size + 1).toList)
    throw new IllegalArgumentException("pickNumberが1~Nで連続ではありません")
}
