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

  import PersonForm._

  def add() = Action{ implicit request =>
    Ok(views.html.add(
      "フォームを記入して下さい。", form
    ))
  }

  def create() = Action{ implicit request =>
    val formData = form.bindFromRequest()
    val data = formData.get
    try {
      db.withConnection{ conn =>
        val ps = conn.prepareStatement("insert into people (name, mail, tel)values(?, ?, ?)")
        ps.setString(1, data.name)
        ps.setString(2, data.mail)
        ps.setString(3, data.tel)
        ps.executeUpdate()
      }
    } catch{
      case e:SQLException => Ok(views.html.add("フォームを記入して下さい。", form))
    }
    Redirect(routes.HomeController.index)
  }
}
