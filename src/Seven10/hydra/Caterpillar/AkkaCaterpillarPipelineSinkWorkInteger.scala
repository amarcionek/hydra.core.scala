/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala._

import akka.actor.ActorRef

/**
 *
 * @author 
 * @
 */

class AkkaCaterpillarPipelineSinkWorkInteger(val m_foodValueExpected : Int, myLogger : HydraLogger, workerNext : ActorRef) extends AkkaWorker("AkkaCaterpillarPipelineSinkWorkInteger", null, myLogger, workerNext) {
    
    m_logger.GetLogger().trace("ctor")
    
    override def receive = {
        
        case AkkaWorker.Work(work) => {
            
            m_logger.GetLogger().trace("receive() - AkkaWorker.Work")
            
            work match {
                case workItem : AkkaCaterpillarWorkInteger => {
                                        
                    if (workItem.getValue() != m_foodValueExpected) {
                        m_logger.GetLogger().error("AkkaCaterpillarPipelineSink.receive():Unexpected work value:" + workItem.getValue() 
                                                    + " Expected:" + m_foodValueExpected)            
                    }
                }
                case _ => {
                    throw new ClassCastException
                }
            }                               

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