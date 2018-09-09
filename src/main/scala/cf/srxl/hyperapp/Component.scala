package cf.srxl.hyperapp

import scala.scalajs.js

import cf.srxl.hyperapp.DSL.View

sealed trait ComponentType
case class Component(f: Map[String, js.Any] => ViewNode) extends ComponentType
case class ComponentWithChildren(f: (Map[String, js.Any], Seq[js.Any]) => ViewNode) extends ComponentType
case class LazyComponent(f: Map[String, js.Any] => View) extends ComponentType
case class LazyComponentWithChildren(f: (Map[String, js.Any], Seq[js.Any]) => View) extends ComponentType
