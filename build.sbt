val scalaV = "2.12.1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scalaV
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

val scalamacrosUsageProjectName = "scala-macros-usage"
lazy val scalamacrosUsage = (project in file(scalamacrosUsageProjectName))
  .withId(scalamacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(),
    name := scalamacrosUsageProjectName,
  )

/////////////

val scalametaProjectName = "scala-meta"
lazy val scalameta = (project in file(scalametaProjectName))
  .withId(scalametaProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "1.8.0" % Provided
    ),
    addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M8" cross CrossVersion.full),
    name := scalametaProjectName
  )

val scalametaUsageProjectName = "scala-meta-usage"
lazy val scalametaUsage = (project in file(scalametaUsageProjectName))
  .withId(scalametaUsageProjectName)
  .dependsOn(scalameta)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(),
    name := scalametaUsageProjectName,
  )
  