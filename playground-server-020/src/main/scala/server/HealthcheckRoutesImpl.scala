package server

import java.time.Instant
import cats.effect.Sync
import io.github.mkows.playground.api.v0.server._
import io.github.mkows.playground.api.v0.models._
import org.http4s._
import org.http4s.headers._

class HealthcheckRoutesImpl[F[_]: Sync] extends HealthcheckRoutes[F] {
  override def get(_req: Request[F]) = ???

  override def postTestFormParamsJson(_req: Request[F], param1: String) = ???

  override def getById(_req: Request[F], id: Int): F[GetByIdResponse] = {
    id match {
      case 1 => Sync[F].pure(GetByIdResponse.HTTP200(Healthcheck(":yay:", Instant.now)))
      case 2 => Sync[F].pure(GetByIdResponse.HTTP400(Error(code=101, message = "no good, no found", details = Some(":sadpanda:"))))
      case 3 => Sync[F].pure(GetByIdResponse.HTTP401(`WWW-Authenticate`(Challenge("Basic", "realm", Map.empty))))
      case 4 => Sync[F].pure(GetByIdResponse.HTTP403("boom-403"))
      case 5 => Sync[F].pure(GetByIdResponse.HTTP404())
      case 6 => Sync[F].pure(GetByIdResponse.UndocumentedResponse(Sync[F].pure(Response(Status.Conflict).withEntity("even different"))))
      case 7 => Sync[F].raiseError(new Exception("Raised error / Failed IO"))
      case _ => throw new Exception("Boom 8+!")
    }
  }

  override def apiVersionMatch(req: org.http4s.Message[F]): Boolean = true
}