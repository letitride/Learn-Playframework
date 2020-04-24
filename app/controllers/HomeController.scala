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
          msg += "<li  class=\"list-data\">" + rs.getInt("id") + ":" + rs.getString("name") + "</li>"
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

  def edit(id:Int) = Action{ implicit request =>
    var formData = form.bindFromRequest
    try {
      db.withConnection{ conn =>
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("select * from people where id =" + id)
        rs.next
        val name = rs.getString("name")
        val mail = rs.getString("mail")
        val tel = rs.getString("tel")
        val data = Data(name, mail, tel)
        formData = form.fill(data)
      }
    } catch {
      case e:SQLException => Redirect(routes.HomeController.index())
    }
    Ok(views.html.edit("フォームを編集して下さい。", formData, id))
  }

  def update(id:Int) = Action{ implicit request =>
    var formData = form.bindFromRequest
    val data = formData.get
    try
      db.withConnection{ conn =>
        val ps = conn.prepareStatement("update people set name=?, mail=?, tel=? where id=?")
        ps.setString(1, data.name)
        ps.setString(2, data.mail)
        ps.setString(3, data.tel)
        ps.setInt(4, id)
        ps.executeUpdate()
      }
    catch {
      case e: SQLException => Ok(views.html.add("フォームに入力して下さい。", form))
    }
    Redirect(routes.HomeController.index())
  }

  def delete(id:Int) = Action{ implicit request =>
    var pdata:Data = null
    try {
      db.withConnection{ conn =>
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("select * from people where id=" + id)
        rs.next()
        val name = rs.getString("name")
        val mail = rs.getString("mail")
        val tel = rs.getString("tel")
        pdata = Data(name, mail, tel)
      }
    } catch {
      case e:SQLException => Redirect(routes.HomeController.index())
    }
    Ok(views.html.delete("このレコードを削除します。", pdata, id))
  }

  def remove(id:Int) = Action{ implicit request =>
    try
      db.withConnection{ conn =>
        val ps = conn.prepareStatement("delete from people where id=?")
        ps.setInt(1, id)
        ps.executeUpdate()
      }
    catch {
      case e:SQLException => Redirect(routes.HomeController.index())
    }
    Redirect(routes.HomeController.index())
  }
}
