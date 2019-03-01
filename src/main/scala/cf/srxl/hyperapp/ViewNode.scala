package cf.srxl.hyperapp

import scala.scalajs.js

class ViewNode(
  val nodeName: String,
  val props: js.Dictionary[js.Any],
  val children: js.Array[js.Any],
  val element: js.UndefOr[ViewNode],
  val key: js.UndefOr[js.Any],
  val `type`: Int
) extends js.Object
