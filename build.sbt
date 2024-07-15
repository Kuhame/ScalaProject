ThisBuild / organization := "fr.efrei.scalaproject"
ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version := "0.1.0-SNAPSHOT"

val scalaTestVersion = "3.2.19"

lazy val graph = project
  .in(file("graph"))
  .settings(
    name := "graph",
    libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % scalaTestVersion % Test,
    libraryDependencies += "dev.zio" %% "zio-json" % "0.7.1",
    
  )

lazy val app = project
  .in(file("app"))
  .settings(
    name := "app",
    libraryDependencies += "dev.zio" %% "zio" % "2.0.6",
    libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.6",
    libraryDependencies += "dev.zio" %% "zio-test" % "2.0.15" % Test,
    libraryDependencies += "dev.zio" %% "zio-test-sbt" % "2.0.15" % Test
  )
  .dependsOn(graph)

lazy val root = project
  .aggregate(graph, app)
  .settings(
    addCommandAlias("run","app/run")
  )