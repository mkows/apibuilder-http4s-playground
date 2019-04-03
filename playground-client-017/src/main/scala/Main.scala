import io.github.mkows.playground.api.v0.{Client => PlaygroundApiClient}
import io.github.mkows.playground.api.v0.errors.{ErrorResponse, FailedRequest, UnitResponse, ValueResponse}
import org.http4s.Uri

object Main extends App {

  println("Running")
  val id = 1
  val healthchecks = new PlaygroundApiClient(baseUrl = Uri.uri("http://localhost:8080")).healthchecks

  for (id <- 1 to 8) {
    go(id)
  }

  def go(id: Int): Unit = {
    val res = healthchecks.getById(id)

    res.handle {
      case errorResponse: ErrorResponse =>
        val details = s"[response=${errorResponse.response}, message=${errorResponse.message}, error=${errorResponse.error.unsafeRun()}]"
        println(s"$id -> error (error response): $errorResponse.\n... Details: $details\n")
      case unitResponse : UnitResponse =>
        println(s"$id -> error (unit response): $unitResponse\n")
      case valueResponse : ValueResponse =>
        val details = s"[response=${valueResponse.response}, message=${valueResponse.message}, value=${valueResponse.value.unsafeRun()}]"
        println(s"$id -> error (value response): $valueResponse.\n... Details: $details\n")
      case fr: FailedRequest =>
        val details = s"[responseCode=${fr.responseCode}, message=${fr.message}]"
        println(s"$id -> error (failed request): $fr.\n... Details: $details\n")
      case error =>
        println(s"$id -> error (other error): $error\n")
    }.unsafeRun()
  }
}
