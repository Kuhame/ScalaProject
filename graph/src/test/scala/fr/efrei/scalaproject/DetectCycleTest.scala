package fr.efrei.scalaproject.graph

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DetectCycleSpec extends AnyFlatSpec with Matchers {

  "hasCycle" should "detect a cycle in a simple cyclic graph" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)

    DetectCycle.hasCycle(graph, 1) shouldBe true
  }

  it should "detect all cycles if a graph contains many" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)
      .addEdge(4, 5)
      .addEdge(5, 6)
      .addEdge(6, 4)

    DetectCycle.hasCycle(graph, 1) shouldBe true
    DetectCycle.hasCycle(graph, 4) shouldBe true
  }

  it should "not detect a cycle if there isn't" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 4)

    DetectCycle.hasCycle(graph, 1) shouldBe false
    DetectCycle.hasCycle(graph, 2) shouldBe false
  }

  it should "handle disconnected graphs and detect cycles in the right component" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)
      .addEdge(3, 1)
      .addEdge(4, 5)

    DetectCycle.hasCycle(graph, 1) shouldBe true
    DetectCycle.hasCycle(graph, 4) shouldBe false
  }

  it should "return false for an empty graph" in {
    val graph = DirectedGraph[Int]()

    DetectCycle.hasCycle(graph, 1) shouldBe false
  }

  it should "return false for a graph where the start vertex does not exist" in {
    val graph = DirectedGraph[Int]()
      .addEdge(1, 2)
      .addEdge(2, 3)

    DetectCycle.hasCycle(graph, 4) shouldBe false
  }
}
