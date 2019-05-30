val scalaV = "2.12.1"

val bigQueryVersion = "0.37.0-beta"
val gcloudStorageVersion = "1.12.0"

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
      "org.scala-lang" % "scala-reflect" % scalaV,
      "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
      "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
    ),
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    name := scalamacrosProjectName
  )

val scalamacrosUsageProjectName = "scala-macros-usage"
lazy val scalamacrosUsage = (project in file(scalamacrosUsageProjectName))
  .withId(scalamacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
      "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
    ),
    name := scalamacrosUsageProjectName,
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  )