package seleniums

import org.openqa.selenium.WebDriver
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

class IndexSpec extends PlaySpec with GuiceOneServerPerTest with OneBrowserPerSuite with HtmlUnitFactory{

  import org.openqa.selenium.htmlunit.HtmlUnitDriver

  override def createWebDriver(): WebDriver = {
    val driver = new HtmlUnitDriver {
      def setAcceptLanguage(lang: String) =
        this.getWebClient().addRequestHeader("Accept-Language", lang)
    }
    driver.setAcceptLanguage("en")
    driver
  }

  override def fakeApplication(): Application = {
    new GuiceApplicationBuilder()
      .configure(
        "db.default.driver" -> "org.h2.Driver",
        "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
      )
      .build()
  }

  "GET /" should {
    "view表示のテスト" in {
      go to s"http://localhost:$port/"
      assert(pageTitle === "index view")
      println( findAll(className("list-data") ).toSeq )
      //assert(findAll(className("post-body")).length === 0)
    }
  }
/*

  "POST /" should {
    "投稿したものが表示される" in {
      val body = "test post"

      go to s"http://localhost:$port/"
      textField(cssSelector("input#post")).value = body
      submit()

      eventually {
        val posts = findAll(className("post-body")).toSeq
        assert(posts.length === 1)
        assert(posts(0).text === body)
        assert(findAll(cssSelector("p#error")).length === 0)
      }
    }
  }

  "POST /" should {
    "投稿したものが表示される その2" in {
      go to s"http://localhost:$port/"

      eventually {
        val posts = findAll(className("post-body")).toSeq
        assert(posts.length === 1)
      }
    }

    "空のメッセージは投稿できない" in {
      val body = ""

      go to s"http://localhost:$port/"
      textField(cssSelector("input#post")).value = body
      submit()

      eventually {
        val error = findAll(cssSelector("p#error")).toSeq
        assert(error.length === 1)
        assert(error(0).text === "Please enter a message.")
      }
    }

    "長すぎるメッセージは投稿できない" in {
      val body = "too long messages"

      go to s"http://localhost:$port/"
      textField(cssSelector("input#post")).value = body
      submit()

      eventually {
        val error = findAll(cssSelector("p#error")).toSeq
        assert(error.length === 1)
        assert(error(0).text === "The message is too long.")
      }
    }
  }
 */
}
