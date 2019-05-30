val scalaV = "2.12.1"

val bigQueryVersion = "0.37.0-beta"
val gcloudStorageVersion = "1.12.0"

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
      "org.scalameta" %% "scalameta" % "1.8.0",
      "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
      "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
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
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "1.8.0",
      "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
      "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
    ),
    addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M8" cross CrossVersion.full),
    name := scalametaUsageProjectName,
  )