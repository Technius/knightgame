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
  val viewport = new FitViewport(
    Gdx.graphics.getWidth, Gdx.graphics.getHeight, camera)
  val batch = new SpriteBatch
  val playerRenderer = new PlayerRenderer
  val controls = List(
    Keys.W -> Controls.Move(Direction.Up),
    Keys.S -> Controls.Move(Direction.Down),
    Keys.A -> Controls.Move(Direction.Left),
    Keys.D -> Controls.Move(Direction.Right),
    Keys.SPACE -> Controls.Stab
  )

  var player = Player()
  var updateTime = 0f
  var walking = false

  resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)

  override def render(deltaTime: Float): Unit = {
    updateTime += deltaTime
    if (updateTime >= 1f/60f) {
      updateTime = 0f
      val pressed = controls filter { t =>
        Gdx.input.isKeyPressed(t._1)
      } map (_._2)
      player = Logic(player, deltaTime).input(pressed).actions.player
    }
    
    Gdx.graphics.getGL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    playerRenderer.render(player, batch)
    batch.end()
  }

  override def resize(width: Int, height: Int): Unit = {
    val ratio = width/height
    camera.setToOrtho(false, width, height)
    playerRenderer.knightSprite.setSize(width/10, height/10)
    camera.update()
  }

  override def dispose(): Unit = {
    batch.dispose()
  }

  override def hide = Unit
  override def show = Unit
  override def pause = Unit
  override def resume = Unit
}
