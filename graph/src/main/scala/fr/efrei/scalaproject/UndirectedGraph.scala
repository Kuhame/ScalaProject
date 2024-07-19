package fr.efrei.scalaproject.graph
import zio.json._
import zio.json.ast.Json.Str

final case class UndirectedGraph[V](adjList: Map[V, Set[V]]) extends Graph[V] {
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

  def toDot(): String = {
    val edges = adjList.foldLeft("") { case (acc, (vertex, neighbors)) =>
      acc + neighbors.foldLeft("") { case (acc, neighbor) =>
        acc + s"$vertex -- $neighbor;\n"
      }
    }
    s"graph G {\n$edges}"
  }

}

object UndirectedGraph {
  def apply[V](): UndirectedGraph[V] =
    new UndirectedGraph[V](Map.empty[V, Set[V]])

  implicit def encoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[UndirectedGraph[V]] =
    DeriveJsonEncoder.gen[UndirectedGraph[V]]

  implicit def mapEncoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[Map[V, Set[V]]] =
    JsonEncoder.map[V, Set[V]]

  implicit def decoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[UndirectedGraph[V]] =
    DeriveJsonDecoder.gen[UndirectedGraph[V]]

  implicit def mapDecoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[Map[V, Set[V]]] =
    JsonDecoder.map[V, Set[V]]
}
