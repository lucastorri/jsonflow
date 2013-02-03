name := "jsonflow"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions := Seq("-deprecation")

libraryDependencies ++= Seq(
	"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3",
	"org.specs2" %% "specs2" % "1.13" % "test"
)

resolvers ++= Seq(
	"snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
	"releases"  at "http://oss.sonatype.org/content/repositories/releases"
)