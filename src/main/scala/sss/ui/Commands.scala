package sss.ui

import akka.actor.ActorRef
import org.vaadin7.console.Console
import org.vaadin7.console.Console.Command
import sss.ui.RestyConsole.{ReqConnect, ReqDisconnect}

/**
  * Created by alan on 4/19/16.
  */
object Commands {

  class DisconnectCmd(actorRef: ActorRef) extends Command {
    @throws[Exception]
    def execute(console: Console, argv: Array[String]): Object = {

      actorRef ! ReqDisconnect
      s"Disconnecting ..."
    }

    def getUsage(console: Console, argv: Array[String]): String = {
      return "disconnect"
    }
  }

  class ConnectCmd(actorRef: ActorRef) extends Command {
    @throws[Exception]
    def execute(console: Console, argv: Array[String]): Object = {
      val connectStr = if(argv.size == 1) s"localhost:7676"
      else if(argv.size == 2) s"localhost:${argv(1)}"
      else s"${argv(1)}:${argv(2)}"
      actorRef ! ReqConnect(connectStr)
      s"Trying to connect to port '$connectStr' ..."
    }

    def getUsage(console: Console, argv: Array[String]): String = {
      return "connect <port> OR connect <host>:<port>"
    }
  }

  class ClearCmd(actorRef: ActorRef) extends Command {
    @throws[Exception]
    def execute(console: Console, argv: Array[String]): Object = {
      actorRef ! ClearPanel
      s"Ok"
    }

    def getUsage(console: Console, argv: Array[String]): String = {
      return "clear (clears the right hand panel)"
    }
  }
}
