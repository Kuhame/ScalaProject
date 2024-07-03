import fr.efrei.scalaproject.graph.{DirectedGraph}

val directedGraph = DirectedGraph[String]()
  .addEdge("A", "B")
  .addEdge("B", "C")
  .addEdge("B", "A")
  .addEdge("C", "A")
  .addEdge("D", "A")

println(directedGraph.vertices)
println(directedGraph.edges)

val update = directedGraph.removeEdge("A", "B")
println(update.edges)

val neighborsOfA = directedGraph.neighbors("A")
