/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.Caterpillar

import Seven10.protopipelinescala._
import java.util.LinkedList

class CaterpillarPipeline(m_numCaterpillarSegements : Int, m_numThreadsPerSegment : Int, m_iMaxQueueDepth : Int, m_iMaxFoodCount : Int ) {

   // Linked list for all workers
    var m_workerSegmentList : LinkedList[Worker] = new LinkedList[Worker]
   
    var m_foodQueue : CaterpillarFoodQueueSource = new CaterpillarFoodQueueSource(m_iMaxQueueDepth, m_iMaxFoodCount)
    var m_poopQueue : CaterpillarFoodQueueSink = new CaterpillarFoodQueueSink(m_iMaxQueueDepth, m_numCaterpillarSegements + 2)

    println("CaterpillarPipeline(numCaterpillarSegments="
                + m_numCaterpillarSegements
                + ", numThreadsPerSegment" + m_numThreadsPerSegment
                + ", maxQueueDepth=" + m_iMaxQueueDepth
                + ", foodCount=" + m_iMaxFoodCount + ")");
    
    // Construct pipeline from tail to head
    
    // Create a single threaded tail segment that outputs to the sink Q.
    var inputQ : WorkerQueue  = new WorkerQueue(m_iMaxQueueDepth)
    var outputQ : WorkerQueue = m_poopQueue
    
    addCaterpillarSegment(inputQ, outputQ, "CaterpillarTail")
    
    // Example for comprehension
    for (i <- m_numCaterpillarSegements until 0 by -1)
    {
        outputQ = inputQ
        inputQ = new WorkerQueue(m_iMaxQueueDepth)
        
        for (j <- 0 until m_numThreadsPerSegment)
        {
            val strThreadName : String = new String("CaterpillarSegment#" + (i) + " - Thread#" + (j + 1))            
            addCaterpillarSegment(inputQ, outputQ, strThreadName)
        }
    }
    
    outputQ = inputQ
    addCaterpillarSegment(m_foodQueue, outputQ, "CaterpillarHead")
    
    private def addCaterpillarSegment(inputQ : WorkerQueue,
                                  outputQ : WorkerQueue, 
                                  name : String) {
        val strategy : WorkerStrategy  = new CaterpillarIncrementingWorker
        val worker : Worker = new Worker(inputQ, outputQ, strategy, name)        
        m_workerSegmentList.addFirst(worker);
    }
    
    def start(){
       println("CaterpillarPipeline.start()")
       val itr : java.util.Iterator[Worker] = m_workerSegmentList.descendingIterator()
       while (itr.hasNext()) {
            val worker : Worker = itr.next()
            worker.getWorkerThread().start()
        }
    }
    
    def stop(){
       println("CaterpillarPipeline.stop()")
       val itr : java.util.Iterator[Worker] = m_workerSegmentList.iterator()
       while (itr.hasNext()) {
            val worker : Worker = itr.next()
            worker.getWorkerThread().interrupt()
        }
    }
    
    def destruct() {
        println("CaterpillarPipeline.destruct()")
        m_workerSegmentList.clear()
    }
    
    def waitForPipelineToDrain() {
        println("CaterpillarPipeline.waitForPipelineToDrain()")
        
        println("CaterpillarPipeline.Waiting for head to be interrupted()")
        val worker : Worker = m_workerSegmentList.getFirst()
        worker.getWorkerThread().join()
        
        var prevInputQ : WorkerQueue = null
        
        val itr : java.util.Iterator[Worker] = m_workerSegmentList.iterator()
        while (itr.hasNext()) {
            val worker : Worker = itr.next()
            val inputQ : WorkerQueue = worker.getInputQueue()
            
            // All worker threads in a segment have the SAME input queue; suppress redundancy
            if (inputQ != prevInputQ) { 
                println("CaterpillarPipeline.waitForPipelineToDrain(): Waiting for " 
                    + worker.m_workerName + "'s inputQ to empty...");

                while (!inputQ.isEmpty) {
                        println("worker.getInputQueue().size(): " + inputQ.size)
                        Thread.sleep(1000);        // Sleep 100 mSecs and re-check
                }
            }
            prevInputQ = inputQ;
            
         }                    
    }
}
