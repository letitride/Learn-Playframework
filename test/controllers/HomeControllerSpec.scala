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
      val home = controller.index(Some("my")).apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      println( cookies(home).get("name").getOrElse(Cookie("name", "no-send")).value )
      cookies(home).get("name").getOrElse(Cookie("name", "no-send")).value mustBe "my"
      contentAsString(home) must include ("nameがおくられました")
    }

    "cookie送信時の表示のテスト" in {
      val controller = inject[HomeController]
      val home = controller.index(None).apply(FakeRequest(GET, "/").withCookies(Cookie("name", "cookiedata")) )
      contentAsString(home) must include ("cookiedata")
    }

    "パラメータなしでindexメソッドを実行のテスト" in {
      val controller = inject[HomeController]
      val home = controller.index(None).apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      //cookies(home).get("name").get mustBe None
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("nameはありません")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index(Some("you")).apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Hello")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Hello")
    }
  }
}
