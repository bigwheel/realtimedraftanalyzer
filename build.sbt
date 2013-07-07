import AssemblyKeys._ // put this at the top of the file

name := "realtimedraftanalyzer"

version := "0.3"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.12.3" % "test",
    "org.scala-lang" % "scala-swing" % "2.9.2"
)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")

test in assembly := {}

assemblySettings
