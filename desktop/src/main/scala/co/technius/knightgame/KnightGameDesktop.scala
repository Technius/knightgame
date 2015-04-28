package co.technius.knightgame

import co.technius.knightgame.core.KnightGame
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object KnightGameDesktop {
  def main(args: Array[String]) {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "Knight Game"
    new LwjglApplication(new KnightGame, cfg)
  }
}
