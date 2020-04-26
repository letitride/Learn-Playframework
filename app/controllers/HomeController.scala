package controllers

import java.sql.SQLException

import javax.inject._
import play.api.db.Database
import play.api.mvc._
import anorm._
import models.PersonRepository
import org.postgresql.util.PSQLException

import scala.concurrent.ExecutionContext

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
}
