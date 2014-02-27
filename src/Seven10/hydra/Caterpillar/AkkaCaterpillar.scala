/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala._

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ActorSystem
import akka.actor.InvalidActorNameException

import scala.collection.mutable.ListBuffer

import ch.qos.logback.classic.Level

/**
 *
 * @author 
 * @
 */

class AkkaCaterpillar(m_numCaterpillarSegements : Int, m_iMaxFoodCount : Int ) {

    // Logger
    val m_logger : HydraLogger = new HydraLogger(this.getClass().getName(), Level.DEBUG)
 
    m_logger.GetLogger().trace("ctor")
    
     // Holds known workers/actors
    var m_workers = ListBuffer.empty[ActorRef]
       
    var m_tallWorkReceived: Boolean = false
    
    // ActorSystem is a heavy object: create only one per application
    val m_system = ActorSystem("localhost")

    // This is a Reaper for detecting when every Actor is finished
    var m_reaper : ActorRef = null
    
    // This is a source
    var m_source : ActorRef = null
        
    // This is a sink
    var m_sink : ActorRef = null
    
    var workerNext : ActorRef = null
        
    // Define a strategy. I think we want to change to WorkAlgorithm
    def strategy : AkkaCaterpillarWorkerStrategy = new AkkaCaterpillarWorkerStrategy
    
    try {
        
        // Create the reaper
        m_reaper = m_system.actorOf(Props(new AkkaCaterpillarReaper(m_logger)), "AkkaCaterpillarReaper")
        
        // Create a Sink
        m_sink = m_system.actorOf(Props(new AkkaCaterpillarPipelineSinkWorkInteger(m_numCaterpillarSegements, m_logger, null)), "AkkaCaterpillarPipelineSinkWorkInteger")
        
        // Gives a reference to an Actor's next Actor. Since types differ, we have to asInstanceOf. 
        workerNext = m_sink.asInstanceOf[ActorRef]        
       
        // Tell the reaper to watch this reference
        m_reaper ! AkkaReaper.WatchMe(workerNext)
        
        // Append
        m_workers += workerNext
        
        // For comprehension: Start at tail and work down to zero decremented 1 at a time
        for (i <- m_numCaterpillarSegements until 0 by -1)
        {
            // CODENOTE: For some reason, I had to assign a val to the passed in ActorRef of Props below. I do not know why.
            val workerPrev = workerNext
            
            val worker : ActorRef = m_system.actorOf(Props(new AkkaWorker("AkkaWorker" + i, strategy.asInstanceOf[AkkaWorkerStrategy], m_logger, workerPrev)), "AkkaWorker" + i)

            // Append
            m_workers += worker
            
            // Tell the reaper to watch this reference
            m_reaper ! AkkaReaper.WatchMe(worker)
            
            // Recurse
            workerNext = worker         
        }
        
         // Create a Source
        m_source = m_system.actorOf(Props(new AkkaCaterpillarPipelineSourceWorkInteger(m_iMaxFoodCount, m_logger, workerNext)), "AkkaCaterpillarPipelineSourceWorkInteger")
        
        // Tell the reaper to watch this reference
        m_reaper ! AkkaReaper.WatchMe(m_source)
        
        // Append
        m_workers += m_source
        
    }
    
    catch {
        case ex: InvalidActorNameException =>  {
            m_logger.GetLogger.error("ctor - InvalidActorName: Props[AkkaWorker]")
        }        
    }

    def start() {
        
        m_logger.GetLogger().trace("start()")
           
        // The source generates until all are done. Then it sends done messages.
        m_source ! AkkaCaterpillarPipelineSourceWorkInteger.Generate
    }

    def stop() {
        
        m_logger.GetLogger().trace("stop()")
        
        // Tell the source to shutdown
        m_source ! AkkaWorker.Done     
    }
}
