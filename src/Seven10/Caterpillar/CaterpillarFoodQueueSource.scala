/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.Caterpillar

import Seven10.protopipelinescala._
import java.util.Date;

class CaterpillarFoodQueueSource (iMaxQueueSize : Int, unitsFoodAvail : Long) extends WorkerQueue(iMaxQueueSize) {

    private var m_unitsFoodAvailable : Long = unitsFoodAvail
    private var m_unitsFoodConsumed : Long = 0  
    
    def getUnitsFoodConsumed() = {
        m_unitsFoodConsumed
    }
 
     def getUnitsFoodAvailable() = {
        m_unitsFoodAvailable
    }
    
    override def isEmpty() : Boolean = {
        (m_unitsFoodAvailable == 0)
    }
  
    override def put(workItem : Work) {
        assert(java.lang.Boolean.FALSE); 
    }
    
    override def take() : Work = {
        if (m_unitsFoodAvailable == 0) {
            println(":CaterpillarFoodQueue().take():Food supply exhausted, "
                    + m_unitsFoodConsumed + " unitsFoodConsumed; throwing InterruptedException!");  
            throw new InterruptedException();
        } else {
            if (((new Date()).getTime() % 5000) == 0)
                println("FoodAvail=" + m_unitsFoodAvailable);  // Output a progress indicator (maybe) every ~1000 msecs...
            m_unitsFoodAvailable -= 1
            m_unitsFoodConsumed += 1
            return new CaterpillarWorkInteger()
        }
    }
}
