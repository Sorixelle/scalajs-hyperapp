sonatypeProfileName := "cf.srxl"
publishMavenStyle := true
licenses := Seq("BSD 3-Clause" -> url("https://github.com/Sorixelle/scalajs-hyperapp/blob/master/LICENSE"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("Sorixelle", "scalajs-hyperapp", "srxl@protonmail.com"))