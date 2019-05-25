val scalaV = "2.12.1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scalaV
)

val scalametaProjectName = "scala-meta"
lazy val scalameta = (project in file(scalametaProjectName))
  .withId(scalametaProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "1.8.0" % Provided,
      "org.scala-lang" % "scala-reflect" % scalaV
    ),
    name := scalametaProjectName,
    addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M8" cross CrossVersion.full)
  )

val scalamacrosProjectName = "scala-macros"
lazy val scalamacros = (project in file(scalamacrosProjectName))
  .withId(scalamacrosProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaV
    ),
    name := scalamacrosProjectName
  )

val usageProjectName = "usage"
lazy val usage = (project in file(usageProjectName))
  .withId(usageProjectName)
  .dependsOn(scalamacros, scalameta)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(),
    name := usageProjectName
  )
