package co.technius.knightgame.core

sealed abstract class Direction(val id: Int, val speed: (Float, Float))
object Direction {
  val conflicts = Seq(Seq(Up, Down), Seq(Left, Right))
  val compounds = Seq(
    (Up,   Left ) -> UpLeft,
    (Up,   Right) -> UpRight,
    (Down, Left ) -> DownLeft,
    (Down, Right) -> DownRight
  )

  lazy val diagDist = math.sqrt(2).toFloat/2f

  case object Up    extends Direction(0, (0f,  1f))
  case object Left  extends Direction(1, (-1f, 0f))
  case object Down  extends Direction(2, (0f, -1f))
  case object Right extends Direction(3, (1f,  0f))

  case object UpRight   extends Direction(3, (diagDist,   diagDist))
  case object UpLeft    extends Direction(1, (-diagDist,  diagDist))
  case object DownRight extends Direction(3, (diagDist,  -diagDist))
  case object DownLeft  extends Direction(1, (-diagDist, -diagDist))
}
