package customer

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import customer.action.BetSettlementAction
import customer.actors.AMQPConsumer
import customer.domain.Customer
import customer.view.CustomerByNameView
import kalix.scalasdk.Kalix
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

object Main {

  private val log = LoggerFactory.getLogger("customer.Main")

  val system = ActorSystem("BetSettlement")
  implicit val mat: Materializer = Materializer(system)
  implicit val ec: ExecutionContext = system.dispatcher

  def createKalix(actorRef: ActorRef): Kalix = {
    // The KalixFactory automatically registers any generated Actions, Views or Entities,
    // and is kept up-to-date with any changes in your protobuf definitions.
    // If you prefer, you may remove this and manually register these components in a
    // `Kalix()` instance.
    KalixFactory.withComponents(
      new Customer(_),
      new BetSettlementAction(_, actorRef),
      new CustomerByNameView(_)
    )
  }

  def main(args: Array[String]): Unit = {
    val queueConsumer = system.actorOf(AMQPConsumer.props())

    log.info("starting the Kalix service")
    createKalix(queueConsumer).start()
  }
}
