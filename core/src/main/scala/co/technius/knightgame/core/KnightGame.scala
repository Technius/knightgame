package co.technius.knightgame.core

import com.badlogic.gdx.{Gdx, Game, Screen}
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{SpriteBatch}
import com.badlogic.gdx.utils.viewport.FitViewport

class KnightGame extends Game {
  override def create() = {
    setScreen(new MainScreen) 
  }
}

class MainScreen extends Screen {
  val camera = new OrthographicCamera
  val viewport = new FitViewport(Gdx.graphics.getWidth, Gdx.graphics.getHeight, camera)
  val batch = new SpriteBatch
  val (player, playerRenderer) = (new Player, new PlayerRenderer)
  val moveKeys = List(
    Keys.W -> Direction.Up,
    Keys.S -> Direction.Down,
    Keys.A -> Direction.Left,
    Keys.D -> Direction.Right
  )

  var updateTime = 0f
  var walking = false

  resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)

  override def render(deltaTime: Float) {
    updateTime += deltaTime
    if (updateTime >= 1f/60f) {
      updateTime = 0f

      player.update(deltaTime)
      
      if (player.action == Action.Stabbing) {
        if (player.stabTime >= 0.9f) {
          player.stabTime = 0
          player.action = Action.Standing
        }
      } else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
        player.action = Action.Stabbing
      } else {
        player.action = Action.Walking
        moveKeys.filter { case (key, _) => Gdx.input.isKeyPressed(key) } match {
          case List() =>
            player.action = Action.Standing
          case l => l.foreach {
            case (key, dir) =>
              player.direction = dir
              if (dir % 2 == 0) player.y += 1 - dir
              else player.x += dir - 2
            }
        }
      }

      Gdx.graphics.getGL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
      batch.setProjectionMatrix(camera.combined)
      batch.begin()
      playerRenderer.render(player, batch)
      batch.end()
    }
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
