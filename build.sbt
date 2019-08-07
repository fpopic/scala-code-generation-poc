val scala = "0.17.0-RC1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scala,
  scalacOptions ++= "-usejavacp" :: Nil
)

val rootProjectName = "root"
lazy val root = (project in file("."))
  .withId(rootProjectName)
  .aggregate(scalamacros, scalamacrosUsage)
  .settings(
    commonSettings,
    name := rootProjectName
  )
  .enablePlugins(DottyPlugin)


val dottyMacrosProjectName = "dotty-macros"
lazy val scalamacros = (project in file(dottyMacrosProjectName))
  .withId(dottyMacrosProjectName)
  .settings(
    commonSettings,
    name := dottyMacrosProjectName
  )
  .enablePlugins(DottyPlugin)

val dottyMacrosUsageProjectName = "dotty-macros-usage"
lazy val scalamacrosUsage = (project in file(dottyMacrosUsageProjectName))
  .withId(dottyMacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    name := dottyMacrosUsageProjectName
  )
  .enablePlugins(DottyPlugin)
