import sbt._

object Dependencies {
  val scalaReflect = "org.scala-lang" % "scala-reflect"
  val scalaPbRuntime = "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion
}
