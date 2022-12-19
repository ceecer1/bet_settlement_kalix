package customer.domain

import com.google.protobuf.Timestamp
import com.google.protobuf.empty.Empty
import customer.api
import kalix.scalasdk.valueentity.ValueEntity
import kalix.scalasdk.valueentity.ValueEntityContext
import org.slf4j.{Logger, LoggerFactory}

import java.time.Instant

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class BetResult(context: ValueEntityContext) extends AbstractBetResult {
  private val log: Logger = LoggerFactory.getLogger(classOf[Customer]);
  override def emptyState: BetResultState = BetResultState()

  override def create(currentState: BetResultState, betResult: api.BetResult): ValueEntity.Effect[Empty] = {
      val state = convertToDomain(betResult)
      effects.updateState(state).thenReply(Empty.defaultInstance)
    }

  def convertToDomain(betResult: api.BetResult): BetResultState =
    BetResultState(
      customerId = betResult.customerId,
      betId = betResult.betId,
      amount = betResult.amount,
      won = betResult.won
    )

  override def getBetResult(currentState: BetResultState, getBetResultRequest: api.GetBetResultRequest): ValueEntity.Effect[api.BetResult] =
    effects.error("The command handler for `GetBetResult` is not implemented, yet")

}

