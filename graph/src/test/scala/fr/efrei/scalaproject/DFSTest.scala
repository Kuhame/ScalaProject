package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DFSSpec extends AnyFlatSpec with Matchers {

  "DFS" should "visit all vertices in a connected graph" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)

    val result = DFS.dfs(graph, 1)
    result should contain allOf (1, 2, 3, 4)
  }

  it should "handle graphs with cycles correctly and not get stuck in an infinite loop" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)
      .addEdge(3, 4)

    val result = DFS.dfs(graph, 1)
    result should contain allOf (1, 2, 3, 4)
  }

  it should "handle disconnected graphs correctly and not traverse unreachable vertices" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(3, 4)

    val result = DFS.dfs(graph, 1)
    result should contain allOf (1, 2)
    result should not contain (3, 4)

    val resultFrom3 = DFS.dfs(graph, 3)
    resultFrom3 should contain allOf (3, 4)
    resultFrom3 should not contain (1, 2)
  }

  it should "return only the starting vertex if it has no neighbors" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(4, 4)

    val result = DFS.dfs(graph, 4)
    result should contain only 4
  }

  it should "return an empty set if the starting vertex does not exist in the graph" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    val result = DFS.dfs(graph, 5)
    result should be (empty)
  }

  it should "return an empty set for an empty graph" in {
    val graph = DirectedGraph[Int]()

    val result = DFS.dfs(graph, 1)
    result should be(empty)
  }
}
