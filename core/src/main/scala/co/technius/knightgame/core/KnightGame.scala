package co.technius.knightgame.core

import com.badlogic.gdx.{Gdx, Game, Screen}
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{SpriteBatch}
import com.badlogic.gdx.utils.viewport.FitViewport

import scala.math

class KnightGame extends Game {
  override def create() = {
    setScreen(new MainScreen) 
  }
}

class MainScreen extends Screen {
  val camera = new OrthographicCamera
  val viewport = new FitViewport(Gdx.graphics.getWidth, Gdx.graphics.getHeight, camera)
  val batch = new SpriteBatch
  val playerRenderer = new PlayerRenderer
  val moveKeys = List(
    Keys.W -> Direction.Up,
    Keys.S -> Direction.Down,
    Keys.A -> Direction.Left,
    Keys.D -> Direction.Right
  )

  var player = Player()
  var updateTime = 0f
  var walking = false

  resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)

  override def render(deltaTime: Float) {
    updateTime += deltaTime
    if (updateTime >= 1f/60f) {
      updateTime = 0f

      val actionState: Player = player.action match {
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

      val inputState = if (Gdx.input.isKeyPressed(Keys.SPACE)) {
        actionState.copy(action = Action.Stabbing)
      } else {
        val (deltaX, deltaY, direction) = moveKeys
          .filter(kp => Gdx.input.isKeyPressed(kp._1))
          .map { kp =>
            val dir = kp._2
            if (dir % 2 == 0) {
              (0, 1 - dir, dir)
            } else {
              (dir - 2, 0, dir)
            }
          }
          .foldLeft((0, 0, actionState.direction)) {
            case ((x, y, _), (dx, dy, dir)) => (x + dx, y + dy, dir)
          }
        val standing = deltaX != 0 || deltaY != 0
        actionState.copy(
          x = math.min(math.max(actionState.x + deltaX, 5), 95),
          y = math.min(math.max(actionState.y + deltaY, 5), 95),
          direction = direction,
          action = if (standing) Action.Walking else actionState.action
        )
      }

      player = inputState
    }
    
    Gdx.graphics.getGL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    playerRenderer.render(player, batch)
    batch.end()
  }

  override def resize(width: Int, height: Int) {
    val ratio = width/height
    camera.setToOrtho(false, width, height)
    playerRenderer.knightSprite.setSize(width/10, height/10)
    camera.update()
  }

  override def dispose() {
    batch.dispose()
  }

  override def hide = Unit
  override def show = Unit
  override def pause = Unit
  override def resume = Unit
}
