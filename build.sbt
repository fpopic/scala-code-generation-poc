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
    libraryDependencies ++= Seq(),
    name := scalamacrosUsageProjectName,
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  )

/////////////

val scalametaProjectName = "scala-meta"
lazy val scalameta = (project in file(scalametaProjectName))
  .withId(scalametaProjectName)
  .settings(
    commonSettings,
    libraryDependencies ++= {
      val bigQueryVersion = "0.37.0-beta"
      val gcloudStorageVersion = "1.12.0"
      Seq(
        "org.scalameta" %% "scalameta" % "1.8.0",
        "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
        "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
      )
    },
    addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M8" cross CrossVersion.full),
    name := scalametaProjectName
  )

val scalametaUsageProjectName = "scala-meta-usage"
lazy val scalametaUsage = (project in file(scalametaUsageProjectName))
  .withId(scalametaUsageProjectName)
  .dependsOn(scalameta)
  .settings(
    commonSettings,
    libraryDependencies ++= {
      val bigQueryVersion = "0.37.0-beta"
      val gcloudStorageVersion = "1.12.0"
      Seq(
        "org.scalameta" %% "scalameta" % "1.8.0",
        "com.google.cloud" % "google-cloud-bigquery" % bigQueryVersion,
        "com.google.cloud" % "google-cloud-storage" % gcloudStorageVersion,
      )
    },
    addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M8" cross CrossVersion.full),
    name := scalametaUsageProjectName,
  )
  