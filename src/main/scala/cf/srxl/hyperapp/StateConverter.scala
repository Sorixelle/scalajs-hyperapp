package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL.Dict

import scala.scalajs.js

abstract class StateConverter[S <: js.Object] extends js.Object {
  def fromJS(obj: Dict): S
}
