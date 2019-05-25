lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.6",
  scalacOptions ++= Seq("-feature")
)

val scalametaProjectName = "scala-meta"
lazy val scalameta = (project in file(scalametaProjectName))
  .withId(scalametaProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "4.1.0"
    ),
    name := scalametaProjectName
  )

val scalamacrosProjectName = "scala-macros"
lazy val scalamacros = (project in file(scalamacrosProjectName))
  .withId(scalamacrosProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.8"
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
