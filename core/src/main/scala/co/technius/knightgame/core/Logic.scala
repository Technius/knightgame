package co.technius.knightgame.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

import Action._

case class Logic(player: Player, deltaTime: Float) {
  def actions: Logic = copy(player = {
    player.action match {
      case Stabbing(stabTime) =>
        val t = stabTime + deltaTime
        if (t >= 0.9f) {
          player.copy(action = Standing)
        } else {
          player.copy(action = Stabbing(t))
        }
      case Walking(walkTime) =>
        player.copy(action = Walking(walkTime + deltaTime))
      case _ =>
        player.copy(action = Walking(0f))
    }
  })

  def input(moveKeys: List[(Int, Int)]): Logic = copy(player = {
    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
      val time = player.action match {
        case Stabbing(t) => t
        case _ => 0f
      }
      player.copy(action = Stabbing(time))
    } else if (!player.action.isInstanceOf[Stabbing]) {
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
      val time = player.action match {
        case Walking(t) => t
        case _ => 0f
      }

      player.copy(
        x = math.min(math.max(player.x + deltaX, 5), 95),
        y = math.min(math.max(player.y + deltaY, 5), 95),
        direction = direction,
        action = if (standing) Walking(time) else Standing
      )
    } else {
      player
    }
  })
}

object Logic {
  implicit def logicToPlayer(logic: Logic) = logic.player
}
