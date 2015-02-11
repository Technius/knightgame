val projectName = """knightgame"""

name := projectName

version := "1.0.0"

scalaVersion := "2.11.5"

lazy val root = Project("root", file("."))
  .aggregate(android, desktop)

lazy val core = Project("core", file("core"))
  .settings(name := projectName + "-core")

lazy val android = Project("android", file("android"))
  .settings(name := projectName + "-android")
  .dependsOn(core)

lazy val desktop = Project("desktop", file("desktop"))
  .settings(name := projectName + "-desktop")
  .dependsOn(core)

libraryDependencies in ThisBuild ++= Seq(
  "com.badlogicgames.gdx" % "gdx" % LibgdxBuild.libgdxVersion
)

javacOptions in Global ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions in Global += "-target:jvm-1.7"
