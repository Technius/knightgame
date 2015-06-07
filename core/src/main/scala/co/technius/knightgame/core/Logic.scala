package co.technius.knightgame.core

import Action._

case class Logic(player: Player, deltaTime: Float) {

  def actions: Logic = copy(player = player.action match {
    case Stabbing(stabTime) =>
      val t = stabTime + deltaTime
      player copy (action = if (t >= 0.9f) Standing else Stabbing(t))
    case Walking(walkTime) =>
      val (dx, dy) = player.direction.speed
      player copy (
        x = math.max(5f, math.min(95f, player.x + dx)),
        y = math.max(5f, math.min(95f, player.y + dy)),
        action = Walking(walkTime + deltaTime)
      )
    case _ => player
  })

  def input(pressedKeys: Seq[Control]): Logic = copy(player = {
    player.action match {
      case s: Stabbing if pressedKeys contains Controls.Stab =>
        player copy (action = s)
      case _ if pressedKeys contains Controls.Stab =>
        player copy (action = Stabbing(0f))
      case _: Stabbing => player
      case _ =>
        val directions = pressedKeys collect {
          case m: Controls.Move => m.direction
        }
  
        val directionOpt = directions match {
          case Seq(dir) => Some(dir)
          case dirs =>
            val pairs = dirs
              .combinations(2)
              .filterNot(Direction.conflicts.contains(_))
              .flatten
              .toSeq
  
            Direction.compounds collectFirst {
              case ((a, b), d) if pairs.contains(a) && pairs.contains(b) => d
            }
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
