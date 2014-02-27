/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.core.scala

import akka.actor.Actor
import akka.actor.ActorRef
//import akka.actor.Props

object AkkaWorker {
 
   /* 2014/02/25 - ADM - Doc says this is the prefered way to define an AkkaWorker factory, but I can't get it to compile.
  def props(workerName: String, workerStrategy : AkkaWorkerStrategy, myLogger : HydraLogger): Props =
      Props(classOf[AkkaWorker], workerName, workerStrategy, myLogger)
  */
 
  // The case classes define the message protocol that passes between the AkkaWorker
  case class Work(workItem : AbstractWork)  // Perform a unit of work.
  case class Done                           // Stop the actor.
}

class AkkaWorker(val m_workerName : String, val m_workerStrategy : AkkaWorkerStrategy, val m_logger : HydraLogger, val m_workerNext : ActorRef) extends Actor {

    // Units of work performed
    var m_unitsWorkPerformed : Int = 0     
    
    m_logger.GetLogger().trace({ m_workerName } + "started" )        
    
    def doneHandler  {
        
        m_logger.GetLogger().trace("receive() - AkkaWorker.Done")
                
        m_logger.GetLogger().info({m_workerName} + " interrupted - Performed:" + { m_unitsWorkPerformed })
            
        // Cascade our done messages
        if (m_workerNext != null)
            m_workerNext ! AkkaWorker.Done
        
        context.stop(self)        
    }
    
    def receive = {
                
        case AkkaWorker.Work(work) => {
            
            m_logger.GetLogger().trace("receive() - AkkaWorker.Work - " + {m_workerName})
            
            val workItem : AbstractWork = m_workerStrategy.DoWork(work)

            // Send the work to the next worker
            m_workerNext ! AkkaWorker.Work(workItem)
              
            m_unitsWorkPerformed = m_unitsWorkPerformed + 1
        }
        case AkkaWorker.Done => {
                
            doneHandler                               
        }    
        
        // Catch all
        case _ => {
            m_logger.GetLogger().error("receive() - received unknown message - " + {m_workerName})
        }
    }
}
