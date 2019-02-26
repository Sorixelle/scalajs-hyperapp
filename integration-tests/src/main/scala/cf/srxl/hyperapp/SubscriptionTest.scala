package cf.srxl.hyperapp

import cf.srxl.hyperapp.DSL._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters._
import org.scalajs.dom._

@JSExportTopLevel("SubscriptionTest")
object SubscriptionTest {
  class ReadInput[S <: js.Object](action: Action[S]) extends Effect {
    override val effect: (JSObj, DispatchFunc) => js.Any = (props, dispatch) => {
      val handler: js.Function1[Event, Unit] = (e: Event) =>
        dispatch(props.get("action").orUndefined, e.target.asInstanceOf[html.Input].value)

      window.addEventListener("input", handler)
      () => window.removeEventListener("input", handler)
    }

    override def mkPropsMap: JSObj = Map("action" -> action.toJS)
  }

  @JSExport
  def run(): Unit = {
    class AppState(val text: String, val update: Boolean) extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState =
        new AppState(obj.getOrElse("text", "<empty>").asInstanceOf[String],
          obj.getOrElse("update", true).asInstanceOf[Boolean])
    }
    implicit val sc: AppStateConverter = new AppStateConverter

    val Update: Action2Data[AppState] = (s: AppState, data: js.Any) => new ActionResult(
      new AppState(data.asInstanceOf[String], s.update)
    )

    val ToggleUpdate: Action1[AppState] = (s: AppState) => new ActionResult(
      new AppState(s.text, !s.update)
    )

    val view = (s: AppState) =>
      <("div", ^(),
        <("h1", ^("id" -> "text"), s.text),
        <("p", ^(), s.update.toString),
        <("input", ^("id" -> "textInput")),
        <("button", ^("id" -> "updateToggle", "onClick" -> new Action(ToggleUpdate).toJS), "Toggle Update")
      )

    new Hyperapp(
      new ActionResult(new AppState("<empty>", true)),
      view,
      (s: AppState) => {
        if (s.update) List(new ReadInput(new Action(Update))) else List()
      },
      document.getElementById("app")
    ).run()
  }
}
