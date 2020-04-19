package controllers

import java.util.Calendar

import javax.inject._
import play.api.mvc._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  import MyForm._
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request =>
    Ok(views.html.index("これはコントローラーで用意したメッセージです", myForm ))
  }

  def form() = Action{ implicit request =>
    val form = myForm.bindFromRequest
    val data = form.get
    Ok(views.html.index( "name:" + data.name + ", password:" + data.pass, form ))
  }
}
