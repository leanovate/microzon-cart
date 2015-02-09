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

val debian = TaskKey[Unit]("debian", "Create debian package")

debian <<= (assembly, baseDirectory, version) map { (asm, base, ver) =>
  val bintrayUser = System.getenv("BINTRAY_USER")
  val bintrayKey = System.getenv("BINTRAY_KEY")
  val release = ver + "-" + System.getenv("TRAVIS_BUILD_NUMBER")
  val debOut = (base / "target" / "microzon-cart.deb")
  val debBase = (base / "target" / "deb")
  IO.copyFile(asm, debBase / "opt" / "cart" / "cart.jar")
  IO.copyFile(base / "src" / "main" / "supervisor" / "cart.conf", debBase / "etc" / "supervisor" / "conf.d" / "cart.conf")
  IO.write(debBase / "DEBIAN" / "control",
    s"""Package: microzon-customer
    |Version: $release
    |Section: misc
    |Priority: extra
    |Architecture: all
    |Depends: supervisor, oracle-java8-installer
    |Maintainer: Bodo Junglas <landru@untoldwind.net>
    |Homepage: http://github.com/leanovate/microzon
    |Description: Customer service
    |""".stripMargin)
  s"/usr/bin/fakeroot /usr/bin/dpkg-deb -b ${debBase.getPath} ${debOut.getPath}" !;
  s"/usr/bin/curl -T ${debOut.getPath} -u${bintrayUser}:${bintrayKey} https://api.bintray.com/content/untoldwind/deb/microzon/${ver}/pool/main/m/microzon/microzon-cart-${release}_all.deb;deb_distribution=trusty;deb_component=main;deb_architecture=all?publish=1" !
}