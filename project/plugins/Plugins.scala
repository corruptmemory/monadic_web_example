import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots"
  val scalaToolsReleases = "Scala-Tools Maven2 Repository Releases" at "http://scala-tools.org/repo-releases"
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.4.0"
  val eclipsifyPlugin = "de.element34" % "sbt-eclipsify" % "0.7.0"
}
