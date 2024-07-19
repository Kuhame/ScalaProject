package fr.efrei.scalaproject.graph
import zio.json._
final case class DirectedGraph[V](adjList: Map[V, Set[V]]) extends Graph[V] {
  def vertices: Set[V] = {
    val referencedVertices = adjList.values.flatten.toSet
    val verticesWithEdges = adjList.filter { case (vertex, edges) =>
      edges.nonEmpty || referencedVertices.contains(vertex)
    }.keySet
    referencedVertices ++ verticesWithEdges
  }
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
    outgoing
  }

  def addEdge(from: V, to: V): DirectedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()) + to
    new DirectedGraph(adjList + (from -> updatedEdges))
  }

  def removeEdge(from: V, to: V): DirectedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()) - to
    new DirectedGraph(adjList + (from -> updatedEdges))
  }

  def toDot(): String = {
    val edges = adjList.foldLeft("") { case (acc, (vertex, neighbors)) =>
      acc + neighbors.foldLeft("") { case (acc, neighbor) =>
        acc + s"$vertex -> $neighbor;\n"
      }
    }
    s"digraph G {\n$edges}"
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
