package controllers

import java.sql.SQLException

import javax.inject._
import play.api.db.Database
import play.api.mvc._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(db: Database, cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {

  def index() = Action { implicit request =>
    var msg = "database record:<br><ul>"
    try{
      db.withConnection{ conn =>
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("select * from people")
        while(rs.next){
          msg += "<li>" + rs.getInt("id") + ":" + rs.getString("name") + "</li>"
        }
        msg += "</ul>"
      }
    }catch{
      case e:SQLException => msg = "<li>no record...</li>"
    }

    Ok(views.html.index( msg ))
  }

}
