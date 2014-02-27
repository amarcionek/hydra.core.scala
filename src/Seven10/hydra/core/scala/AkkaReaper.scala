/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.core.scala

import akka.actor.{Actor, ActorRef, Terminated}
import scala.collection.mutable.ArrayBuffer
 
object AkkaReaper {
  // Used by others to register an Actor for watching
  case class WatchMe(ref: ActorRef)
}
 
abstract class AkkaReaper(myLogger : HydraLogger) extends Actor {  
  import AkkaReaper._
    
 val m_logger = myLogger
 
 m_logger.GetLogger().trace("ctor")
    
  // Keep track of what we're watching
  val watched = ArrayBuffer.empty[ActorRef]
 
  // Derivations need to implement this method.  It's the
  // hook that's called when everything's dead
  def allSoulsReaped(): Unit
 
  // Watch and check for termination
  final def receive = {
      
    case WatchMe(ref) =>  {
      m_logger.GetLogger().trace("receive() - WatchMe - " + ref.toString)
      
      context.watch(ref)
      watched += ref
    }
    
    case Terminated(ref) => {
      m_logger.GetLogger().trace("receive() - Terminated - " + ref.toString)
      watched -= ref
      if (watched.isEmpty)
          allSoulsReaped()
    }
    
  }
  
}
