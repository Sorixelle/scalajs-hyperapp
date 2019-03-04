package cf.srxl.hyperapp

import cf.srxl.hyperapp.Implicits._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.JSConverters._
import org.scalajs.dom

class Hyperapp[S <: js.Object](val init: ActionResult[S],
                               val view: S => ViewNode,
                               val subscriptions: S => Seq[Effect],
                               val container: dom.Node)
                              (implicit sc: StateConverter[S]) {
  def run(): Unit = {
    Hyperapp.app(new InternHyperappConfig(
      init.toJS,
      view,
      (state: Dict) => subscriptions(sc.fromJS(state)).map(_.toJS).toJSArray,
      container
    ))
  }
}

class InternHyperappConfig(
  val init: js.Any,
  val view: js.Function,
  val subscriptions: js.Function,
  val container: dom.Node
) extends js.Object

@js.native
@JSImport("hyperapp", JSImport.Namespace, "hyperapp")
object Hyperapp extends js.Object {
  def app(config: InternHyperappConfig): Dict = js.native
  def h(name: String, props: Dict, children: js.Any*): ViewNode = js.native
}
