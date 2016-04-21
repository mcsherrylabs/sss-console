package sss.ui

import akka.actor.{Actor, ActorRef, Props}
import com.vaadin.ui._

import sss.ui.reactor._

case class Feedback(msg: String) extends Event { val category = FeedbackPanel.category }
case object ClearPanel extends Event { val category = FeedbackPanel.category }

object FeedbackPanel {

  val category = "sss.ui.feedback"

  def apply(uiReactor: UIReactor): Component = {

    val textArea = new TextArea
    val rhsLayout = new VerticalLayout
    rhsLayout.setSizeFull

    textArea.setWordwrap(true)
    textArea.setSizeFull


    rhsLayout.addComponent(textArea)

    object TextAreaReactor extends UIEventActor {
      override def react(reactor: ActorRef, broadcaster: ActorRef, ui: UI): Actor.Receive = {
        case ClearPanel => push(textArea.setValue(""))
        case Feedback(msg) => push(textArea.setValue(s"${textArea.getValue}\n${msg}"))
      }
    }

    val textAreaActor = uiReactor actorOf (Props(TextAreaReactor))
    textAreaActor ! Register(FeedbackPanel.category)

    rhsLayout
  }
}
