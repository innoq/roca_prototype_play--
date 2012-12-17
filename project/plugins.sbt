// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.0.4")

resolvers += {
  "remeniuk repo" at "http://remeniuk.github.com/maven" 
}

libraryDependencies += {
  "org.netbeans" %% "sbt-netbeans-plugin" % "0.1.4"
}

