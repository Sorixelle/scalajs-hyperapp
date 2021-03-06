package cf.srxl.hyperapp

import org.scalatest.FlatSpec
import DSL._

import scala.scalajs.js

class ViewDSLTest extends FlatSpec {
  val component = Component(props =>
    <("pre", ^(), props.string("message").get)
  )

  val lazyComponent = LazyComponent(_ => (_, _) =>
    <("p", ^(), "lazyComponent")
  )

  val node: ViewNode = <("div", ^(),
    <("p", ^(), "text"),
    <(component, ^("message" -> "message")),
    <(lazyComponent, ^()),
    <("h1", ^("key" -> "abc123"))
  )

  "ViewNode" should "have correct shape" in {
    assert(node.nodeName == "div")
    assert(node.attributes.toMap.isEmpty)
    assert(node.key.toOption.isEmpty)
    assert(node.children.length == 4)

    assert(node.children(0).isInstanceOf[ViewNode])
    val p = node.children(0).asInstanceOf[ViewNode]
    assert(p.children(0).asInstanceOf[String] == "text")

    assert(node.children(1).isInstanceOf[ViewNode])
    val comp = node.children(1).asInstanceOf[ViewNode]
    assert(comp.nodeName == "pre")

    assert(node.children(2).isInstanceOf[js.Function])

    val h1 = node.children(3).asInstanceOf[ViewNode]
    assert(h1.attributes.toMap.length == 1)
    assert(h1.attributes.toMap.get("key").contains("abc123"))
    assert(h1.children.length == 0)
    assert(h1.key.toOption.contains("abc123"))
  }
}
