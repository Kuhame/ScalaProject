package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.json._

class UndirectedGraphSpec extends AnyFlatSpec with Matchers {

  "An UndirectedGraph" should "start empty" in {
    val graph = UndirectedGraph[Int]()
    graph.vertices should be(empty)
    graph.edges should be(empty)
  }

  it should "add and remove edges correctly" in {
    val graph = UndirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    graph.vertices should contain allOf (1, 2, 3)
    graph.edges should contain allOf ((1, 2), (2, 1), (2, 3), (3, 2))

    val updatedGraph = graph.removeEdge(2, 3)
    updatedGraph.edges should not contain (2, 3)
    updatedGraph.edges should not contain (3, 2)
    updatedGraph.edges should contain(1, 2)
    updatedGraph.edges should contain(2, 1)
  }

  it should "return correct neighbors" in {
    val graph = UndirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)

    graph.neighbors(1) should contain allOf (2, 3)
    graph.neighbors(2) should contain allOf (1, 3)
    graph.neighbors(3) should contain allOf (1, 2)
  }

  it should "filter out isolated vertices with empty sets" in {
    val graph = UndirectedGraph[String](
      Map(
        "C" -> Set(),
        "D" -> Set(),
        "B" -> Set("C"),
        "A" -> Set("B")
      )
    )

    graph.vertices should contain allOf ("A", "B", "C")
    graph.vertices should not contain ("D")
  }

  it should "serialize and deserialize correctly" in {
    val graph = UndirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    val json = graph.toJson
    json.fromJson[UndirectedGraph[Int]] match {
      case Right(decodedGraph) =>
        decodedGraph.vertices should contain allOf (1, 2, 3)
        decodedGraph.edges should contain allOf ((1, 2), (2, 1), (2, 3), (3, 2))
      case Left(error) =>
        fail(s"Deserialization failed with error: $error")
    }
  }

  it should "fail deserialization with invalid JSON" in {
    val invalidJson = """{"invalid": "json"}"""
    invalidJson.fromJson[UndirectedGraph[Int]] match {
      case Right(_) =>
        fail("Deserialization should have failed with invalid JSON")
      case Left(_) =>
        succeed
    }
  }
}
