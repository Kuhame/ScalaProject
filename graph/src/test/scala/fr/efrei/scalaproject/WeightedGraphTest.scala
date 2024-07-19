package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.json._

class WeightedGraphSpec extends AnyFlatSpec with Matchers {

  "A WeightedGraph" should "start empty" in {
    val graph = WeightedGraph[String]()
    graph.vertices should be(empty)
    graph.edges should be(empty)
  }

  it should "add and remove edges correctly" in {
    val graph = WeightedGraph[String]()
      .addEdge("A", "B", 5)
      .addEdge("B", "C", 10)

    graph.vertices should contain allOf ("A", "B", "C")
    graph.edges should contain allOf (("A", "B"), ("B", "C"))

    val updatedGraph = graph.removeEdge("B", "C")
    updatedGraph.edges should not contain ("B", "C")
    updatedGraph.edges should contain("A", "B")
  }

  it should "return correct outgoing neighbors" in {
    val graph = WeightedGraph[String]()
      .addEdge("A", "B", 5)
      .addEdge("B", "C", 10)
      .addEdge("C", "A", 15)

    graph.neighbors("A") should contain only "B"
    graph.neighbors("B") should contain only "C"
    graph.neighbors("C") should contain only "A"
  }

  it should "filter out isolated vertices with empty sets" in {
    val graph = WeightedGraph[String](
      Map(
        "C" -> Set(),
        "D" -> Set(),
        "B" -> Set(WeightedEdge("C", 2)),
        "A" -> Set(WeightedEdge("B", 3))
      )
    )

    graph.vertices should contain allOf ("A", "B", "C")
    graph.vertices should not contain ("D")
  }

  it should "serialize and deserialize correctly" in {
    val graph = WeightedGraph[String]()
      .addEdge("A", "B", 5)
      .addEdge("B", "C", 10)

    val json = graph.toJson
    json.fromJson[WeightedGraph[String]] match {
      case Right(decodedGraph) =>
        decodedGraph.vertices should contain allOf ("A", "B", "C")
        decodedGraph.edges should contain allOf (("A", "B"), ("B", "C"))
      case Left(error) =>
        fail(s"Deserialization failed with error: $error")
    }
  }

  it should "fail deserialization with invalid JSON" in {
    val invalidJson = """{"invalid": "json"}"""
    invalidJson.fromJson[WeightedGraph[String]] match {
      case Right(_) =>
        fail("Deserialization should have failed with invalid JSON")
      case Left(_) =>
        succeed
    }
  }
}
