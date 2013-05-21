package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.io.Source

class DraftScore(path: String) {
  val text = Source.fromFile(path).mkString
}
