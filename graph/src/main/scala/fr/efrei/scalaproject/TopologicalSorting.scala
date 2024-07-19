package fr.efrei.scalaproject.graph
import DFS._
import DetectCycle._
import scala.annotation.tailrec

object TopologicalSorting {
  def topologicalSort[V](graph: Graph[V]): Option[List[V]] = {
    // Use DFS to detect cycles
    val allVertices = graph.vertices.toList
    val hasCycleDetected = allVertices.exists(vertex => DetectCycle.hasCycle(graph, vertex))
    
    // If a cycle is detected, topological sorting is not possible
    if (hasCycleDetected) {
      None
    } else {
      // Perform DFS to get the topological order
      def dfsVisit(node: V, visited: Set[V], stack: List[V]): (Set[V], List[V]) = {
        if (visited.contains(node)) {
          (visited, stack)
        } else {
          val (newVisited, newStack) = graph.neighbors(node).foldLeft((visited + node, stack)) {
            case ((vis, stk), neighbor) =>
              val (nextVis, nextStk) = dfsVisit(neighbor, vis, stk)
              (nextVis, nextStk)
          }
          (newVisited, node :: newStack)
        }
      }

      val (visited, stack) = allVertices.foldLeft((Set[V](), List[V]())) {
        case ((visited, stack), vertex) =>
          if (visited.contains(vertex)) {
            (visited, stack)
          } else {
            val (newVisited, newStack) = dfsVisit(vertex, visited, stack)
            (newVisited, newStack)
          }
      }
      
      Some(stack.reverse)
    }
  }
}