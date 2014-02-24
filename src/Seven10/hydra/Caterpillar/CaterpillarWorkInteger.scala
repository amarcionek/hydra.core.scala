/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala.Work

class CaterpillarWorkInteger extends Work {

    var m_myValue : Int = 0
    
    def setValue(value : Int) {
        m_myValue = value
    }
    
    def getValue() : Int = {
        m_myValue
    }
    
    // : Int = is optional due to type inference
    def incrememtValue() : Int = {
        m_myValue += 1
        return m_myValue
    }
}
