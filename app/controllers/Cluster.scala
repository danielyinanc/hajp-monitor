package controllers

import javax.inject.{Singleton, Inject}

import actors._
import akka.actor.ActorSystem
import models.Metrics
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc._

object Cluster extends Controller {
  @Inject var system : ActorSystem = null

  def clusterNodesWebsocket = WebSocket.acceptWithActor[JsValue, JsValue] { implicit request =>
    MonitorActor.props(_, system)
  }

  def clusterMetricsWebsocket = WebSocket.acceptWithActor[JsValue, Metrics.NodeMetric] { implicit request =>
    MetricsActor.props(_, system)
  }
}

@Singleton
class Cluster extends Controller {

}
