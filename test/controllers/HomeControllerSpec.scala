package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  import play.api.db.Databases
  import play.api.db.evolutions._

  val database = Databases.inMemory(
    name = "default",
    urlOptions = Map(
      "MODE" -> "PostgreSQL"
    ),
    config = Map(
      "logStatements" -> true
    )
  )
  //Evolutionsは /conf/evolutions/"dbname" を実行する
  Evolutions.cleanupEvolutions(database)
  Evolutions.applyEvolutions(database)

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController( database, stubMessagesControllerComponents())
      val home = controller.index().apply(FakeRequest(GET, "/").withCSRFToken)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      println(contentAsString(home))
      //contentAsString(home) must include ("Terry")

    }
/*
    "データ送信のテスト" in {
      val request = FakeRequest(POST, "/form").withCSRFToken.withBody(
        Map("name"->Seq("v1"),"pass"->Seq("v2"),"radio"->Seq("v3")))
      //val controller = new HomeController(stubMessagesControllerComponents())
      //val home = controller.form().apply(request)
      val home = route( new GuiceApplicationBuilder().build(), request ).get
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("v1")
    }
    */
  }
}
