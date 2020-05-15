package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}


case class Person(id: Int, name: String, mail: String, tel: String)
case class PersonForm(name: String, mail: String, tel: String)

object Person {

  val passwordCheckConstraint: Constraint[String] = Constraint("constraints.passwordcheck")({
    plainText =>
      println(plainText)
      val errors = plainText match {
        case "a" => Seq(ValidationError("Password is all numbers"))
        case "b" => Seq(ValidationError("Password is all letters"))
        case _ => Nil
      }
      println(errors)
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  val personForm:Form[PersonForm] = Form {
    mapping(
      "name" -> nonEmptyText.verifying(passwordCheckConstraint),
      "mail" -> email,
      "tel" -> nonEmptyText(maxLength = 13, minLength = 10).verifying(error="不正な番号です", constraint = _.matches("""[0-9-]+"""))
    )(PersonForm.apply)(PersonForm.unapply)
  }
  val personFind:Form[PersonFind] = Form {
    mapping(
      "find" -> text
    )(PersonFind.apply)(PersonFind.unapply)
  }
}
case class PersonFind(find:String)

