lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-macros",
    version := "0.1.0",
    scalaVersion := "0.23.0-RC1",
  )
