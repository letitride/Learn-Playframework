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
  def index(p:Option[Int]) = Action { request =>
    val arr = List(
      List("Terry", "Man", "101-0001"),
      List("Mike", "Maki", "785-0001"),
      List("Inami", "San", "001-0001")
    )
    Ok(views.html.index("これはコントローラーで用意したメッセージです", arr, List("Name", "NickName", "ZipCode") ))
  }
}
