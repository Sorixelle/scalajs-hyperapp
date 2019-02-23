package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

abstract class Effect[S <: js.Object] extends js.Object {
  type DispatchFunc = (Action[S], js.Any) => Unit
  val effect: (JSObj, DispatchFunc) => Unit

  def mkPropsMap: JSObj
  def toJS: js.Array[js.Any] = Seq[js.Any](
    (props: Dict, dispatch: (js.Any, js.Any) => Unit) =>
      effect(props.toMap, (action: Action[S], data: js.Any) => dispatch(action.toJS, data)),
    mkPropsMap.toJSDictionary
  ).toJSArray
}
