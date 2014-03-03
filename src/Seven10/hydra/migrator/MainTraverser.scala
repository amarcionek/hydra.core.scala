/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.migrator

import Seven10.hydra.core.scala._

import ch.qos.logback.classic.Level

import java.nio._
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

object MainTraverser {

    val usage = """"
        Usage: [--path path]
    """
    
    /**
     * @param args the command line arguments
     */
    def main(args: Array[String]): Unit = {
        
        // Logger
        val m_logger : HydraLogger = new HydraLogger(this.getClass().getName(), Level.DEBUG)
 
        m_logger.GetLogger().trace("ctor")
        
        if (args.length == 0) {
            println(usage)
            return
        }              
      
        val directory = new DirectoryTraverser()
        
        // Apply the foreach method
        directory foreach {
            // use case to seamlessly deconstruct the tuple
            case (filePath : Path, attr : BasicFileAttributes) => {
                m_logger.GetLogger.info("Found: " + filePath 
                                        + " - Create:" + attr.creationTime()
                                        + " - Size:" + attr.size())
            }
        }
       
        var lTotalDataRead : Long = 0
        var lTotalFilesRead : Long = 0
                
        // Apply the foreach method
        directory foreach {
              
            // use case to seamlessly deconstruct the tuple
            case (filePath : Path, attr : BasicFileAttributes) => {
                    
                lTotalFilesRead += 1
                
                val reader : FileReader = new FileReader(filePath.toString)
                
                val buffer : ByteBuffer = ByteBuffer.allocate(4096)
                
                val end : Long  = buffer.capacity
                while (reader.getPosition < attr.size) {
                    reader.readBytes(buffer)
                    lTotalDataRead += buffer.position
                }
            }
        }

        m_logger.GetLogger.info("Total Files: " + lTotalFilesRead 
                                 + " Total Data: " + lTotalDataRead)
    }        
}
