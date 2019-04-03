package client

import cats.effect.{ContextShift, IO, Sync}
import cats.effect.{ExitCode, IOApp}
import cats.implicits._
import io.github.mkows.playground.api.v0.errors._
import org.http4s.client._
import org.http4s.client.blaze._
import org.http4s.Uri
import io.github.mkows.playground.api.v0.{Client => PlaygroundApiClient}

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {
  def run(args: List[String]) =
    ClientApp.go().map(_ => ExitCode.Success)
}


object ClientApp {

  def sampleAppClient[F[_]: Sync](httpClient: Client[F]): PlaygroundApiClient[F] = {
    new PlaygroundApiClient(baseUrl = Uri.uri("http://localhost:8080"), httpClient = httpClient)
  }

  def go()(implicit cs: ContextShift[IO]): IO[Unit] = {
    BlazeClientBuilder[IO](global).resource.use { blazeClient =>
      val client = sampleAppClient(blazeClient)
      for {
        _ <- callHealthchecksById[IO](client, 1)
        _ <- callHealthchecksById[IO](client, 2)
        _ <- callHealthchecksById[IO](client, 3)
        _ <- callHealthchecksById[IO](client, 4)
        _ <- callHealthchecksById[IO](client, 5)
        _ <- callHealthchecksById[IO](client, 6)
        _ <- callHealthchecksById[IO](client, 7)
        _ <- callHealthchecksById[IO](client, 8)
      } yield {
        ()
      }
    }
  }

  private def callHealthchecksById[F[_]: Sync](client: PlaygroundApiClient[F], id: Int): F[Unit] = {
    val res = for {
      res <- client.healthchecks.getById(id)
      _ <- Sync[F].delay { println(s"$id -> response: $res\n") }
    } yield {
      ()
    }

    // TODO extract all info possible
    res.handleErrorWith {
      case errorResponse: ErrorResponse =>
        val details = s"[headers=${errorResponse.headers}, status=${errorResponse.status}, message=${errorResponse.message}, body=${errorResponse.body}, error=${errorResponse.error}]"
        println(s"$id -> error (error response): $errorResponse.\n... Details: $details\n")
        Sync[F].pure(())
      case unitResponse : UnitResponse =>
        println(s"$id -> error (unit response): $unitResponse\n")
        Sync[F].pure(())
      case valueResponse : ValueResponse =>
        val details = s"[headers=${valueResponse.headers}, status=${valueResponse.status}, message=${valueResponse.message}, body=${valueResponse.body}, value=${valueResponse.value}]"
        println(s"$id -> error (value response): $valueResponse.\n... Details: $details\n")
        Sync[F].pure(())
      case fr: FailedRequest =>
        val details = s"[responseCode=${fr.responseCode}, message=${fr.message}]"
        println(s"$id -> error (failed request): $fr.\n... Details: $details\n")
        Sync[F].pure(())
      case error =>
        println(s"$id -> error (other error): $error\n")
        Sync[F].pure(())
    }
  }
}
