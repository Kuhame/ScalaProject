package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.json._

class DirectedGraphSpec extends AnyFlatSpec with Matchers {

  "A DirectedGraph" should "start empty" in {
    val graph = DirectedGraph[Int]()
    graph.vertices should be(empty)
    graph.edges should be(empty)
  }

  it should "add and remove edges correctly" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    graph.vertices should contain allOf (1, 2, 3)
    graph.edges should contain allOf ((1, 2), (2, 3))

    val updatedGraph = graph.removeEdge(2, 3)
    updatedGraph.edges should not contain (2, 3)
    updatedGraph.edges should contain(1, 2)
  }

  it should "return correct outgoing neighbors" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)

    graph.neighbors(1) should contain only 2
    graph.neighbors(2) should contain only 3
    graph.neighbors(3) should contain only 1
  }

  it should "serialize and deserialize correctly" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    val json = graph.toJson
    json.fromJson[DirectedGraph[Int]] match {
      case Right(decodedGraph) =>
        decodedGraph.vertices should contain allOf (1, 2, 3)
        decodedGraph.edges should contain allOf ((1, 2), (2, 3))
      case Left(error) =>
        fail(s"Deserialization failed with error: $error")
    }
  }

  it should "fail deserialization with invalid JSON" in {
    val invalidJson = """{"invalid": "json"}"""
    invalidJson.fromJson[DirectedGraph[Int]] match {
      case Right(_) =>
        fail("Deserialization should have failed with invalid JSON")
      case Left(_) =>
        succeed
    }
  }
}
