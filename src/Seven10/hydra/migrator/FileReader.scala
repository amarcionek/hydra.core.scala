/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.migrator

import java.io._
import java.nio._
import java.nio.file._
import java.nio.channels._

class FileReader(path : String) extends Reader {

    val m_path : Path  = Paths.get(path)
    
    val m_inSeekableChannel : SeekableByteChannel = Files.newByteChannel(m_path)
    
    val strEncoding : String = System.getProperty("file.encoding")
    
    def setPostition(lPos : Long) {
        m_inSeekableChannel.position(lPos)
    }
    
    def getPosition() : Long = {
        m_inSeekableChannel.position
    }
    
    def readBytes(buffer : ByteBuffer) {
        // Rewinding just in case
        buffer.rewind 
        try {
            m_inSeekableChannel.read(buffer)
        }
        catch {
            case ex : IOException => {
                    
            }
            case _ : Throwable => {
                    
            }
        }
    }
}
