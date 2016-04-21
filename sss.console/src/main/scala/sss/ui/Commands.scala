package sss.ui

import akka.actor.ActorRef
import org.vaadin7.console.Console
import org.vaadin7.console.Console.Command
import sss.ui.RestyConsole.{ReqConnect, ReqDisconnect}

/**
  * Created by alan on 4/19/16.
  */
object Commands {

  object NullCmd extends Command {
    @throws[Exception]
    def execute(console: Console, argv: Array[String]): Object = "No such command"
    def getUsage(console: Console, argv: Array[String]): String = "This is the null command"
  }


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
      val connectStr = if(argv.size == 1) s"localhost:7676/console"
      else s"localhost:${argv(1)}"

      actorRef ! ReqConnect(connectStr)
      s"Trying to connect to port '$connectStr' ..."
    }

    def getUsage(console: Console, argv: Array[String]): String = {
      return "connect <port> OR connect <host><port> OR connect <host><port><contextpath>"
    }
  }

  class ClearCmd(actorRef: ActorRef) extends Command {
    @throws[Exception]
    def execute(console: Console, argv: Array[String]): Object = {
      if(argv.size > 1 && "rhs".compareToIgnoreCase(argv(1)) == 0) actorRef ! ClearPanel
      else console.clear
      s"Ok"
    }

    def getUsage(console: Console, argv: Array[String]): String = {
      return "clear [rhs] - rhs will clear the right hand side."
    }
  }
}
