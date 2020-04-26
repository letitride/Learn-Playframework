package models

import play.api.data.Form
import play.api.data.Forms._


case class Person(id: Int, name: String, mail: String, tel: String)
case class PersonForm(name: String, mail: String, tel: String)

object Person {
  val ppersonForm:Form[PersonForm] = Form {
    mapping(
      "name" -> text,
      "mail" -> text,
      "tel" -> text
    )(PersonForm.apply)(PersonForm.unapply)
  }
}

