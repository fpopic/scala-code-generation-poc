val scala = "2.13.0-RC3"

val scalaReflect = "org.scala-lang" % "scala-reflect" %  scala
val bigQuery = "com.google.cloud" % "google-cloud-bigquery" % "0.37.0-beta"
val googleCloudStorage = "com.google.cloud" % "google-cloud-storage" % "1.12.0"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := scala
)

val scalamacrosProjectName = "scala-macros"
lazy val scalamacros = (project in file(scalamacrosProjectName))
  .withId(scalamacrosProjectName)
  .settings(
    commonSettings,
    name := scalamacrosProjectName,
    libraryDependencies ++= scalaReflect :: bigQuery :: googleCloudStorage :: Nil,
  )

val scalamacrosUsageProjectName = "scala-macros-usage"
lazy val scalamacrosUsage = (project in file(scalamacrosUsageProjectName))
  .withId(scalamacrosUsageProjectName)
  .dependsOn(scalamacros)
  .settings(
    commonSettings,
    name := scalamacrosUsageProjectName,
    libraryDependencies ++= bigQuery :: googleCloudStorage :: Nil
  )