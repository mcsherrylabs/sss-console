import NativePackagerHelper._

enablePlugins(JavaAppPackaging)

packageSummary in Linux := "sss-console"

scalaVersion := "2.11.8"

version := "0.4"

resolvers += "indvd00m-github-repo" at "https://github.com/indvd00m/maven-repo/raw/master/repository"

resolvers += "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"

//Seq(vaadinWebSettings: _*)

val vaadinVer = "7.5.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0",
  "org.eclipse.jetty.aggregate" % "jetty-all-server" % "8.1.18.v20150929",
  "com.vaadin" % "vaadin-server" % vaadinVer,
  "com.vaadin" % "vaadin-themes" % vaadinVer,
  "com.vaadin" % "vaadin-push" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiler" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiled" % vaadinVer,
  "org.vaadin7.addons" % "console" %  "1.3.0",
  "us.monoid.web" % "resty" % "0.3.2",
  "mcsherrylabs.com" %% "sss-ancillary" % "0.9.3",
  "org.scalatra" % "scalatra_2.11" % "2.4.0",
  "io.spray" %%  "spray-json" % "1.3.2"
)


// Settings for the Vaadin plugin widgetset compilation
// Widgetset compilation needs memory and to avoid an out of memory error it usually needs more memory:
//javaOptions in compileVaadinWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M")

//vaadinWidgetsets := Seq("DemoWidgetSet")

//vaadinOptions in compileVaadinWidgetsets := Seq("-logLevel", "DEBUG", "-strict")

// Compile widgetsets into the source directory (by default themes are compiled into the target directory)
//target in compileVaadinWidgetsets := (baseDirectory).value / "WebContent" / "VAADIN" / "widgetsets"

mappings in Universal ++= directory("WebContent")

mainClass in (Compile, run) := Some("sss.ui.ServerLauncher")
