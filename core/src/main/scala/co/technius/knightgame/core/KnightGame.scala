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

  override def render(deltaTime: Float): Unit = {
    updateTime += deltaTime
    if (updateTime >= 1f/60f) {
      updateTime = 0f
      player = Logic(player, deltaTime).input(moveKeys).actions.player
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
