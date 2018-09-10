val commonSettings = Seq(
  scalaVersion := "2.12.6",
  libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  scalacOptions += "-P:scalajs:sjsDefinedByDefault"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "scalajs-hyperapp",
    version := "1.0.0",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
    npmDependencies in Compile += "hyperapp" -> "1.2.8",
    webpackBundlingMode := BundlingMode.LibraryOnly()
  ).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

lazy val integrationTests = (project in file("integration-tests"))
  .settings(
    commonSettings,
    name := "scalajs-hyperapp-integration-tests",
    version := "1.0.0",
    webpackBundlingMode := BundlingMode.LibraryAndApplication()
  ).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .dependsOn(root)
