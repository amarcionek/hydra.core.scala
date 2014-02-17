/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.protopipelinescala

// Scala does not have a blocking queue. The package scala.collection.mutable.SynchronizedQueue throws an exception on an empty queue
// We could certainly build one in pure scala, but not right now. There are public ones available however.
import java.util.concurrent.LinkedBlockingQueue;

class WorkerQueue(val thisSize : Long) extends LinkedBlockingQueue[Work] {

    val increment = (x: Long) => x + 1
    val decrement = (x: Long) => x - 1
    
    private var m_numBlockedOnTake : Long = 0
    private var m_numBlockedOnPut : Long = 0

    def getMaxSize () : Long = { // NOTE: Int is optional
        this.thisSize
    }
    
    def getNumBlockedOnPut() = {
        m_numBlockedOnPut
    }
 
     def getNumBlockedOnTake() = {
        m_numBlockedOnTake
    }

    override def take() : Work = {
        m_numBlockedOnTake = increment(m_numBlockedOnTake)
        val workItem : Work = super.take()
        m_numBlockedOnTake = decrement(m_numBlockedOnTake)
        return workItem
    }
        
    override def put(workItem : Work) {
        m_numBlockedOnPut = increment(m_numBlockedOnPut)
        super.put(workItem)
        m_numBlockedOnPut = decrement(m_numBlockedOnPut)
    }
}
