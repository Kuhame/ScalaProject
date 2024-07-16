package fr.efrei.scalaproject.graph
import scala.annotation.tailrec

object DetectCycle {
  def hasCycle[V](graph: Graph[V], start: V): Boolean = {
    @tailrec
    def loop(visited: Set[V], stack: List[V], recStack: Set[V]): Boolean = {
      stack match {
        case Nil => false
        case vertex :: rest =>
          if (recStack.contains(vertex)) {
            // A cycle is detected if the vertex is already in the recursion stack
            true
          } else if (visited.contains(vertex)) {
            loop(visited, rest, recStack)
          } else {
            val neighbors = graph.neighbors(vertex)
            loop(visited + vertex, neighbors.toList ++ rest, recStack + vertex)
          }
      }
    }

    loop(Set(), List(start), Set())
  }
}