/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.Caterpillar

import Seven10.protopipelinescala

object Main {

    /**
     * @param args the command line arguments
     */
    def main(args: Array[String]): Unit = {
        
        println("Hello, world!")
        
        val NUM_CATERPILLAR_SEGMENTS : Int = 10            // Not counting head and tail
        val NUM_WORKER_THREADS_PER_SEGMENT : Int = 10
        val MAX_Q_DEPTH : Int = 10
        val PRODUCTION_COUNT : Int = 10000000

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
