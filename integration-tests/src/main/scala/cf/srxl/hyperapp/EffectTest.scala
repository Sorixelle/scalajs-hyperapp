package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters._
import org.scalajs.dom._

@JSExportTopLevel("EffectTest")
object EffectTest {
  class Delay[S <: js.Object](time: Int, message: String, action: Action[S]) extends Effect {
    override val effect: (JSObj, DispatchFunc) => js.Any = (props, dispatch) => {
      window.setTimeout(() => dispatch(props.get("action").orUndefined, props.getOrElse("message", "")), time)
    }

    override def mkPropsMap: JSObj = Map(
      "time" -> time,
      "message" -> message,
      "action" -> action.toJS
    )
  }

  @JSExport
  def run(): Unit = {
    class AppState(val text: String) extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState = new AppState(
        obj.get("text").map(_.asInstanceOf[String]).getOrElse("empty_state")
      )
    }
    implicit val sc: AppStateConverter = new AppStateConverter

    val Update: Action2Data[AppState] = (_: AppState, data: js.Any) => new ActionResult(
      new AppState(data.asInstanceOf[String])
    )

    val Enqueue: Action2Args[AppState] = (state: AppState, args: JSObj) => new ActionResult(
      state,
      new Delay(5000, args.string("msg").getOrElse(""), new Action(Update))
    )

    val view = (s: AppState) => {
      <("div", ^(),
        <("h1", ^("id" -> "message"), s.text),
        <("button", ^("id" -> "update", "onClick" -> new Action(Enqueue, Map[String, js.Any]("msg" -> "Pong")).toJS), "Update")
      )
    }

    new Hyperapp[AppState](
      new ActionResult(new AppState("Ping")),
      view,
      (_: AppState) => List(),
      document.getElementById("app")
    ).run()
  }
}
