val projectName = """knightgame"""

name := projectName

version := "1.0.0"

scalaVersion in Global := "2.11.6"

lazy val root = Project("root", file("."))
  .aggregate(android, desktop)

lazy val core = Project("core", file("core"))
  .settings(
    name := projectName + "-core",
    libraryDependencies += libGdx
  )

lazy val android = Project("android", file("android"))
  .settings(name := projectName + "-android")
  .dependsOn(core)

lazy val desktop = Project("desktop", file("desktop"))
  .settings(
    name := projectName + "-desktop",
    watchSources <++= sources in (core, Compile)
  )
  .dependsOn(core)
  .enablePlugins(LibGdxDesktop)

javacOptions in Global ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions in Global ++= Seq(
  "-target:jvm-1.7",
  "-unchecked",
  "-feature",
  "-deprecation",
  "-Xlint",
  "-Xfatal-warnings"
)
