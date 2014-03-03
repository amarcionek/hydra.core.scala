/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Seven10.hydra.migrator

import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import scala.collection.Traversable
   
class DirectoryTraverser(rootPath : String = ".") extends Traverser with Traversable[(Path, BasicFileAttributes)] {
  
    // Create a Path using the string
    val m_rootPath : Path  = Paths.get(rootPath)
    
    // Do some function on file path
    def doOp[U](f: (Path) => Unit) = {
        class Visitor extends SimpleFileVisitor[Path] {
        override def visitFile(filePath: Path, attrs: BasicFileAttributes): FileVisitResult = {
          f(filePath)
          FileVisitResult.CONTINUE
        } 
      }
    }
    
    // Make foreach receive a function from Tuple2 to Unit
    def foreach[U](f: ((Path, BasicFileAttributes)) => U) {
      class Visitor extends SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = try {
          // Pass a tuple to f
          f(file -> attrs)
          FileVisitResult.CONTINUE
        } catch { 
          case _ : Throwable => FileVisitResult.TERMINATE
        }
      }
      Files.walkFileTree(m_rootPath, new Visitor)
    }
}