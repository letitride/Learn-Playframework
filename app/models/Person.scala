package models

import play.api.data.Form
import play.api.data.Forms._


case class Person(id: Int, name: String, mail: String, tel: String)
case class PersonForm(name: String, mail: String, tel: String)

object Person {
  val personForm:Form[PersonForm] = Form {
    mapping(
      "name" -> nonEmptyText(maxLength = 3),
      "mail" -> email,
      "tel" -> nonEmptyText(maxLength = 3, minLength = 2).verifying(error="不正な番号", constraint = _.matches("""[1-9-]+"""))
    )(PersonForm.apply)(PersonForm.unapply)
  }
  val personFind:Form[PersonFind] = Form {
    mapping(
      "find" -> text
    )(PersonFind.apply)(PersonFind.unapply)
  }
}
case class PersonFind(find:String)

