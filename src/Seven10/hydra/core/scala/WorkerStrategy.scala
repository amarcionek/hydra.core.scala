/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.core.scala

trait WorkerStrategy {
    def DoWork(worktItem : Work) : Work = {
        return worktItem
    }
}
