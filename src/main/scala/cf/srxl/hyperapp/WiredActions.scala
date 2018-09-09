package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL.{Dict, State}

import scala.language.postfixOps
import scala.scalajs.js

sealed trait WiredActionsEntry
case class WiredActions(as: Map[String, WiredActionsEntry]) extends WiredActionsEntry {
  def scope(key: String): Option[WiredActions] = as.get(key).map(_.asInstanceOf[WiredActions])
  def action(key: String): Option[js.Any => State] = as.get(key).map(_.asInstanceOf[WiredAction].f)
}
case class WiredAction(f: js.Any => State) extends WiredActionsEntry

object WiredActions {
  def apply(as: (String, WiredActionsEntry)*): WiredActions = WiredActions(as.toMap)

  def fromJS(obj: Dict): WiredActions = {
    def run(dict: Dict, name: String): WiredActions = {
      val d = dict.toMap
      new WiredActions(js.Dynamic.global.__sjsActionMeta.selectDynamic(name).asInstanceOf[js.Dictionary[String]] map { case (k, t) => t match {
        case "scope" => (k, run(d(k).asInstanceOf[Dict], s"$name.$k"))
        case "action" => (k, WiredAction((a: js.Any) => d(k).asInstanceOf[js.Function1[js.Any, Dict]](a).toMap))
      }} toMap)
    }

    run(obj, "root")
  }
}
