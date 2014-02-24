/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.core.scala


import org.slf4j.LoggerFactory
import org.slf4j.Logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter

/**
 *
 * @author Adam
 * Note that with logback, you cannot extend those classes because they are declared final.
 */
class HydraLogger(stringClassName : String, newLevel : ch.qos.logback.classic.Level) {
    
    // logback classic logger
    val m_baselogger : org.slf4j.Logger = LoggerFactory.getLogger(stringClassName)
    val m_logger : ch.qos.logback.classic.Logger = m_baselogger.asInstanceOf[ch.qos.logback.classic.Logger]
    
    // The logger level is set on the underlying implementation, which IMHO, sucks.
    // The following statement only used for the SimpleLogger implementation.
    // System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, logLevel);
    m_logger.setLevel(newLevel)
         
    val m_loggerFactory : org.slf4j.ILoggerFactory  = LoggerFactory.getILoggerFactory()
        
    val m_myloggerContext : ch.qos.logback.classic.LoggerContext = m_loggerFactory.asInstanceOf[ch.qos.logback.classic.LoggerContext]
         
    // print internal state
    StatusPrinter.print(m_myloggerContext)
  
    def Stop() {     
        m_myloggerContext.stop();        
    }
    
    def GetLogger() : ch.qos.logback.classic.Logger = {
        return m_logger
    }
}
