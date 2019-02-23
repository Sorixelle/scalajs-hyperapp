package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom

class Hyperapp[S <: js.Object](val init: ActionResult[S],
                               val view: S => ViewNode,
                               val container: dom.Node) {
  def run(): Unit = {
    Hyperapp.app(new InternHyperappConfig(
      init.toJS,
      view,
      container
    ))
  }
}

class InternHyperappConfig(
  val init: js.Any,
  val view: js.Function,
  val container: dom.Node
) extends js.Object

@js.native
@JSImport("hyperapp", JSImport.Namespace)
object Hyperapp extends js.Object {
  def app(config: InternHyperappConfig): Dict = js.native
  def h(name: String, props: Dict, children: js.Any*): ViewNode = js.native
}
