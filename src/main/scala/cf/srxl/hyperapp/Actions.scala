package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL.{Dict, State}

import scala.language.{implicitConversions, postfixOps}
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

sealed trait ActionsEntry {
  def toJS: js.Any
}

class Actions(val actions: Map[String, ActionsEntry]) extends ActionsEntry {
  def scope(key: String): Option[Actions] = actions.get(key) collect {
    case a: Actions => a
  }

  def action(key: String): Option[Action] = actions.get(key) collect {
    case a: Action => a
  }

  def stateAction(key: String): Option[ActionWithState] = actions.get(key) collect {
    case a: ActionWithState => a
  }

  def asyncAction(key: String): Option[AsyncAction] = actions.get(key) collect {
    case a: AsyncAction => a
  }

  def mkTagObject(): Unit = {
    def run(name: String, as: Map[String, ActionsEntry]): Unit = {
      js.Dynamic.global.__sjsActionMeta.updateDynamic(name)(as map {
        case (k, v: Actions) =>
          run(s"$name.$k", v.actions)
          (k, "scope")
        case (k, _: ActionFunc[js.Any]) => (k, "action")
      } toJSDictionary)
    }

    run("root", actions)
  }

  override def toJS: Dict = actions mapValues (_.toJS) toJSDictionary

  override def toString: String = s"Actions($actions)"
}

sealed abstract class ActionFunc[+A](val func: js.Any => A) extends ActionsEntry {
  def apply(v: js.Any): A = func(v)

  override def toJS: js.Function
}

case class Action(f: js.Any => Option[State]) extends ActionFunc[Option[State]](f) {
  override def toJS: js.Function1[js.Any, Dict] = a => f(a).getOrElse(Map()).toJSDictionary
}
case class ActionWithState(f: js.Any => State => Option[State]) extends ActionFunc[State => Option[State]](f) {
  override def toJS: js.Function1[js.Any, js.Function1[Dict, Dict]] =
    a => (state: Dict) => f(a)(state.toMap).getOrElse(Map()).toJSDictionary
}
case class AsyncAction(f: js.Any => (State, WiredActions) => Option[State]) extends ActionFunc[(State, WiredActions) => Option[State]](f) {
  override def toJS: js.Function1[js.Any, js.Function2[Dict, Dict, js.Any]] =
    a => (state: Dict, actions: Dict) => f(a)(state.toMap, WiredActions.fromJS(actions)).orUndefined
}

object Actions {
  def apply(as: (String, ActionsEntry)*) = new Actions(as.toMap)
}