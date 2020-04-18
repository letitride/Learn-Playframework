package controllers

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
  def index(name:Option[String]) = Action { request =>

    val param = name.getOrElse("")
    val message:String = name match {
      case Some(value) =>  "<p>nameがおくられました。</p>"
      case None => "<p>nameはありません</p>"
    }

    val cookie = request.cookies.get("name")
    val resMessage = message + "<p>cookie: " + cookie.getOrElse(Cookie("name", "no-cookie.")).value + "</p>"
    val res = Ok( "<meta charset='utf-8'><title>Hello!</title><h1>Hello</h1>" + resMessage ).as("text/html")
    if (param != "") {
      res.withCookies(Cookie("name", param)).bakeCookies()
    }else{
      res
    }
  }
}
