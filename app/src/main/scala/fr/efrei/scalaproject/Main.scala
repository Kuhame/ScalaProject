
import zio._
import zio.Console._
import fr.efrei.scalaproject.graph.DirectedGraph
object Main extends ZIOAppDefault {

  val directedGraph = DirectedGraph[String]()
  .addEdge("A", "B")
  def run =
    for {
        // Menu A-B
        _ <- Console.printLine("What do you want to do ?")
        _ <- Console.printLine("A. Create blank")
        _ <- Console.printLine("B. Use existing graph")

        firstChoice <- Console.readLine
        _ <- handleFirstChoice(firstChoice)
    } yield()
      
  // Handler of Menu A-B
  def handleFirstChoice(choice: String): UIO[Unit] = choice.trim.toUpperCase match {
  case "A" => handleCreateBlankGraph
  //case "B" => handleUseExistingGraph
  case _   => Console.printLine(s"Invalid choice: $choice").orDie
  }


  // A. Create a blank Graph 
  def handleCreateBlankGraph: UIO[Unit] = 
  for {
      _ <- Console.printLine(
      """A-1: What to do?
        |1. Get all Vertices
        |2. Get all Edges
        |3. Get neighbors of a Vertex
        |4. Add Edge
        |5. Remove Edge
        |6. Save as Graph (DOT + JSON)
      """.stripMargin
    ).orDie
    subChoice <- Console.readLine.orDie
    _ <- blankMenu(subChoice)
  } yield()

  // Handler of Menu A
  // les *> permettent de renvoyer vers un autre menu après l'exécution de la première
  def blankMenu(choice: String): UIO[Unit] = choice.trim match {
    case "1" => getAllVertices *> handleCreateBlankGraph
    case "2" => getAllEdges *> handleCreateBlankGraph
    case "3" => getNeighborsOfVertex *> handleCreateBlankGraph
    case "4" => addEdge *> handleCreateBlankGraph
    case "5" => removeEdge  *> handleCreateBlankGraph
    case "6" => saveGraph 
    case _   => Console.printLine(s"Invalid choice: $choice").orDie *> handleCreateBlankGraph
  }

  // Handler of Menu A-1 : What to Do ?
  def getAllVertices: UIO[Unit] =
    Console.printLine(s"Fetching all vertices : ${directedGraph.vertices}").orDie

  def getAllEdges: UIO[Unit] =
    Console.printLine("Fetching all edges...").orDie

  def getNeighborsOfVertex: UIO[Unit] =
    Console.printLine("Fetching neighbors of a vertex...").orDie

  def addEdge: UIO[Unit] =
    Console.printLine(s"Adding an edge... ${directedGraph.addEdge}").orDie

  def removeEdge: UIO[Unit] =
    Console.printLine("Removing an edge...").orDie

  def saveGraph: UIO[Unit] =
    Console.printLine("Saving the graph as DOT and JSON...").orDie
}
