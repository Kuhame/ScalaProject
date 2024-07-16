package fr.efrei.scalaproject.graph
import scala.annotation.tailrec
import scala.collection.immutable.Queue

object BFS {
  def bfs[V](graph: Graph[V], start: V): Set[V] = {
    @tailrec
    def loop(visited: Set[V], queue: Queue[V]): Set[V] = {
      if (queue.isEmpty) {
        visited
      } else {
        val (vertex, rest) = queue.dequeue
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
