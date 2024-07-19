package fr.efrei.scalaproject.graph

import scala.collection.mutable

object Dijkstra {
  def dijkstra(graph: Map[Int, List[(Int, Int)]], source: Int): Map[Int, Int] = {
    val distances = mutable.Map[Int, Int]() withDefaultValue Int.MaxValue
    val priorityQueue = mutable.PriorityQueue[(Int, Int)]()(Ordering.by(-_._2)) 
    distances(source) = 0
    priorityQueue.enqueue((source, 0))

    while (priorityQueue.nonEmpty) {
      val (u, distU) = priorityQueue.dequeue()

      if (distU <= distances(u)) {
        for ((v, weight) <- graph.getOrElse(u, List())) {
          val newDist = distU + weight
          
          if (newDist < distances(v)) {
            distances(v) = newDist
            priorityQueue.enqueue((v, newDist))
          }
        }
      }
    }
    
    graph.keys.foreach { node =>
      if (!distances.contains(node)) distances(node) = Int.MaxValue
    }

    distances.toMap
  }
}
