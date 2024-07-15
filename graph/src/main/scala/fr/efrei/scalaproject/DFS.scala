package fr.efrei.scalaproject.graph
import scala.annotation.tailrec

object DFS {
  def dfs[V](graph: Graph[V], start: V): Set[V] = {
    @tailrec
    def loop(visited: Set[V], stack: List[V]): Set[V] = {
      stack match {
        case Nil => visited
        case vertex :: rest =>
          if (visited.contains(vertex)) {
            loop(visited, rest)
          } else {
            val neighbors = graph.neighbors(vertex)
            loop(visited + vertex, neighbors.toList ++ rest)
          }
      }
    }

    loop(Set(), List(start))
  }
}
