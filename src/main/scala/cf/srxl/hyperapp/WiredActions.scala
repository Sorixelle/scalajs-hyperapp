package cf.srxl.hyperapp

import cf.srxl.hyperapp.Implicits.{Dict, JSObj}

import scala.language.postfixOps
import scala.scalajs.js

sealed trait WiredActionsEntry
case class WiredActions(as: Map[String, WiredActionsEntry]) extends WiredActionsEntry {
  def scope(key: String): Option[WiredActions] = as.get(key).map(_.asInstanceOf[WiredActions])
  def action(key: String): Option[js.Any => JSObj] = as.get(key).map(_.asInstanceOf[WiredAction].f)
}
case class WiredAction(f: js.Any => JSObj) extends WiredActionsEntry

object WiredActions {
  def apply(as: (String, WiredActionsEntry)*): WiredActions = WiredActions(as.toMap)

  def fromJS(dict: Dict): WiredActions = {
    val d = dict.toMap
    new WiredActions(d mapValues {
      case action: js.Function => WiredAction((a: js.Any) => action.asInstanceOf[js.Function1[js.Any, Dict]](a).toMap)
      case s => fromJS(s.asInstanceOf[Dict])
    })
  }
}
