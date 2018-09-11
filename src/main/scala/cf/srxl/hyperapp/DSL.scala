package cf.srxl.hyperapp

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object DSL {
  type Dict = js.Dictionary[js.Any]
  type State = Map[String, js.Any]
  type View = (State, WiredActions) => ViewNode

  implicit class StringOps(val s: String) extends AnyVal {
    def :>(as: (String, ActionsEntry)*): (String, Actions) = (s, new Actions(as.toMap))

    def -->[A](f: A => Option[State]): (String, Action) = (s, Action(f.asInstanceOf[js.Any => Option[State]]))

    def ==>[A](f: A => State => Option[State]): (String, ActionWithState) = (s, ActionWithState(f.asInstanceOf[js.Any => State => Option[State]]))

    def =/>[A](f: A => (State, Actions) => Option[State]): (String, AsyncAction) = (s, AsyncAction(f.asInstanceOf[js.Any => (State, WiredActions) => Option[State]]))

    def ::>(xs: (String, js.Any)*): (String, Dict) = (s, xs.toMap.toJSDictionary)
  }

  implicit class StateOps(val s: State) extends AnyVal {
    def scope(k: String): Option[State] = s.get(k) map (_.asInstanceOf[Dict].toMap)
    def string(k: String): Option[String] = s.get(k) map (_.asInstanceOf[String])
    def int(k: String): Option[Int] = s.get(k) map (_.asInstanceOf[Int])
    def float(k: String): Option[Float] = s.get(k) map (_.asInstanceOf[Float])
    def double(k: String): Option[Double] = s.get(k) map (_.asInstanceOf[Double])
    def boolean(k: String): Option[Boolean] = s.get(k) map (_.asInstanceOf[Boolean])
    def char(k: String): Option[Char] = s.get(k) map (_.asInstanceOf[Char])
    def array[A](k: String): Option[Array[A]] = s.get(k) map (_.asInstanceOf[Array[A]])
  }

  def <(name: String, attrs: Map[String, js.Any], children: js.Any*): ViewNode = new ViewNode(
    name,
    attrs.toJSDictionary,
    children.toJSArray,
    attrs.get("key").orUndefined
  )

  def <(component: ComponentType, attrs: Map[String, js.Any], children: js.Any*): js.Any = component match {
    case Component(f) => f(attrs)
    case ComponentWithChildren(f) => f(attrs, children)
    case LazyComponent(f) => (s: Dict, a: Dict) => f(attrs)(s.toMap, WiredActions.fromJS(a))
    case LazyComponentWithChildren(f) => (s: Dict, a: Dict) => f(attrs, children)(s.toMap, WiredActions.fromJS(a))
  }

  def ^(attrs: (String, js.Any)*): Map[String, js.Any] = attrs.toMap

  def app(
           state: State,
           actions: Actions,
           view: View,
           container: dom.Node
         ): WiredActions =
    WiredActions.fromJS(
      Hyperapp.app(state.toJSDictionary, actions.toJS, (s: Dict, a: Dict) => view(s.toMap, WiredActions.fromJS(a)), container)
    )
}
