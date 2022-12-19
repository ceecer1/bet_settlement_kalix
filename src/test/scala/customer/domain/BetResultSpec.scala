package customer.domain

import com.google.protobuf.empty.Empty
import customer.api
import kalix.scalasdk.testkit.ValueEntityResult
import kalix.scalasdk.valueentity.ValueEntity
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BetResultSpec
    extends AnyWordSpec
    with Matchers {

  "BetResult" must {

    "have example test that can be removed" in {
      val service = BetResultTestKit(new BetResult(_))
      pending
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = service.someOperation(SomeRequest)
      // verify the reply
      // val reply = result.getReply()
      // reply shouldBe expectedReply
      // verify the final state after the command
      // service.currentState() shouldBe expectedState
    }

    "handle command Create" in {
      val service = BetResultTestKit(new BetResult(_))
      pending
      // val result = service.create(api.BetResult(...))
    }

    "handle command GetBetResult" in {
      val service = BetResultTestKit(new BetResult(_))
      pending
      // val result = service.getBetResult(api.GetBetResultRequest(...))
    }

  }
}
