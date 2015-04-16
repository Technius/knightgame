package co.technius.knightgame.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

case class Logic(player: Player, deltaTime: Float) {
  def actions: Logic = copy(player = {
    player.action match {
      case Action.Stabbing =>
        val t = player.stabTime + deltaTime
        if (t >= 0.9f) {
          player.copy(stabTime = 0f, action = Action.Standing)
        } else {
          player.copy(stabTime = t)
        }
      case Action.Walking =>
        player.copy(walkTime = player.walkTime + deltaTime)
      case _ =>
        player.copy(walkTime = 0f)
    }
  })

  def input(moveKeys: List[(Int, Int)]): Logic = copy(player = {
    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
      player.copy(action = Action.Stabbing)
    } else if (player.action != Action.Stabbing) {
      val (deltaX, deltaY, direction) = moveKeys
        .filter { case (key, _) => Gdx.input.isKeyPressed(key) }
        .map { case (_, dir) =>
          if (dir % 2 == 0) {
            (0, 1 - dir, dir)
          } else {
            (dir - 2, 0, dir)
          }
        }
        .foldLeft((0, 0, player.direction)) {
          case ((x, y, _), (dx, dy, dir)) => (x + dx, y + dy, dir)
        }
      val standing = deltaX != 0 || deltaY != 0
      player.copy(
        x = math.min(math.max(player.x + deltaX, 5), 95),
        y = math.min(math.max(player.y + deltaY, 5), 95),
        direction = direction,
        action = if (standing) Action.Walking else Action.Standing
      )
    } else {
      player
    }
  })
}

object Logic {
  implicit def logicToPlayer(logic: Logic) = logic.player
}
