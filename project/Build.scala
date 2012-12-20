import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "notmyissue"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.h2database" % "h2" % "1.3.167",
      "commons-beanutils" % "commons-beanutils" % "1.8.2",
      "commons-collections" % "commons-collections" % "3.2.1",
      "commons-lang" % "commons-lang" % "2.6"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
        lessEntryPoints <<= baseDirectory(customLessEntryPoints),
        routesImport += "controllers._",
        routesImport += "userselection._",
        routesImport += "controllers.IssuesOverviewState._"
    )
    
      // Only compile the bootstrap bootstrap.less file and any other *.less file in the stylesheets directory
  def customLessEntryPoints(base: File): PathFinder = (
      (base / "app" / "assets" / "bootstrap" * "bootstrap.less") +++
      (base / "app" / "assets" / "stylesheets" * "*.less")
  )

}
