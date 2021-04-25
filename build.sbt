name := "generic-programming-shapeless"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.4"

Compile / PB.targets  := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value
)
