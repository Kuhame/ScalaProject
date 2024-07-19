package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DijkstraSpec extends AnyFlatSpec with Matchers {

  "Dijkstra's algorithm" should "compute the shortest paths for a simple graph" in {
    val graph = Map(
      1 -> List((2, 1), (3, 4)),
      2 -> List((3, 2)),
      3 -> List((4, 1)),
      4 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 1,
      3 -> 3,
      4 -> 4
    )
  }

  it should "handle a graph with a single node" in {
    val graph = Map(1 -> List())

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(1 -> 0)
  }

  it should "handle a disconnected graph" in {
    val graph = Map(
      1 -> List((2, 1)),
      2 -> List(),
      3 -> List((4, 3)),
      4 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 1,
      3 -> Int.MaxValue,
      4 -> Int.MaxValue
    )
  }

  it should "handle a graph with no edges" in {
    val graph = Map(
      1 -> List(),
      2 -> List(),
      3 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> Int.MaxValue,
      3 -> Int.MaxValue
    )
  }

  it should "handle a graph with multiple shortest paths" in {
    val graph = Map(
      1 -> List((2, 1), (3, 2)),
      2 -> List((3, 2)),
      3 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 1,
      3 -> 2
    )
  }

  it should "handle a large graph" in {
    val graph = Map(
      1 -> List((2, 5), (3, 2)),
      2 -> List((3, 1), (4, 2)),
      3 -> List((4, 4)),
      4 -> List((5, 1)),
      5 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 5,
      3 -> 2,
      4 -> 6,
      5 -> 7
    )
  }

  it should "handle graphs with negative weights" in {
    val graph = Map(
      1 -> List((2, 2), (3, 4)),
      2 -> List((3, -2)),
      3 -> List((4, 1)),
      4 -> List()
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 2,
      3 -> 0,
      4 -> 1
    )
  }

  it should "handle graphs with cycles correctly" in {
    val graph = Map(
      1 -> List((2, 1)),
      2 -> List((3, 1)),
      3 -> List((4, 1)),
      4 -> List((1, 1)) // Creates a cycle
    )

    val result = Dijkstra.dijkstra(graph, 1)
    result shouldEqual Map(
      1 -> 0,
      2 -> 1,
      3 -> 2,
      4 -> 3
    )
  }

}
