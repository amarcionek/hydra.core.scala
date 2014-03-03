/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala

object MainAkka {

    /**
     * @param args the command line arguments
     */
    def main(args: Array[String]): Unit = {
                      
        val NUM_CATERPILLAR_SEGMENTS : Int = 10            // Not counting head and tail
        val PRODUCTION_COUNT : Int = 1000000
       
        val pipeline : AkkaCaterpillar = new AkkaCaterpillar(NUM_CATERPILLAR_SEGMENTS, PRODUCTION_COUNT)

        pipeline.start()

        pipeline.stop()        
    }
}
