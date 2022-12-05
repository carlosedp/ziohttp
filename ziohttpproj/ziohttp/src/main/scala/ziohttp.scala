import zio.*
import zio.http.*
import zio.http.model.Method
import zio.http.ServerConfig.LeakDetectionLevel
import zio.http.middleware.Cors.CorsConfig

object ZioHttpApp extends ZIOAppDefault {
  val PORT = 8080
  // Create CORS configuration
  val corsConfig = CorsConfig(
    allowedOrigins = _ == "*",
    allowedMethods =
      Some(Set(Method.PUT, Method.DELETE, Method.POST, Method.GET))
  )

  // Add route managers and middleware
  val httpProg = HomeApp() ++ GreetingApp() @@ Middleware.cors(
    corsConfig
  ) @@ Middleware.debug

  // Server config
  val config =
    ServerConfig.default
      .port(PORT)
      .leakDetection(LeakDetectionLevel.PARANOID)
      .maxThreads(2)

  val server =
    Server.serve(httpProg).provide(ServerConfig.live(config), Server.live)

  val console = Console.printLine(
    s"Server started on http://localhost:${PORT}"
  )
  def run = console *> server
}

object HomeApp {
  def apply(): Http[Any, Nothing, Request, Response] =
    Http.collect[Request] {
      // GET /, redirect to /greet
      case Method.GET -> !! =>
        Response.redirect("/greet")
    }
}

object GreetingApp {
  def apply(): Http[Any, Nothing, Request, Response] =
    Http.collect[Request] {
      // GET /greet?name=:name
      case req @ (Method.GET -> !! / "greet") if req.url.queryParams.nonEmpty =>
        Response.text(
          s"Hello ${req.url.queryParams("name").mkString(" and ")}!"
        )

      // GET /greet/:name
      case Method.GET -> !! / "greet" / name =>
        Response.text(s"Hello $name!")

      // GET /greet
      case Method.GET -> !! / "greet" =>
        Response.text("Hello World!")
    }
}
