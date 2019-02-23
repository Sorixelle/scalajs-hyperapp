package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

abstract class Effect[S <: js.Object] extends js.Object {
  type DispatchFunc = (js.Any, js.Any) => Unit
  val effect: (JSObj, DispatchFunc) => Unit

  def mkPropsMap: JSObj
  def toJS: js.Array[js.Any] = Seq[js.Any](
    (props: Dict, dispatch: js.Function2[js.Any, js.Any, Unit]) =>
      effect(props.toMap, (action: js.Any, data: js.Any) => dispatch(action, data)),
    mkPropsMap.toJSDictionary
  ).toJSArray
}
