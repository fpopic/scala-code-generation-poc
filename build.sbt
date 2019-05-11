lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.8"
)

val macrosProjectName = "macros"
lazy val macros = (project in file(macrosProjectName))
  .withId(macrosProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.8"
    ),
    name := macrosProjectName
  )

val usageProjectName = "usage"
lazy val usage = (project in file(usageProjectName))
  .withId(usageProjectName)
  .dependsOn(macros)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(),
    name := usageProjectName
  )
