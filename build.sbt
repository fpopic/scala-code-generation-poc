import Dependencies._
Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  Seq(
    version := "0.1",
    scalaVersion := "2.13.5",
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-language:experimental.macros",
      //"-Xlog-implicits",
      "-Ymacro-debug-lite"
    ),
    // for nicer error messages
    addCompilerPlugin("io.tryp" % "splain" % "0.5.8" cross CrossVersion.patch)
  )
)

lazy val root = (project in file("."))
  .aggregate(`scala-macros`, `scala-macros-usage`)

lazy val `scala-macros` = (project in file("scala-macros"))
  .settings(
    libraryDependencies ++= Seq(
      scalaReflect % scalaVersion.value,
      scalaPbRuntime,
      beamSdksJavaCore,
      beamRunnersDirectJava
    )
  )

lazy val `scala-macros-usage` = (project in file("scala-macros-usage"))
  .dependsOn(`scala-macros`)
  .settings(
    libraryDependencies ++= Seq(
      scalaPbRuntime,
      beamSdksJavaCore,
      beamRunnersDirectJava,
      scalaTest
    )
  )
