
lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.1"

crossScalaVersions := Seq(scala212, scala213)

lazy val commonSettings = Seq(
  organization := "com.mcsherrylabs",
  scalaVersion := "2.13.1",
  updateOptions := updateOptions.value.withGigahorse(false),
  resolvers += "stepsoft" at "http://nexus.mcsherrylabs.com/repository/releases",
  resolvers += "stepsoft-snapshots" at "http://nexus.mcsherrylabs.com/repository/snapshots",
  // https://mvnrepository.com/artifact/org.scalatra/scalatra
  libraryDependencies += "org.scalatra" %% "scalatra" % "2.7.0",
  libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"
)

lazy val util = (project in file("sss.console-util"))
  .settings(commonSettings)

lazy val console = (project in file("sss.console"))
  .settings(commonSettings)