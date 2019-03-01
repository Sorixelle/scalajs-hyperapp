val commonSettings = Seq(
  version := "1.0.0",
  scalaVersion := "2.12.8",
  libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  scalacOptions += "-P:scalajs:sjsDefinedByDefault"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "scalajs-hyperapp",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
    npmDependencies in Compile += "hyperapp" -> "2.0.0-alpha.2",
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    publishTo := sonatypePublishTo.value,
    sourceGenerators in Compile += Def.taskDyn {
      val outputFile = sourceManaged.in(Compile).value /
        "cf" / "srxl" / "hyperapp" / "Tags.scala"
      val tagsListFile = baseDirectory.in(Compile).in(codegen).value /
        "tagList.txt"
      Def.task {
        (run in codegen in Compile)
          .toTask(s" ${outputFile.getAbsolutePath} ${tagsListFile.getAbsolutePath}")
          .value
        Seq(outputFile)
      }
    }.taskValue
  ).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

lazy val integrationTests = (project in file("integration-tests"))
  .settings(
    commonSettings,
    name := "scalajs-hyperapp-integration-tests",
    webpackBundlingMode := BundlingMode.LibraryAndApplication()
  ).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .dependsOn(root)

lazy val codegen = (project in file("codegen"))
  .settings(
    name := "scalajs-hyperapp-codegen",
  )