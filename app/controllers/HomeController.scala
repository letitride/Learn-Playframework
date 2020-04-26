package controllers

import java.sql.SQLException

import javax.inject._
import play.api.db.Database
import play.api.mvc._
import anorm._
import PersonForm._
import org.postgresql.util.PSQLException

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(db: Database, cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {

  def index() = Action { implicit request =>
    db.withConnection{ implicit conn =>
      val result = SQL("select * from people")
        .as(personparser.*)
      Ok(views.html.index( "People Data", result ))
    }
  }

  def show(id:Int) = Action{ implicit request =>
    db.withConnection{ implicit conn =>
      val result = SQL("select * from people where id = {id}")
        .on("id" -> id)
        .as(personparser.single)
      Ok(views.html.show("People Data", result))
    }
  }


  def add() = Action{ implicit request =>
    Ok(views.html.add(
      "フォームを記入して下さい。", form
    ))
  }

  def create() = Action{ implicit request =>
    val formData = form.bindFromRequest()
    val data = formData.get
    try {
      db.withConnection { implicit conn =>
        SQL("insert into people (name, mail, tel) values ({name}, {mail}, {tel})")
          .on(
            "name" -> data.name,
            "mail" -> data.mail,
            "tel" -> data.tel
          ).executeInsert()
        Redirect(routes.HomeController.index())
      }
    }catch {
      case e:PSQLException => {
        println(e.getMessage)
        Redirect(routes.HomeController.index())
      }

    }
  }

  def edit(id:Int) = Action{ implicit request =>
    var formdata = personForm.bindFromRequest
    db.withConnection{ implicit conn =>
      val pdata = SQL("select * from people where id = {id}")
        .on("id" -> id)
        .as(personparser.single)
      formdata = personForm.fill(pdata)
      Ok(views.html.edit("フォームを編集して下さい。", formdata, id))
    }
  }

  def update(id:Int) = Action{ implicit request =>
    var formData = form.bindFromRequest
    val data = formData.get
    db.withConnection{ implicit conn =>
      val result = SQL("update people set name={name}, mail={mail}, tel={tel} where id={id}")
        .on(
          "name" -> data.name,
          "mail" -> data.mail,
          "tel" -> data.tel,
          "id" -> id
        ).executeUpdate()
      println(result)
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
