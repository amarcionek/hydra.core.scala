/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala._

import java.util.Date

import akka.actor.ActorRef

object AkkaCaterpillarPipelineSourceWorkInteger {

  // The case classes define the message protocol that passes between the AkkaCaterpillarPipelineSourceWorkInteger
  case class Generate                       // Start generating
}

class AkkaCaterpillarPipelineSourceWorkInteger(unitsFoodAvail : Long, myLogger : HydraLogger, workerNext : ActorRef) extends AkkaWorker("AkkaCaterpillarPipelineSourceWorkInteger", null, myLogger, workerNext){
    
    m_logger.GetLogger().trace("ctor")
    
    private var m_unitsFoodAvailable : Long = unitsFoodAvail

    def getUnitsFoodAvailable() = {
        m_unitsFoodAvailable
    }

    def isEmpty() : Boolean = {
        (m_unitsFoodAvailable == 0)
    }

    override def receive = {

        case AkkaCaterpillarPipelineSourceWorkInteger.Generate => {

            m_logger.GetLogger().trace("receive() - AkkaCaterpillarPipelineSourceWorkInteger.Generate")

            // While there are units to send, send 'em'
            while (m_unitsFoodAvailable != 0) {

                if (((new Date()).getTime() % 5000) == 0)
                    m_logger.GetLogger().debug("FoodAvail=" + m_unitsFoodAvailable);  // Output a progress indicator (maybe) every ~1000 msecs...

                // Create a newer work item.
                val worker : AkkaCaterpillarWorkInteger = new AkkaCaterpillarWorkInteger
                // Send it with a AkkaWorker.Work msg
                m_workerNext ! AkkaWorker.Work(worker)

                m_unitsFoodAvailable -= 1
                m_unitsWorkPerformed += 1
            }

            m_logger.GetLogger().info("Food supply exhausted - Processed:" + m_unitsWorkPerformed)                

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
