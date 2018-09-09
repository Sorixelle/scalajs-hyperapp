package cf.srxl.hyperapp

import org.scalatest.{FlatSpec, Matchers}
import cf.srxl.hyperapp.DSL._

class ActionsTest extends FlatSpec with Matchers {
  val actions = Actions(
      "action1" --> ((_: String) => None),
      "action2" ==> ((_: String) => _ => None),
      "action3" =/> ((_: String) => (_, _) => None),
      "scope1" :> (
        "action4" --> ((_: String) => None)
      )
    )

  "ActionStringOps" should "properly create actions" in {
    actions.action("action1") shouldBe defined
    actions.scope("action1") shouldBe None
    actions.stateAction("action1") shouldBe None
    actions.asyncAction("action1") shouldBe None
    actions.stateAction("action2") shouldBe defined
    actions.asyncAction("action3") shouldBe defined
    actions.action("scope1") shouldBe None
    actions.scope("scope1").get.action("action4") shouldBe defined
    actions.action("invalid") shouldBe None
  }

  "Actions#toJS" should "properly convert to JavaScript object" in {
    val converted = actions.toJS.toMap

    converted should contain key "action1"
    converted should contain key "scope1"
    converted should not contain key ("invalid")
    converted.get("scope1") shouldBe defined
    converted("scope1").asInstanceOf[Dict].toMap should contain key "action4"
    converted("scope1").asInstanceOf[Dict].toMap.get("action4") shouldBe defined
  }
}
