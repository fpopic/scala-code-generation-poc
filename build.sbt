val scala = "2.13.0"
val scalaReflect = "org.scala-lang" % "scala-reflect" % scala

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scala,
  scalacOptions ++= "-usejavacp" :: "-Ymacro-annotations" :: Nil
)

val rootProjectName = "root"
lazy val root = (project in file("."))
  .withId(rootProjectName)
  .settings(
    commonSettings,
    name := rootProjectName
  )

val scalamacrosProjectName = "scala-macros"
lazy val scalamacros = (project in file(scalamacrosProjectName))
  .withId(scalamacrosProjectName)
  .settings(
    commonSettings,
    name := scalamacrosProjectName,
    libraryDependencies ++= scalaReflect :: Nil
  )

val scalamacrosUsageProjectName = "scala-macros-usage"
lazy val scalamacrosUsage = (project in file(scalamacrosUsageProjectName))
  .withId(scalamacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    name := scalamacrosUsageProjectName
  )
