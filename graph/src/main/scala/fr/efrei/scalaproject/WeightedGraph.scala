package fr.efrei.scalaproject.graph
import zio.json._

case class WeightedEdge[V](to: V, weight: Int)

object WeightedEdge {
  implicit def encoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[WeightedEdge[V]] =
    DeriveJsonEncoder.gen[WeightedEdge[V]]

  implicit def decoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[WeightedEdge[V]] =
    DeriveJsonDecoder.gen[WeightedEdge[V]]
}

final case class WeightedGraph[V](adjList: Map[V, Set[WeightedEdge[V]]])
    extends Graph[V] {

  def vertices: Set[V] = adjList.keySet ++ adjList.values.flatten.map(_.to)

  def edges: Set[(V, V)] = adjList
    .map {
      case (vertex, edges) => {
        edges.map(edge => (vertex, edge.to))
      }
    }
    .flatten
    .toSet

  def neighbors(vertex: V): Set[V] = {
    val outgoing = adjList.getOrElse(vertex, Set()).map(_.to)
    outgoing
  }

  def addEdge(from: V, to: V): WeightedGraph[V] = {
    addEdge(from, to, 0)
  }

  def addEdge(from: V, to: V, weight: Int): WeightedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()) + WeightedEdge(to, weight)
    new WeightedGraph(adjList + (from -> updatedEdges))
  }

  def removeEdge(from: V, to: V): WeightedGraph[V] = {
    val updatedEdges = adjList.getOrElse(from, Set()).filterNot(_.to == to)
    new WeightedGraph(adjList + (from -> updatedEdges))
  }

  def toDot(): String = {
    val edges = adjList.foldLeft("") { case (acc, (vertex, neighbors)) =>
      acc + neighbors.foldLeft("") { case (acc, neighbor) =>
        acc + s"$vertex -> ${neighbor.to} [label=${neighbor.weight}];\n"
      }
    }
    s"digraph G {\n$edges}"
  }

}

object WeightedGraph {
  def apply[V](): WeightedGraph[V] =
    new WeightedGraph[V](Map.empty[V, Set[WeightedEdge[V]]])

  implicit def encoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[WeightedGraph[V]] =
    DeriveJsonEncoder.gen[WeightedGraph[V]]

  implicit def mapEncoder[V: JsonEncoder: JsonFieldEncoder]
      : JsonEncoder[Map[V, Set[WeightedEdge[V]]]] =
    JsonEncoder.map[V, Set[WeightedEdge[V]]]

  implicit def decoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[WeightedGraph[V]] =
    DeriveJsonDecoder.gen[WeightedGraph[V]]

  implicit def mapDecoder[V: JsonDecoder: JsonFieldDecoder]
      : JsonDecoder[Map[V, Set[WeightedEdge[V]]]] =
    JsonDecoder.map[V, Set[WeightedEdge[V]]]
}
