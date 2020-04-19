package controllers

object MyForm {

  import play.api.data._
  import play.api.data.Forms._

  case class Data(name:String, pass:String)

  val myForm = Form(
    mapping(
      "name" -> text,
      "pass" -> text
    )(Data.apply)(Data.unapply)
  )
}
