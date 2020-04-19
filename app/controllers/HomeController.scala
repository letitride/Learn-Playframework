package controllers

import java.util.Calendar

import javax.inject._
import play.api.mvc._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { request =>
    Ok(views.html.index("これはコントローラーで用意したメッセージです" ))
  }

  def form() = Action{ request =>
    val form:Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    val param = form.getOrElse(Map())
    val name = param.get("name").get(0)
    val pass = param.get("pass").get(0)
    Ok(views.html.index( "name:" + name + ", password:" + pass ))
  }
}
