import zio.http.*
import zio.http.model.*
import zio.test.*

object MainSpec extends ZIOSpecDefault {

  val homeApp = HomeApp()
  val greetApp = GreetingApp()

  def spec =
    suite("App Tests")(
      suite("Main backend application")(
        test("should show start message") {
          for {
            _ <- ZioHttpApp.console
            output <- TestConsole.output
          } yield assertTrue(output.head contains "started")
        },
        test("root route should redirect to /greet") {
          for {
            response <- homeApp(Request.get(URL(!!)))
            body <- response.body.asString
          } yield assertTrue(
            response.status == Status.TemporaryRedirect,
            response.headers == Headers.location("/greet"),
            body.isEmpty
          )
        }
      ),
      suite("Greet backend application")(
        test("should greet world") {
          for {
            response <- greetApp(Request.get(URL(!! / "greet")))
            body <- response.body.asString
          } yield assertTrue(
            response.status == Status.Ok,
            body == "Hello World!"
          )
        },
        test("should greet User if using path") {
          for {
            response <- greetApp(Request.get(URL(!! / "greet" / "User")))
            body <- response.body.asString
          } yield assertTrue(
            response.status == Status.Ok,
            body == "Hello User!"
          )
        },
        test("should greet User if using param") {
          for {
            response <- greetApp(
              Request.get(URL(!! / "greet").setQueryParams("?name=User"))
            )
            body <- response.body.asString
          } yield assertTrue(
            response.status == Status.Ok,
            body == "Hello User!"
          )
        }
      )
    )
}
