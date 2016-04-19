package sss.ui

import java.util

import akka.actor.ActorRef
import com.vaadin.ui.UI
import org.vaadin7.console.Console
import org.vaadin7.console.Console.{Command, CommandProvider}
import sss.ancillary.Logging
import sss.ui.reactor.UIEventActor
import us.monoid.web.Resty

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

/**
  * Created by alan on 4/19/16.
  */
object RestyConsole {


  case object ReqDisconnect
  case class Req(strs: String*)
  case class ReqConnect(connectStr: String)
  case object ListCommands

  class RequestActor(console: Console) extends UIEventActor {

    val resty = new Resty

    def react(reactor: ActorRef, broadcaster: ActorRef, ui: UI) = {

      case ListCommands => console.getCommands.foreach(broadcaster ! Feedback(_))

      case cp@ ClearPanel => broadcaster ! cp

      case ReqConnect(connectionStr) =>
        Try(resty.json(s"http://$connectionStr/commands")) match {
          case Failure(e) =>
            broadcaster ! Feedback(s"Problem connecting to $connectionStr")
            broadcaster ! Feedback(s"${e.getMessage}")
          case Success(a) =>
            console.removeAllCommandProviders()
            console.addCommandProvider(new RestyCommandProvider(broadcaster, connectionStr, resty))
            broadcaster ! Feedback(s"Connected to $connectionStr")
            self ! ListCommands
        }

      case x => broadcaster ! Feedback(s"Sorry, don't know -> $x")
    }
  }

  class RestyCommandProvider(broadcast: ActorRef,connectionStr: String, resty: Resty) extends CommandProvider {

    private val urlStr = s"http://$connectionStr"
    val cmds = {
      val json = resty.json(s"$urlStr/commands")
      val ary = json.array()
      (0 to ary.length - 1).map { i =>
        val cmd = ary.getJSONObject(i)
        val cmdName = cmd.getString("name")
        (cmdName -> new RestyCommand(broadcast, cmdName, urlStr, resty, cmd.getString("help")))
      }.toMap
    }


    override def getCommand(console: Console, commandName: String): Command = cmds(commandName)

    override def getAvailableCommands(console: Console): util.Set[String] = cmds.keys.toSet[String]
  }

  class RestyCommand(broadcast: ActorRef, cmdName: String, urlStr: String, resty: Resty, helpStr: String) extends Command with Logging {
    override def execute(console: Console, argv: Array[String]): AnyRef = {

      Try (resty.json(s"$urlStr/command/$cmdName?${argv.mkString("&")}")) match {
        case Failure(e) =>
          broadcast ! Feedback(s"$urlStr Transport error executing command.")
          log.warn(s"Transport error $urlStr", e)
          e.getMessage
        case Success(json) =>
          val ary = json.array()
          (0 to ary.length - 1).map { i =>
            broadcast ! Feedback(ary.getString(i))
          }
          "Ok"
      }
    }

    override def getUsage(console: Console, argv: Array[String]): String = helpStr
  }
}
