package customer.domain

import com.google.protobuf.empty.Empty
import customer.api
import customer.api.UpdateBalanceRequest
import kalix.scalasdk.valueentity.ValueEntity
import kalix.scalasdk.valueentity.ValueEntityContext
import org.slf4j.{Logger, LoggerFactory}

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Customer(context: ValueEntityContext) extends AbstractCustomer {

  private val log: Logger = LoggerFactory.getLogger(classOf[Customer]);
  override def emptyState: CustomerState = CustomerState()

  override def create(currentState: CustomerState, customer: api.Customer): ValueEntity.Effect[Empty] = {
    val state = convertToDomain(customer)
    effects.updateState(state).thenReply(Empty.defaultInstance)
  }

  def convertToDomain(customer: api.Customer): CustomerState =
    CustomerState(
      customerId = customer.customerId,
      email = customer.email,
      name = customer.name,
      address = customer.address.map(convertToDomain)
    )

  def convertToDomain(address: api.Address): Address =
    Address(
      street = address.street,
      city = address.city
    )

  override def getCustomer(currentState: CustomerState, getCustomerRequest: api.GetCustomerRequest): ValueEntity.Effect[api.Customer] =
    if (currentState.customerId == "") {
      effects.error(s"Customer ${getCustomerRequest.customerId} has not been created.")
    } else {
      effects.reply(convertToApi(currentState))
    }

  def convertToApi(customer: CustomerState): api.Customer =
    api.Customer(
      customerId = customer.customerId,
      email = customer.email,
      name = customer.name,
      balance = customer.balance
    )

  override def updateBalance(currentState: CustomerState, updateBalanceRequest: UpdateBalanceRequest): ValueEntity.Effect[Empty] = {
    val newState = currentState.copy(balance = currentState.balance + updateBalanceRequest.amount)
    effects
      .updateState(newState)
      .thenReply(Empty.defaultInstance)
  }
}

