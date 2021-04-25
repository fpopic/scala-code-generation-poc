import sbt._

object Dependencies {
  val scalaReflect = "org.scala-lang" % "scala-reflect"
  val scalaPbRuntime = "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion

  val beamVersion = "2.28.0"
  val beamSdksJavaCore = "org.apache.beam" % "beam-sdks-java-core" % beamVersion
  val beamRunnersDirectJava = "org.apache.beam" % "beam-runners-direct-java" % beamVersion % Runtime
}
