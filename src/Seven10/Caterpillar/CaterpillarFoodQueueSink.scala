/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.Caterpillar

import Seven10.protopipelinescala._

class CaterpillarFoodQueueSink (size : Int, val m_foodValueExpected : Int) extends WorkerQueue(size) {

    var m_unitsWorkDecomposed : Int = 0
    
     def getUnitsWorkDecomposed() = {
        m_unitsWorkDecomposed
    }
 
     def getExpectedWorkValue() = {
        m_foodValueExpected
    }
    
    def put(workItem : CaterpillarWorkInteger) {

        if (workItem.getValue() != m_foodValueExpected) {
            println(Thread.currentThread().getName() 
                    + ":CaterpillarManureQ.put():Unexpected work value " + workItem.getValue() 
                    + ", expected " + m_foodValueExpected)
            Thread.currentThread().interrupt()
        }
        m_unitsWorkDecomposed -= 1
    }
    
}
