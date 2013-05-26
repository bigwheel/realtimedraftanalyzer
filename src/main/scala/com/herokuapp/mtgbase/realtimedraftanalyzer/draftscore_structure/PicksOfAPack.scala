package com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure

/**
 * Created with IntelliJ IDEA.
 * User: kbigwheel
 * Date: 13/05/26
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
case class PicksOfAPack(expansion: String, picks: List[Pick]) {
  if (picks.groupBy(_.packNumber).size != 1)
    throw new IllegalArgumentException("packNumberの値がすべて同じではありません")
  if (picks.map(_.pickNumber) != Range(1, picks.size + 1).toList)
    throw new IllegalArgumentException("pickNumberが1~Nで連続ではありません")
}
