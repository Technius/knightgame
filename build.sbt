import com.typesafe.sbt.SbtNativePackager._

import NativePackagerKeys._

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
  .settings(
    name := projectName + "-desktop",
    fork in run := true,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % LibgdxBuild.libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % LibgdxBuild.libgdxVersion classifier "natives-desktop"
    )
  )
  .settings(packageArchetype.java_application: _*)
  .dependsOn(core)

libraryDependencies in ThisBuild ++= Seq(
  "com.badlogicgames.gdx" % "gdx" % LibgdxBuild.libgdxVersion
)

javacOptions in Global ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions in Global += "-target:jvm-1.7"
