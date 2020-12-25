name := "spsc"

version := "0.1"

scalaVersion := "3.0.0-M3"

libraryDependencies += "org.scalameta" %% "munit-scalacheck" % "0.7.20" % Test

testFrameworks += new TestFramework("munit.Framework")
