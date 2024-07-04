import fr.efrei.scalaproject.graph.{UndirectedGraph}

val undirectedGraph = UndirectedGraph[String]()
  .addEdge("A", "B")
  .addEdge("B", "C")
  .addEdge("C", "A")
  .addEdge("D", "A")

println(undirectedGraph.vertices)
println(undirectedGraph.edges)

val update = undirectedGraph.removeEdge("A", "B")
println(update.edges)

val neighborsOfA = undirectedGraph.neighbors("A")
