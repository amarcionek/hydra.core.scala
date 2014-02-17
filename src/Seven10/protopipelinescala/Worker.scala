/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.protopipelinescala

class Worker (inQ : WorkerQueue, outQ : WorkerQueue, workerStrategy : WorkerStrategy, name : String) extends Runnable {

    assert(inQ != null)
    assert(workerStrategy != null)
        
    // NOTE: Primary constructor params become implicit vals
    val m_inputQueue : WorkerQueue = inQ
    // If outputQ is null then this is a Consumer Process only...
    val m_outputQueue : WorkerQueue = outQ
    val m_workerStrategy : WorkerStrategy = workerStrategy
    var m_workerName : String = name
    val m_workerThread : Thread = new Thread(this, m_workerName)
    
    var m_unitsWorkPerformed : Int = 0 

    // Example of mappings
    val increment = (x: Int) => x + 1
    val decrement = (x: Int) => x - 1
     
    def getInputQueue() : WorkerQueue = {
        m_inputQueue
    }
    
    def getWorkerThread() : Thread = {
        m_workerThread
    }
    def run() {
      
        try
        {
            while (java.lang.Boolean.TRUE) {
                val workItem : Work = m_workerStrategy.DoWork(m_inputQueue.take())
                
                if (m_outputQueue != null)
                    m_outputQueue.put(workItem)
                
                m_unitsWorkPerformed = increment(m_unitsWorkPerformed)
            }
        }
        catch
        {
                case ex: InterruptedException => {
               
                    var stringOut : String = "" + m_workerName + " interrupted - Performed=" + m_unitsWorkPerformed + " MaxInputQueueDepth=" + m_inputQueue.getMaxSize()

                    if (m_outputQueue != null)
                        stringOut += " MaxOutputQueueDepth=" + m_outputQueue.getMaxSize()
                    
                    println(stringOut)
                }
        }
    }
    
}
