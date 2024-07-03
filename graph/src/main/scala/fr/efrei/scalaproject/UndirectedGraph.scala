package fr.efrei.scalaproject.graph

final case class UndirectedGraph[V](adjList: Map[V, Set[V]]) extends Graph[V] {
  def vertices: Set[V] = adjList.keySet ++ adjList.values.flatten

  def edges: Set[(V, V)] = adjList
    .map {
      case (vertex, edges) => {
        edges.map(edges => (vertex, edges))
      }
    }
    .flatten
    .toSet

  def neighbors(vertex: V): Set[V] = adjList.getOrElse(vertex, Set())

  def addEdge(from: V, to: V): UndirectedGraph[V] = {
    val updatedEdgesFrom = adjList.getOrElse(from, Set()) + to
    val updatedEdgesTo = adjList.getOrElse(to, Set()) + from
    new UndirectedGraph(
      adjList + (from -> updatedEdgesFrom) + (to -> updatedEdgesTo)
    )
  }

  def removeEdge(from: V, to: V): UndirectedGraph[V] = {
    val updatedEdgesFrom = adjList.getOrElse(from, Set()) - to
    val updatedEdgesTo = adjList.getOrElse(to, Set()) - from
    new UndirectedGraph(
      adjList + (from -> updatedEdgesFrom) + (to -> updatedEdgesTo)
    )
  }

}

object UndirectedGraph {
  def apply[V](): UndirectedGraph[V] =
    new UndirectedGraph[V](Map.empty[V, Set[V]])
}
