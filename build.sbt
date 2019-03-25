
lazy val commonSettings = Seq(
  organization := "com.mcsherrylabs",
  scalaVersion := "2.12.6",
  updateOptions := updateOptions.value.withGigahorse(false),
  resolvers += "stepsoft" at "http://nexus.mcsherrylabs.com/repository/releases",
  resolvers += "stepsoft-snapshots" at "http://nexus.mcsherrylabs.com/repository/snapshots",
  // https://mvnrepository.com/artifact/org.scalatra/scalatra
  libraryDependencies += "org.scalatra" %% "scalatra" % "2.6.3",
  libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"
)

lazy val util = (project in file("sss.console-util"))
  .settings(commonSettings)

lazy val console = (project in file("sss.console"))
  .settings(commonSettings)