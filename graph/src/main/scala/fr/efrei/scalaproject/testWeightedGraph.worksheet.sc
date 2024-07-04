import fr.efrei.scalaproject.graph.{WeightedGraph, WeightedEdge}
import zio.json._

val weightedGraph: WeightedGraph[String] = WeightedGraph[String]()
  .addEdge("A", "B", 1)
  .addEdge("B", "C", 2)
  .addEdge("C", "A", 3)
  .addEdge("D", "A", 4)

println(weightedGraph.vertices)
println(weightedGraph.edges)

val update = weightedGraph.removeEdge("A", "B")
println(update.edges)

val neighborsOfA = update.neighbors("A")

val json = update.toJson
println(s"Graph as JSON: $json")
