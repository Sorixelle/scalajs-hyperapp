package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

sealed abstract class ActionFunc[S <: js.Object](f: js.Function) {
  def toJS: js.Function
}
case class Action1[S <: js.Object](f: S => ActionResult[S])(sc: StateConverter[S]) extends ActionFunc[S](f) {
  override def toJS: js.Function = (state: Dict) => f(sc.fromJS(state)).toJS
}
case class Action2Args[S <: js.Object](f: (S, JSObj) => ActionResult[S])(sc: StateConverter[S]) extends ActionFunc[S](f) {
  override def toJS: js.Function = (state: Dict, args: Dict) => f(sc.fromJS(state), args.toMap).toJS
}
case class Action2Data[S <: js.Object](f: (S, js.Any) => ActionResult[S])(sc: StateConverter[S]) extends ActionFunc[S](f) {
  override def toJS: js.Function = (state: Dict, data: js.Any) => f(sc.fromJS(state), data).toJS
}
case class Action3[S <: js.Object](f: (S, JSObj, js.Any) => ActionResult[S])(sc: StateConverter[S]) extends ActionFunc[S](f) {
  override def toJS: js.Function = (state: Dict, args: Dict, data: js.Any) => f(sc.fromJS(state), args.toMap, data).toJS
}

class Action[S <: js.Object](func: ActionFunc[S], args: Option[JSObj]) {
  def this(func: ActionFunc[S]) = this(func, None)
  def this(func: ActionFunc[S], args: JSObj) = this(func, Option(args))

  def toJS: js.Any = args match {
    case Some(a) => Seq(func.toJS, a.toJSDictionary).toJSArray
    case None => func.toJS
  }
}

class ActionResult[S <: js.Object](state: S, effect: Option[Effect[S]]) {
  def this(state: S) = this(state, None)

  def toJS: js.Any = effect match {
    case Some(e) => Seq[js.Any](state, e).toJSArray
    case None => state
  }
}
