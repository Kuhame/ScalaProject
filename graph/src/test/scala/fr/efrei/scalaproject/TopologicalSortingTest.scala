package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TopologicalSortingSpec extends AnyFlatSpec with Matchers {

  "topologicalSort" should "return a valid topological order for a DAG" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)  // DAG: 1 -> 2 -> 3 -> 4

    val result = TopologicalSorting.topologicalSort(graph)
    result shouldBe defined

    val resultList = result.get
    // Ensure that all vertices are included
    resultList should contain theSameElementsAs graph.vertices.toList

    // Ensure that the result is a valid topological sort
    for {
      vertex <- graph.vertices
      neighbor <- graph.neighbors(vertex)
    } yield {
      resultList.indexOf(vertex) should be < resultList.indexOf(neighbor)
    }
  }

  it should "return None for a graph with a cycle" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)  // Cycle: 1 -> 2 -> 3 -> 1

    val result = TopologicalSorting.topologicalSort(graph)
    result should be (None)
  }

  it should "return Some empty list for an empty graph" in {
    val graph = DirectedGraph[Int]() // Empty graph

    val result = TopologicalSorting.topologicalSort(graph)
    result should be (Some(List()))
  }

  it should "return Some(List(1)) for a graph with only one vertex" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 1) // Single vertex, self-loop (should be ignored)

    val result = TopologicalSorting.topologicalSort(graph)
    result should be(Some(List(1)))
  }

  it should "return a valid topological order for a disconnected graph with no cycles" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(3, 4)
      .addEdge(5, 6)  // Disconnected DAGs

    val result = TopologicalSorting.topologicalSort(graph)
    result shouldBe defined

    val resultList = result.get
    // Ensure that all vertices are included
    resultList should contain theSameElementsAs graph.vertices.toList

    // Ensure that the result is a valid topological sort
    for {
      vertex <- graph.vertices
      neighbor <- graph.neighbors(vertex)
    } yield {
      resultList.indexOf(vertex) should be < resultList.indexOf(neighbor)
    }
  }

  it should "return None for a disconnected graph where some components have cycles" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)  // Cycle: 1 -> 2 -> 3 -> 1
      .addEdge(4, 5)
      .addEdge(5, 6)

    val result = TopologicalSorting.topologicalSort(graph)
    result should be (None)
  }
}
