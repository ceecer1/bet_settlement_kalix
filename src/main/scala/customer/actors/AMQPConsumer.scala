package customer.actors

import akka.NotUsed
import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.stream.Materializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, NamedQueueSourceSettings, QueueDeclaration, ReadResult}
import akka.stream.scaladsl.{Sink, Source}
import com.google.protobuf.empty.Empty
import customer.action.AMQPBetResult
import customer.api.Customer
import kalix.scalasdk.DeferredCall
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read

object AMQPConsumer {
  def props(): Props = Props(new AMQPConsumer)

  case class Start(call: Customer => DeferredCall[Customer, Empty])
  case object Stop
}

class AMQPConsumer extends Actor {
  val log = Logging(context.system, this)
  import AMQPConsumer._
  val connectionProvider = AmqpLocalConnectionProvider
  implicit val formats = Serialization.formats(NoTypeHints)
  implicit val mat: Materializer = Materializer(context.system)

  def receive = {
    case Start(call) =>
      log.info("Starting data ingestion from AMQP source")
      val queueName = "test-queue"
      val queueDeclaration = QueueDeclaration(queueName)

      val amqpSource: Source[ReadResult, NotUsed] =
        AmqpSource.atMostOnceSource(
          NamedQueueSourceSettings(connectionProvider, queueName)
            .withDeclaration(queueDeclaration)
            .withAckRequired(false),
          bufferSize = 10
        )
      amqpSource
        .map({ x => read[Customer](x.bytes.utf8String) })
        .map { customer =>
          log.info("Customer read from AMQP queue: " + customer)
          call(customer).execute()
        }
        .runWith(Sink.ignore)

    case _      =>
      log.info("received unknown message")
  }
}
