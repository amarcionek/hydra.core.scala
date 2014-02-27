/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala.AkkaReaper
import Seven10.hydra.core.scala.HydraLogger

class AkkaCaterpillarReaper(myLogger : HydraLogger) extends AkkaReaper(myLogger) {
    
  m_logger.GetLogger().trace("ctor")
    
  // Shutdown
  def allSoulsReaped(): Unit = { 
      
      m_logger.GetLogger().trace("allSoulsReaped")
      
      m_logger.GetLogger().debug("shutting down system")
         
      context.system.shutdown()      
  }
}