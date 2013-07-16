// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "cilib repository" at "http://cilib.net/maven/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")

resolvers += {
  "remeniuk repo" at "http://remeniuk.github.com/maven" 
}

libraryDependencies += {
  "org.netbeans" %% "sbt-netbeans-plugin" % "0.1.5"
}

