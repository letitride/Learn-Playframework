package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Cookie
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents())
      val home = controller.index(Some("my"), Some("you")).apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      session(home).get("my").getOrElse("") mustBe "you"
    }

    "session送信時の表示のテスト" in {
      val controller = inject[HomeController]
      val home = controller.index(None, None).apply(FakeRequest(GET, "/")
        .withSession("session-key" -> "session1"))
      contentAsString(home) must include ("session1")
    }

    "パラメータなしでindexメソッドを実行のテスト" in {
      val controller = inject[HomeController]
      val home = controller.index(None, None).apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index(Some("you"), None).apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }
  }
}
