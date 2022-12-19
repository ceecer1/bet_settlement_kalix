package customer.action

import com.google.protobuf.empty.Empty
import kalix.scalasdk.action.Action
import kalix.scalasdk.action.ActionCreationContext
import akka.actor.ActorRef
import customer.actors.AMQPConsumer.Start
import customer.api.Customer
import kalix.scalasdk.DeferredCall

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class BetSettlementAction(creationContext: ActionCreationContext,
                          actorRef: ActorRef)
  extends AbstractBetSettlementAction {

  override def startIngestion(empty: Empty): Action.Effect[Empty] = {

    // deferred call create customer function to pass to actor
    def customerCreateFn: Customer => DeferredCall[Customer, Empty] = (customer: Customer) => {
      components.customer.create(customer)
    }

    //val reply: Future[NewCartCreated] =
    //      for {
    //        created <- components.shoppingCart.create(CreateCart(cartId)).execute()
    //        populated <- components.shoppingCart.addItem(AddLineItem(cartId, "e", "eggplant", 1)).execute()
    //      } yield NewCartCreated(cartId)

    for {
      get <- components.customer.updateBalance()
    }

    //    Start ingestion and return quickly
    actorRef ! Start(customerCreateFn)
    effects.reply(Empty.defaultInstance)
  }
}

