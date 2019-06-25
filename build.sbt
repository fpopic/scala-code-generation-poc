val scala = "2.13.0-M3"
val scalaReflect = "org.scala-lang" % "scala-reflect" % scala
val scalaMacrosParadise = "org.scalamacros" %% "paradise" % "2.1.1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scala,
  //  scalacOptions ++= "-usejavacp" :: "-Ymacro-annotations" :: Nil
  scalacOptions ++= "-Dscala.usejavacp=true" :: "-language:experimental.macros" :: Nil
)

val rootProjectName = "root"
lazy val root = (project in file("."))
  .withId(rootProjectName)
  .aggregate(scalamacros, scalamacrosUsage)
  .settings(
    commonSettings,
    name := rootProjectName,
    addCompilerPlugin(scalaMacrosParadise)
  )

val scalamacrosProjectName = "scala-macros"
lazy val scalamacros = (project in file(scalamacrosProjectName))
  .withId(scalamacrosProjectName)
  .settings(
    commonSettings,
    name := scalamacrosProjectName,
    libraryDependencies ++= scalaReflect :: Nil,
    addCompilerPlugin(scalaMacrosParadise)
  )

val scalamacrosUsageProjectName = "scala-macros-usage"
lazy val scalamacrosUsage = (project in file(scalamacrosUsageProjectName))
  .withId(scalamacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    name := scalamacrosUsageProjectName,
    addCompilerPlugin(scalaMacrosParadise)
  )
