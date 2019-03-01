package cf.srxl.hyperapp

import Implicits._
import cf.srxl.hyperapp.Tags._

import scala.scalajs.js
import org.scalajs.dom._
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("AppMountTest")
object AppMountTest {
  @JSExport
  def run(): Unit = {
    class AppState extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState = new AppState()
    }

    implicit val sc: AppStateConverter = new AppStateConverter()

    console.log(p(Map("id" -> "message"), "Hello, World!"))

    new Hyperapp[AppState](
      new ActionResult(new AppState),
      _ => p(Map("id" -> "message"), "Hello, World!"),
      (_: AppState) => List(),
      document.getElementById("app")
    ).run()
  }
}
