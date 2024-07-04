package fr.efrei.scalaproject.graph
import zio.json._
final case class DirectedGraph[V](adjList: Map[V, Set[V]]) extends Graph[V] {
  def vertices: Set[V] = adjList.keySet ++ adjList.values.flatten
  def edges: Set[(V, V)] = adjList
    .map {
      case (vertex, edges) => {
        edges.map(edges => (vertex, edges))
      }
    }
    .flatten
    .toSet

  def neighbors(vertex: V): Set[V] = {
    val outgoing = adjList.getOrElse(vertex, Set())
    // Does it takes also incoming edges?
    val incoming = adjList.filter(_._2.contains(vertex)).keySet
    outgoing ++ incoming

  }

  def addEdge(from: V, to: V): DirectedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()) + to
    new DirectedGraph(adjList + (from -> updatedEdges))
  }

  def removeEdge(from: V, to: V): DirectedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()) - to
    new DirectedGraph(adjList + (from -> updatedEdges))
  }
}

object DirectedGraph {
  def apply[V](): DirectedGraph[V] =
    new DirectedGraph[V](Map.empty[V, Set[V]])

  implicit def encoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[DirectedGraph[V]] =
    DeriveJsonEncoder.gen[DirectedGraph[V]]

  implicit def mapEncoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[Map[V, Set[V]]] =
    JsonEncoder.map[V, Set[V]]

  implicit def decoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[DirectedGraph[V]] =
    DeriveJsonDecoder.gen[DirectedGraph[V]]

  implicit def mapDecoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[Map[V, Set[V]]] =
    JsonDecoder.map[V, Set[V]]
}
