package sss.ui


import akka.actor.Props
import com.vaadin.annotations.{Push, Theme, Title}
import com.vaadin.server.VaadinRequest
import com.vaadin.ui._
import org.vaadin7.console.Console
import sss.ui.Commands._
import sss.ui.RestyConsole.{ListCommands, RequestActor}
import sss.ui.reactor.UIReactor


@Theme("sss-console")
@Title("sss console")
@SuppressWarnings(Array("serial"))
@Push
class ConsoleUI extends UI {

  val uiReactor = UIReactor(this)

  protected def init(request: VaadinRequest) {
    getPage.setTitle("sss console")
    val tabs = new TabSheet
    setContent(tabs)
    tabs.addTab(new ConsoleTab(uiReactor, tabs), "console")

  }

}

class ConsoleTab(uiReactor: UIReactor, tabs: TabSheet) extends HorizontalLayout {

  val console = new Console
  val req = uiReactor.actorOf(Props(classOf[RequestActor], console))

  setMargin(false)
  //setSizeFull


  val lhs = new VerticalLayout
  lhs.setWidth("50%")
  val rhs = FeedbackPanel(uiReactor)
  addComponents(lhs, rhs)


  console.setImmediate(true)
  lhs.addComponent(console)
  console.setPs("> ")
  console.setCols(100)
  console.setRows(80)
  console.setWrap(true)
  console.setSizeFull
  console.setMaxBufferSize(50)
  console.setGreeting("Welcome to sss console")
  console.reset
  console.focus

  val helpCommand: Console.Command = new Console.Command() {
    def getUsage(console: Console, argv: Array[String]): String = argv(0) + " <command>"

    def execute(console: Console, argv: Array[String]): Object = {
      if (argv.length == 2) {
        val hc: Console.Command = console.getCommand(argv(1))
        "Usage: " + hc.getUsage(console, argv.tail)
      } else  listAvailableCommands
    }
  }
  console.addCommand("help", helpCommand)
  console.addCommand("info", helpCommand)
  console.addCommand("man", helpCommand)

  console.addCommand("nametab", new NameTabCmd(tabs))
  console.addCommand("closetab", new CloseTabCmd(tabs))
  console.addCommand("newtab", new NewTabCmd(uiReactor, tabs))
  console.addCommand("connect", new ConnectCmd(req))
  console.addCommand("clear", new ClearCmd(req))


  def listAvailableCommands: String = {
    req ! ListCommands
    "--->"
  }
}