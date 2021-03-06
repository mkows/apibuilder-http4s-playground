/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.0.1-dev
 * apibuilder 0.14.75 localhost 9000/michal/playground-api/0.0.1-dev/http4s_0_20
 */
package io.github.mkows.playground.api.v0.server

import org.http4s.dsl.{io => _, _}
import org.http4s.implicits._
import cats.effect._
import cats.implicits._
import io.github.mkows.playground.api.v0.models.json._

private[server] trait Matchers[F[_]] extends Http4sDsl[F] {

  implicit lazy val bigDecimalDateQueryParamDecoder: org.http4s.QueryParamDecoder[BigDecimal] =
    org.http4s.QueryParamDecoder.fromUnsafeCast[BigDecimal](p => BigDecimal(p.value))("BigDecimal")

  implicit lazy val instantQueryParamDecoder: org.http4s.QueryParamDecoder[java.time.Instant] =
    org.http4s.QueryParamDecoder.fromUnsafeCast[java.time.Instant](p => java.time.Instant.parse(p.value))("java.time.Instant")

  implicit lazy val localDateQueryParamDecoder: org.http4s.QueryParamDecoder[java.time.LocalDate] =
    org.http4s.QueryParamDecoder.fromUnsafeCast[java.time.LocalDate](p => java.time.LocalDate.parse(p.value))("java.time.LocalDate")

  implicit lazy val uuidQueryParamDecoder: org.http4s.QueryParamDecoder[java.util.UUID] =
    org.http4s.QueryParamDecoder.fromUnsafeCast[java.util.UUID](p => java.util.UUID.fromString(p.value))("java.util.UUID")


  object ApiVersion {
    val ApiVersionMajor = {
      "X-Apidoc-Version-Major".ci
    }

    def apply(req: org.http4s.Message[F]): Boolean = req.headers.get(ApiVersionMajor) match {
      case Some(v) if v.value == "0" => true
      case _ => false
    }
  }

  object IntVal {
    def unapply(s: String): Option[Int] = scala.util.Try(s.toInt).toOption
  }

}

trait HealthcheckRoutes[F[_]] extends Matchers[F] {

  implicit def circeJsonDecoder[A](implicit decoder: _root_.io.circe.Decoder[A], sync: Sync[F]) = org.http4s.circe.jsonOf[F, A]
  implicit def circeJsonEncoder[A](implicit encoder: _root_.io.circe.Encoder[A], sync: Sync[F]) = org.http4s.circe.jsonEncoderOf[F, A]

  sealed trait GetResponse

  object GetResponse {
    case class HTTP200(value: io.github.mkows.playground.api.v0.models.Healthcheck, headers: Seq[org.http4s.Header] = Nil) extends GetResponse
    case class UndocumentedResponse(response: F[org.http4s.Response[F]]) extends GetResponse
  }

  def get(
    _req: org.http4s.Request[F]
  ): F[GetResponse]

  case class PostTestFormParamsJsonRequest(param1: String)

  implicit val PostTestFormParamsJsonRequestDecoder: _root_.io.circe.Decoder[PostTestFormParamsJsonRequest] = _root_.io.circe.Decoder.instance { a =>
    for {
      param1 <- a.downField("param1").as[String]
    } yield {
      PostTestFormParamsJsonRequest(
        param1 = param1
      )
    }
  }

  sealed trait PostTestFormParamsJsonResponse

  object PostTestFormParamsJsonResponse {
    case class HTTP204(headers: Seq[org.http4s.Header] = Nil) extends PostTestFormParamsJsonResponse
    case class UndocumentedResponse(response: F[org.http4s.Response[F]]) extends PostTestFormParamsJsonResponse
  }

  def postTestFormParamsJson(
    _req: org.http4s.Request[F],
    param1: String
  ): F[PostTestFormParamsJsonResponse]

  sealed trait GetByIdResponse

  object GetByIdResponse {
    case class HTTP200(value: io.github.mkows.playground.api.v0.models.Healthcheck, headers: Seq[org.http4s.Header] = Nil) extends GetByIdResponse
    case class HTTP400(value: io.github.mkows.playground.api.v0.models.Error, headers: Seq[org.http4s.Header] = Nil) extends GetByIdResponse
    case class HTTP401(authenticate: org.http4s.headers.`WWW-Authenticate`, headers: Seq[org.http4s.Header] = Nil) extends GetByIdResponse
    case class HTTP403(value: String, headers: Seq[org.http4s.Header] = Nil) extends GetByIdResponse
    case class HTTP404(headers: Seq[org.http4s.Header] = Nil) extends GetByIdResponse
    case class UndocumentedResponse(response: F[org.http4s.Response[F]]) extends GetByIdResponse
  }

  def getById(
    _req: org.http4s.Request[F],
    id: Int
  ): F[GetByIdResponse]

  def apiVersionMatch(req: org.http4s.Message[F]): Boolean = ApiVersion(req)

  def service()(implicit sync: Sync[F]) = org.http4s.HttpRoutes.of[F] {
    case _req @ GET -> Root / "_internal_" / "healthcheck" if apiVersionMatch(_req) =>
      get(_req).flatMap {
        case GetResponse.HTTP200(value, headers) => Ok(value, headers: _*)
        case GetResponse.UndocumentedResponse(response) => response
      }
    case _req @ GET -> Root / "_internal_" / "healthcheck" if !_req.headers.get(ApiVersion.ApiVersionMajor).isDefined =>
      BadRequest(s"Missing required request header: ${ApiVersion.ApiVersionMajor}.")

    case _req @ POST -> Root / "_internal_" / "healthcheck" / "test_form_params_json" if apiVersionMatch(_req) =>
    if (_req.contentType.exists(_.mediaType == _root_.org.http4s.MediaType.application.json)) {
      _req.attemptAs[PostTestFormParamsJsonRequest].value.flatMap{
        case Right(req) =>
          postTestFormParamsJson(_req, req.param1).flatMap {
            case PostTestFormParamsJsonResponse.HTTP204(headers) => NoContent(headers: _*)
            case PostTestFormParamsJsonResponse.UndocumentedResponse(response) => response
          }
        case Left(_) => BadRequest()
      }
    } else {
        _req.decode[_root_.org.http4s.UrlForm] {
          req =>
            val responseOpt = for {
              param1 <- req.getFirst("param1")
            } yield {
                postTestFormParamsJson(_req, param1).flatMap {
                  case PostTestFormParamsJsonResponse.HTTP204(headers) => NoContent(headers: _*)
                  case PostTestFormParamsJsonResponse.UndocumentedResponse(response) => response
                }
              }
            responseOpt.getOrElse(BadRequest())
      }
    }
    case _req @ POST -> Root / "_internal_" / "healthcheck" / "test_form_params_json" if !_req.headers.get(ApiVersion.ApiVersionMajor).isDefined =>
      BadRequest(s"Missing required request header: ${ApiVersion.ApiVersionMajor}.")

    case _req @ GET -> Root / "_internal_" / "healthcheck" / IntVal(id) if apiVersionMatch(_req) =>
      getById(_req, id).flatMap {
        case GetByIdResponse.HTTP200(value, headers) => Ok(value, headers: _*)
        case GetByIdResponse.HTTP400(value, headers) => BadRequest(value, headers: _*)
        case GetByIdResponse.HTTP401(authenticate, headers) => Unauthorized(authenticate, headers: _*)
        case GetByIdResponse.HTTP403(value, headers) => Forbidden(value, headers: _*)
        case GetByIdResponse.HTTP404(headers) => NotFound(headers: _*)
        case GetByIdResponse.UndocumentedResponse(response) => response
      }
    case _req @ GET -> Root / "_internal_" / "healthcheck" / IntVal(id) if !_req.headers.get(ApiVersion.ApiVersionMajor).isDefined =>
      BadRequest(s"Missing required request header: ${ApiVersion.ApiVersionMajor}.")
  }
}

     