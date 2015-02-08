import AssemblyKeys._

name := """cart"""

version := "0.1.0"

scalaVersion := "2.10.4"

resolvers += "Twitter" at "http://maven.twttr.com/"


libraryDependencies ++= Seq(
  "com.twitter" %% "finatra" % "1.6.0",
  "com.twitter" %% "finagle-mysql" % "6.20.0",
  "org.flywaydb" % "flyway-core" % "3.0",
  "mysql" % "mysql-connector-java" % "5.1.32",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.slf4j" % "jul-to-slf4j" % "1.7.7",
  "tyrex" % "tyrex" % "1.0.1"
)

assemblySettings

mainClass in assembly := Some("de.leanovate.dose.cart.Application")
