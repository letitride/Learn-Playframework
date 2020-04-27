package controllers

import java.sql.SQLException

import javax.inject._
import play.api.db.Database
import play.api.mvc._
import anorm._
import models.{Person, PersonRepository}
import org.postgresql.util.PSQLException

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()
  (repository: PersonRepository, cc: MessagesControllerComponents)
  (implicit ec:ExecutionContext)
  extends MessagesAbstractController(cc) {

  def index() = Action.async { implicit request =>
    repository.list().map { people =>
      Ok(views.html.index("People Data.", people))
    }
  }

  def add() = Action{ implicit request =>
      Ok(views.html.add("フォームを記入して下さい。", Person.personForm))
  }

  def create() = Action.async{ implicit request =>
    Person.personForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.add("error.", errorForm)))
      },
      person => {
        repository.create(person.name, person.mail, person.tel).map{ _ =>
          Redirect(routes.HomeController.index())
        }
      }
    )
  }
}
