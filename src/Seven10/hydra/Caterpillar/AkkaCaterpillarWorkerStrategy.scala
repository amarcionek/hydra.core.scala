/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala.AkkaWorkerStrategy
import Seven10.hydra.core.scala.AbstractWork

class AkkaCaterpillarWorkerStrategy extends AkkaWorkerStrategy {
    
    override def DoWork(workItem : AbstractWork) : AbstractWork = {
        
        assert(workItem != null)

        // You have a few obvious choices here. 
        // Explicit cast:       var workItemCIW : CaterpillarWorkInteger = workItem.asInstanceOf[CaterpillarWorkInteger]
        // Pattern Matching:    workItem match { case workItemCIW : CaterpillarWorkInteger =>  ... }
        // Explicit cast will throw ClassCastException on its own, so you need to know to catch it here or upstream.
        // Pattern matching provides more flexibility even if it is more syntactically bulkly.

        workItem match {
            case workItem : AkkaCaterpillarWorkInteger => {
                workItem.incrememtValue()    
            }
            case _ => {
                throw new ClassCastException
            }
        }                    

        return workItem
    }
}
