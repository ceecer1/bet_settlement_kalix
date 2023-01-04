package customer

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import customer.action.BetSettlementAction
import customer.actors.AMQPConsumer
import customer.domain.BetResult
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

  def createKalix(): Kalix = {
    KalixFactory.withComponents(
      new BetResult(_),
      new Customer(_),
      ctx => new BetSettlementAction(ctx, ctx.materializer().system.actorOf(AMQPConsumer.props())),
      new CustomerByNameView(_),
    )
  }

  def main(args: Array[String]): Unit = {

    log.info("starting the Kalix service")
    createKalix().start()
  }
}
