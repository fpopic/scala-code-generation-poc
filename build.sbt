name := "generic-programming-shapeless"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

