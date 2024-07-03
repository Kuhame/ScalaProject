ThisBuild / organization := "fr.efrei.scalaproject"
ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version := "0.1.0-SNAPSHOT"

val scalaTestVersion = "3.2.19"

lazy val graph = project
  .in(file("graph"))
  .settings(
    name := "graph",
    libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % scalaTestVersion % Test
  )

lazy val app = project
  .in(file("app"))
  .settings(
    name := "app"
  )
  .dependsOn(graph)
