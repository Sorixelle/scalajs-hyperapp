package cf.srxl.hyperapp

import scala.scalajs.js

import scala.language.implicitConversions

object Implicits {
  type Dict = js.Dictionary[js.Any]
  type JSObj = Map[String, js.Any]

  implicit def actionFunc1Box[S <: js.Object](f: S => ActionResult[S])(implicit sc: StateConverter[S]): Action1[S] =
    Action1(f)(sc)
  implicit def actionFunc2ArgsBox[S <: js.Object](f: (S, JSObj) => ActionResult[S])(implicit sc: StateConverter[S]): Action2Args[S] =
    Action2Args(f)(sc)
  implicit def actionFunc2DataBox[S <: js.Object](f: (S, js.Any) => ActionResult[S])(implicit sc: StateConverter[S]): Action2Data[S] =
    Action2Data(f)(sc)
  implicit def actionFunc3Box[S <: js.Object](f: (S, JSObj, js.Any) => ActionResult[S])(implicit sc: StateConverter[S]): Action3[S] =
    Action3(f)(sc)

  implicit class StateOps(val s: JSObj) extends AnyVal {
    def obj(k: String): Option[JSObj] = s.get(k) map (_.asInstanceOf[Dict].toMap)
    def string(k: String): Option[String] = s.get(k) map (_.asInstanceOf[String])
    def int(k: String): Option[Int] = s.get(k) map (_.asInstanceOf[Int])
    def float(k: String): Option[Float] = s.get(k) map (_.asInstanceOf[Float])
    def double(k: String): Option[Double] = s.get(k) map (_.asInstanceOf[Double])
    def boolean(k: String): Option[Boolean] = s.get(k) map (_.asInstanceOf[Boolean])
    def char(k: String): Option[Char] = s.get(k) map (_.asInstanceOf[Char])
    def array[A](k: String): Option[Array[A]] = s.get(k) map (_.asInstanceOf[Array[A]])
  }
}
