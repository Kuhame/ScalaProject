package fr.efrei.scalaproject.graph

object TopologicalSorting {

  def topologicalSort[V](graph: Graph[V]): Option[List[V]] = {
    def dfsVisit(node: V, visited: Set[V], stack: List[V], tempMarks: Set[V]): (Set[V], List[V], Boolean) = {
      if (tempMarks.contains(node)) {
        (visited, stack, true)
      } else if (visited.contains(node)) {
        (visited, stack, false)
      } else {
        val newTempMarks = tempMarks + node
        val (newVisited, newStack, hasCycle) = graph.neighbors(node).foldLeft((visited + node, stack, false)) {
          case ((vis, stk, cycle), neighbor) =>
            if (cycle) (vis, stk, cycle)
            else {
              val (nextVis, nextStk, nextCycle) = dfsVisit(neighbor, vis, stk, newTempMarks)
              (nextVis, nextStk, nextCycle)
            }
        }
        (newVisited, node :: newStack, hasCycle)
      }
    }

    val (visited, stack, hasCycle) = graph.vertices.foldLeft((Set[V](), List[V](), false)) {
      case ((vis, stk, cycle), vertex) =>
        if (vis.contains(vertex)) (vis, stk, cycle)
        else {
          val (newVis, newStk, newCycle) = dfsVisit(vertex, vis, stk, Set())
          (newVis, newStk, cycle || newCycle)
        }
    }

    if (hasCycle) None
    else Some(stack.reverse)
  }
}
