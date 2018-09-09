package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL.Dict

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom

@js.native
@JSImport("hyperapp", JSImport.Namespace)
object Hyperapp extends js.Object {
  def app(
           state: Dict,
           actions: Dict,
           view: js.Function2[Dict, Dict, ViewNode],
           container: dom.Node
         ): Dict = js.native
}
