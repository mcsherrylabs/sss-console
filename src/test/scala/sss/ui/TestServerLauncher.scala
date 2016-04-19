package sss.ui


import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.scalatra.ScalatraServlet
import spray.json._
/**
 * Created by alan on 12/8/15.
 */


class TestServerLauncher(port: Int, contextPath: String) {

  val server: Server = new Server(port)
  private val context: ServletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)

  context.setContextPath(contextPath)

  server.setHandler(context)

  context.addServlet(classOf[MyTestServlet], "/*")
  server.start

  def join = server.join()
}

object TestServerLauncher {

  def main(args: Array[String]) {

    val port = args.find(_.startsWith("port=")).map(_.substring(5).toInt).getOrElse(7676)
    val cPath = args.find(_.startsWith("contextPath=")).map(_.substring(12)).getOrElse("/")
    val server = new TestServerLauncher(port, cPath)
    server.join
  }
}

class MyTestServlet extends ScalatraServlet {

  case class Command(name: String, help: String)

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val commandFormat = jsonFormat2(Command)

  }

  import MyJsonProtocol._

  val cmds = (0 to 10).indices map(i => Command(s"cmd$i", s"help $i"))

  get("/commands") {
    val txt = cmds.toJson
    txt
  }

  get("/command/*") {

    val txt = multiParams("splat").toJson
    txt
  }
}