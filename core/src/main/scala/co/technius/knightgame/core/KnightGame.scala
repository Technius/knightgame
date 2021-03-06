package co.technius.knightgame.core

import com.badlogic.gdx.{ Gdx, Game, Screen }
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.{ GL20, OrthographicCamera, Texture }
import com.badlogic.gdx.graphics.g2d.{ BitmapFont, SpriteBatch }
import com.badlogic.gdx.utils.viewport.FitViewport

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

  camera.setToOrtho(false, 100, 100)

  var player = Player()
  var updateTime = 0f
  var walking = false

  resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)

  override def render(deltaTime: Float): Unit = {
    Gdx.graphics.getGL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    playerRenderer.render(player, batch)
    batch.end()

    updateTime += deltaTime
    while (updateTime >= 1f / 60f) {
      updateTime -= 1f / 60f
      val pressed = controls collect {
        case (key, control) if Gdx.input.isKeyPressed(key) => control
      }
      player = Logic(player, 1f / 60f).input(pressed).actions.player
    }
  }

  override def resize(width: Int, height: Int): Unit = {
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
