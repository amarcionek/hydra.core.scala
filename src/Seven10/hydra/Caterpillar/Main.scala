/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.Caterpillar

import Seven10.hydra.core.scala


object Main {

    /**
     * @param args the command line arguments
     */
    def main(args: Array[String]): Unit = {
        
        println("Hello, world!")
        
        val NUM_CATERPILLAR_SEGMENTS : Int = 1            // Not counting head and tail
        val NUM_WORKER_THREADS_PER_SEGMENT : Int = 1
        val MAX_Q_DEPTH : Int = 10
        val PRODUCTION_COUNT : Int = 100
       
        val pipeline : CaterpillarPipeline = new CaterpillarPipeline(NUM_CATERPILLAR_SEGMENTS,
                NUM_WORKER_THREADS_PER_SEGMENT,
                MAX_Q_DEPTH,
                PRODUCTION_COUNT)

        pipeline.start()
        pipeline.waitForPipelineToDrain()
        pipeline.stop()
        pipeline.destruct()
    }
}
