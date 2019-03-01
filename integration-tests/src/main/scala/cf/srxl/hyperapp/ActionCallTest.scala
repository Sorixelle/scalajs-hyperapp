package cf.srxl.hyperapp

import cf.srxl.hyperapp.Implicits._
import cf.srxl.hyperapp.Tags._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom._

@JSExportTopLevel("ActionCallTest")
object ActionCallTest {
  @JSExport
  def run(): Unit = {
    class AppState(val counter: Int) extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState =
        new AppState(obj.get("counter").map(_.asInstanceOf[Int]).getOrElse(0))
    }
    implicit val sc: AppStateConverter = new AppStateConverter

    val Up: Action2Args[AppState] = (s: AppState, a: JSObj) => new ActionResult(new AppState(s.counter + a.int("by").getOrElse(1)))
    val Down: Action2Args[AppState] = (s: AppState, a: JSObj) => new ActionResult(new AppState(s.counter - a.int("by").getOrElse(1)))

    val view = (s: AppState) =>
      div(Map(),
        h1(Map("id" -> "counter"), s.counter),
        button(Map("id" -> "increment", "onClick" -> new Action[AppState](Up, Map[String, js.Any]("by" -> 1)).toJS), "+"),
        button(Map("id" -> "decrement", "onClick" -> new Action[AppState](Down, Map[String, js.Any]("by" -> 1)).toJS), "-")
      )

    new Hyperapp[AppState](
      new ActionResult(new AppState(0)),
      view,
      (_: AppState) => List(),
      document.getElementById("app")
    ).run()
  }
}
