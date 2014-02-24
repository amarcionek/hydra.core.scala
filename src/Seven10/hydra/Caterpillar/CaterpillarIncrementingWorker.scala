/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala._

class CaterpillarIncrementingWorker extends WorkerStrategy {

    override def DoWork(workItem : Work) : Work = {
        
        assert(workItem != null)

        // You have a few obvious choices here. 
        // Explicit cast:       var workItemCIW : CaterpillarWorkInteger = workItem.asInstanceOf[CaterpillarWorkInteger]
        // Pattern Matching:    workItem match { case workItemCIW : CaterpillarWorkInteger =>  ... }
        // Explicit cast will throw ClassCastException on its own, so you need to know to catch it here or upstream.
        // Pattern matching provides more flexibility even if it is more syntactically bulkly.

        workItem match {
            case workItemCIW : CaterpillarWorkInteger => {
                workItemCIW.incrememtValue()    
            }
            case _ => {
                throw new ClassCastException
            }
        }                    
        
        return workItem
    }
}
