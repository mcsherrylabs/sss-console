package sss.ui.console.util

import org.scalatra.ScalatraServlet
import spray.json._

import scala.util.{Failure, Success, Try}

/**
  * Copyright Stepping Stone Software Ltd. 2016, all rights reserved. 
  * mcsherrylabs on 3/9/16.
  */
trait Cmd {
  val help:String = ""
  def apply(params: Seq[String]) : Seq[String]
}

case class CommandDescription(name: String, help: String)


object JsonProtocol extends DefaultJsonProtocol {
  implicit val commandFormat = jsonFormat2(CommandDescription)
}

trait ConsoleServlet extends ScalatraServlet {

  import JsonProtocol._

  val cmds: Map[String, Cmd]

  get("/ping") {"pong!"}

  get("/commands") {
    cmds.map(entry => CommandDescription(entry._1, entry._2.help)).toJson
  }

  get("/command") {
    Try[Seq[String]] {
      val allParams = params.toSeq.sortBy(_._1).map(_._2)

      cmds(allParams.head)(allParams.tail)
    } match {
      case Failure(e) => Seq("command failed", e.getMessage).toJson
      case Success(s) => s.toJson
    }
  }

}
