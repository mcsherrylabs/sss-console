package sss.ui


import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler

import sss.ui.console.util.{Cmd, ConsoleServlet}
/**
 * Created by alan on 12/8/15.
 */


class TestServerLauncher(port: Int, contextPath: String) {

  val server: Server = new Server(port)
  private val context: ServletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)

  context.setContextPath(contextPath)

  server.setHandler(context)

  context.addServlet(classOf[MyTestServlet], "/console/*")
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

class MyTestServlet extends ConsoleServlet {

  val cmds = ((0 to 10).indices map(i => s"cmd$i" -> new Cmd {
    override def help: String = s"cmd$i"
    def apply(params: Seq[String]): Seq[String] = params ++ Seq("<- params")
  })).toMap

}