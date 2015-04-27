package co.technius.knightgame.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

import Action._

case class Logic(player: Player, deltaTime: Float) {
  def actions: Logic = copy(player = player.action match {
    case Stabbing(stabTime) =>
      val t = stabTime + deltaTime
      player copy (action = if (t >= 0.9f) Standing else Stabbing(t))
    case Walking(walkTime) =>
      val (dx, dy) = (player.direction.speed._1, player.direction.speed._2)
      player copy (
        x = math.max(5, math.min(95, player.x + dx)),
        y = math.max(5, math.min(95, player.y + dy)),
        action = Walking(walkTime + deltaTime)
      )
    case _ => player
  })

  def input(moveKeys: List[(Int, Direction)]): Logic = copy(player = {
    player.action match {
      case s: Stabbing if Gdx.input.isKeyPressed(Keys.SPACE) =>
        player copy (action = s)
      case _ if Gdx.input.isKeyPressed(Keys.SPACE) =>
        player copy (action = Stabbing(0f))
      case _: Stabbing => player
      case _ =>
        val directions = moveKeys
          .filter { case (key, _) => Gdx.input.isKeyPressed(key) }
          .map(_._2)
  
        val directionOpt = directions match {
          case Seq(dir) => Some(dir)
          case dirs =>
            val pairs = dirs
              .combinations(2)
              .filterNot(Direction.conflicts.contains(_))
              .flatten
              .toSeq
  
            Direction.compounds find { case ((a, b), dir) =>
              pairs.contains(a) && pairs.contains(b)
            } map (_._2)
        }
  
        val walkTime = player.action match {
          case Walking(t) => t
          case _ => 0f
        }
  
        player.copy(
          direction = directionOpt getOrElse player.direction,
          action = directionOpt map (_ => Walking(walkTime)) getOrElse Standing
        )
    }
  })
}
