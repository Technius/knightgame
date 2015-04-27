package co.technius.knightgame.core

sealed trait Control
object Controls {
  case object Stab extends Control
  case class Move(direction: Direction) extends Control
}
