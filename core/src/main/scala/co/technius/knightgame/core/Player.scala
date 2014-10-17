package co.technius.knightgame.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Animation, Sprite, SpriteBatch, TextureRegion}

class Player {
  private[this] var (_x, _y) = (50, 50)
  private[this] var _direction = Direction.Right
  private[this] var _lastDirection = _direction
  private[this] var _action = Action.Standing
  private[this] var _lastAction = _action

  var stabTime = 0f
  var walkTime = 0f

  def update(deltaTime: Float) {
    if (_action == Action.Stabbing) stabTime += deltaTime
    else stabTime = 0f

    if (_action == Action.Walking) walkTime += deltaTime
    else walkTime = 0f
  }

  def x = _x
  def x_=(x: Int) = _x = Math.min(Math.max(x, 5), 95)
  def y = _y
  def y_=(y: Int) = _y = Math.min(Math.max(y, 5), 95)
  def direction = _direction
  def direction_=(dir: Int) = {
    _lastDirection = _direction
    _direction = dir
  }
  def lastDirection = _lastDirection
  def action = _action
  def action_=(a: Action.Value) = {
    _lastAction = action
    _action = a
  }
  def lastAction = _lastAction
}

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
    val frame = player.action match {
      case Action.Walking => walkAnims(player.direction).getKeyFrame(player.walkTime, true)
      case Action.Stabbing => stabAnims(player.direction).getKeyFrame(player.stabTime, false)
      case Action.Standing => standFrames(player.direction)
    }
    knightSprite.setRegion(frame)
    knightSprite.setPosition(player.x*Gdx.graphics.getWidth/100 - knightSprite.getWidth/2, player.y*Gdx.graphics.getHeight/100 - knightSprite.getHeight/2)
    knightSprite.draw(batch)
  }
}

object Action extends Enumeration {
  val Stabbing = Value
  val Standing = Value
  val Walking = Value
}

object Direction {
  val Up = 0
  val Left = 1
  val Down = 2
  val Right = 3
}
