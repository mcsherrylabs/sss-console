import NativePackagerHelper._

enablePlugins(JavaAppPackaging)

packageSummary in Linux := "sss-console"

version := "0.4"

resolvers += "indvd00m-github-repo" at "https://github.com/indvd00m/maven-repo/raw/master/repository"

resolvers += "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"

//Seq(vaadinWebSettings: _*)

val vaadinVer = "7.7.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % Vers.akkaVer,
  "com.typesafe.akka" %% "akka-remote" % Vers.akkaVer,
  //"org.eclipse.jetty.aggregate" % "jetty-all-server" % "8.1.18.v20150929",
  // https://mvnrepository.com/artifact/javax.portlet/portlet-api
  "javax.portlet" % "portlet-api" % "2.0" % "provided",
  "com.vaadin" % "vaadin-server" % vaadinVer,
  "com.vaadin" % "vaadin-themes" % vaadinVer,
  "com.vaadin" % "vaadin-push" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiler" % vaadinVer,
  "com.vaadin" % "vaadin-client-compiled" % vaadinVer,
  "org.vaadin7.addons" % "console" %  "1.3.0",
  "us.monoid.web" % "resty" % "0.3.2",
  "com.mcsherrylabs" %% "sss-ancillary" % Vers.ancillaryVer,
  "com.mcsherrylabs" %% "sss-vaadin-akka-reactive" % "0.4-SNAPSHOT",
  "com.mcsherrylabs" %% "sss-console-util" % Vers.consoleUtilVer % Test
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
