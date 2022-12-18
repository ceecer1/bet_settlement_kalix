package customer.actors

import akka.actor.{Actor, Props}
import akka.event.Logging

object MyActor {
  def props(): Props = Props(new MyActor)
}

class MyActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "test" => log.info("received bet result")
    case _      => log.info("received unknown message")
  }
}
