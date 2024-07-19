package fr.efrei.scalaproject.graph
import scala.annotation.tailrec
import scala.collection.immutable.Queue

object BFS {
  def bfs[V](graph: Graph[V], start: V): Set[V] = {
    if (!graph.vertices.contains(start)) {
      Set.empty[V]
    } else {
      @tailrec
      def loop(visited: Set[V], queue: Queue[V]): Set[V] = {
        queue.dequeueOption match {
          case None => visited
          case Some((vertex, rest)) =>
            if (visited.contains(vertex)) {
              loop(visited, rest)
            } else {
              val neighbors = graph.neighbors(vertex)
              loop(visited + vertex, rest.enqueueAll(neighbors))
            }
        }
      }

      loop(Set(), Queue(start))
    }
  }
}
