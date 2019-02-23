package cf.srxl.hyperapp

import scala.scalajs.js

class ViewNode(
  val nodeName: String,
  val props: js.Dictionary[js.Any],
  val children: js.Array[js.Any],
  val key: js.UndefOr[js.Any]
) extends js.Object
