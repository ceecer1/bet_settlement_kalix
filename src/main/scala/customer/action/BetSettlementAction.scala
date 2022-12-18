package customer.action

import akka.NotUsed
import com.google.protobuf.empty.Empty
import kalix.scalasdk.action.Action
import kalix.scalasdk.action.ActionCreationContext
import akka.actor.ActorRef
import akka.stream.Materializer
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.alpakka.amqp.{AmqpLocalConnectionProvider, NamedQueueSourceSettings, QueueDeclaration, ReadResult}
import akka.stream.scaladsl.{Sink, Source}
import customer.models.CustomerCreated
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class BetSettlementAction(creationContext: ActionCreationContext,
                          actorRef: ActorRef)(implicit mat: Materializer)
  extends AbstractBetSettlementAction {

  val connectionProvider = AmqpLocalConnectionProvider
  val queueName = "test-queue"
  val queueDeclaration = QueueDeclaration(queueName)
  implicit val formats = Serialization.formats(NoTypeHints)

  override def startIngestion(empty: Empty): Action.Effect[Empty] = {
    val amqpSource: Source[ReadResult, NotUsed] =
      AmqpSource.atMostOnceSource(
        NamedQueueSourceSettings(connectionProvider, queueName)
          .withDeclaration(queueDeclaration)
          .withAckRequired(false),
        bufferSize = 10
      )

    effects.asyncEffect {
      amqpSource
        .map({ x => read[AMQPBetResult](x.bytes.utf8String) })
        .map(println)
        .runWith(Sink.ignore)
        .map(_ => effects.ignore)
        .recover(_ => effects.error("Failed to ingest amqp data"))
    }

  }
}

