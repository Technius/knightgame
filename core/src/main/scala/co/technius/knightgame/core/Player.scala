package co.technius.knightgame.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d._

case class Player(
    x: Int = 50,
    y: Int = 50,
    direction: Int = Direction.Right,
    action: Action = Action.Standing)

class PlayerRenderer {
  val knightTex = new Texture(Gdx.files.internal("knight.png"))
  val (knightRow, knightCol) = (21, 13)
  val knightSheet = TextureRegion.split(knightTex, knightTex.getWidth/knightCol, knightTex.getHeight/knightRow)
  val walkFrames = knightSheet.slice(8, 12)
  val standFrames = walkFrames.map(_(0))
  val walkAnims = walkFrames.map(_.slice(1, 9)).map(new Animation(0.125f, _:_*))
  val stabAnims = knightSheet.slice(4, 8).map(l => new Animation(0.1f, l.slice(1, 8):_*))
  val knightSprite = new Sprite()

  def render(player: Player, batch: SpriteBatch) {
    import Action._
    val frame = player.action match {
      case Walking(time) =>
        walkAnims(player.direction).getKeyFrame(time, true)
      case Stabbing(time) =>
        stabAnims(player.direction).getKeyFrame(time, false)
      case Standing =>
        standFrames(player.direction)
    }
    knightSprite.setRegion(frame)
    knightSprite.setPosition(
      player.x * Gdx.graphics.getWidth  / 100 - knightSprite.getWidth  / 2,
      player.y * Gdx.graphics.getHeight / 100 - knightSprite.getHeight / 2
    )
    knightSprite.draw(batch)
  }
}

sealed trait Action
object Action {
  case object Standing extends Action
  case class Walking(time: Float) extends Action
  case class Stabbing(time: Float) extends Action
}

object Direction {
  val Up = 0
  val Left = 1
  val Down = 2
  val Right = 3
}
