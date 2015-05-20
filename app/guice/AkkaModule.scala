package guice

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Injector, Provider}
import com.typesafe.config.{Config, ConfigFactory}
import controllers.Cluster
import guice.AkkaModule.ActorSystemProvider
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.ScalaModule

object AkkaModule {
  class ActorSystemProvider @Inject() (val injector: Injector) extends Provider[ActorSystem] {
    override def get() = {
      val name:String  = injector.instance[String](Names.named("name"))
      val config:Config = injector.instance[Config]
      val system = ActorSystem(name, config)
      system
    }
  }
}

/**
 * A module providing an Akka ActorSystem.
 */
class AkkaModule extends AbstractModule with ScalaModule {

  override def configure() {
    bind[String].annotatedWith(Names.named("name")).toInstance("HajpCluster")
    bind[Config].toInstance(ConfigFactory.load().getConfig("HajpCluster"))
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()

    bind[Cluster.type].toInstance(Cluster)
  }
}
