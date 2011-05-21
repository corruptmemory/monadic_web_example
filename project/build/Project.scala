import sbt._
import de.element34.sbteclipsify._

class MonadicWeb(info: ProjectInfo) extends DefaultProject(info)  with Eclipsify { 
  val scalacheck = "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test"
}

