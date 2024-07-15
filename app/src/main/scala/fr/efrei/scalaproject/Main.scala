
import zio._
import zio.Console._
import zio.json._
import fr.efrei.scalaproject.graph.DirectedGraph
import java.io.IOException
import java.nio.file.{Files,Paths}
object Main extends ZIOAppDefault {

  //diagramme à exploiter
  var directedGraph = DirectedGraph[String]()
  var loadedFilePath: Option[String] = None


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
  case "B" => handleUseExistingGraph.orDie
  case _   => Console.printLine(s"Invalid choice: $choice").orDie
  }


  // A. Create a blank Graph 
  def handleCreateBlankGraph: UIO[Unit] = 
  for {
      _ <- Console.printLine(
        """
        =====================
          |A-1: What to do?
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


  // B. Use existing Graph
  def handleUseExistingGraph: IO[IOException, Unit] =
    for {
      _ <- Console.printLine("Enter the path to the DOT file:")
      path <- Console.readLine.orDie
      _ <- loadGraphFromDot(path)
      _ = loadedFilePath = Some(path) // Store the file path
      _ <- useExistingGraphMenu
    } yield ()

  //voici a quoi doit ressembler le path du fichier dot : D:/scala/graph.dot
  //bien se rassurer que le fihier soit bien un fichier dot

  def loadGraphFromDot(path: String): IO[IOException, Unit] =
    for {
      content <- ZIO.attempt(Files.readString(Paths.get(path))).refineToOrDie[IOException]
      lines = content.split("\n").filter(_.trim.nonEmpty) // Filter out empty lines
      _ <- ZIO.foreachDiscard(lines)(parseAndAddEdge)
    } yield ()

  val edgePattern = "([A-Za-z0-9]+) -> ([A-Za-z0-9]+);".r

  def parseAndAddEdge(line: String): UIO[Unit] =
    line.trim match {
      case edgePattern(from, to) => ZIO.succeed {
        directedGraph = directedGraph.addEdge(from, to)
      }
      case _ => ZIO.unit
    }

  def useExistingGraphMenu: UIO[Unit] =
    for {
      _ <- Console.printLine(
        """
        =====================
          |B-1: What to do?
          |1. Get all Vertices
          |2. Get all Edges
          |3. Get neighbors of a Vertex
          |4. Add Edge
          |5. Remove Edge
          |6. Save as Graph (DOT + JSON)
        """.stripMargin
      ).orDie
      subChoice <- Console.readLine.orDie
      _ <- useMenu(subChoice)
    } yield ()

  def useMenu(choice: String): UIO[Unit] = choice.trim match {
    case "1" => getAllVertices *> useExistingGraphMenu
    case "2" => getAllEdges *> useExistingGraphMenu
    case "3" => getNeighborsOfVertex *> useExistingGraphMenu
    case "4" => addEdge *> saveLoadedGraph *> useExistingGraphMenu
    case "5" => removeEdge *> saveLoadedGraph *> useExistingGraphMenu
    case "6" => saveGraph
    case _   => Console.printLine(s"Invalid choice: $choice").orDie *> useExistingGraphMenu
  }

  def displayGraph: UIO[Unit] = Console.printLine(directedGraph.toDot()).orDie

  def saveLoadedGraph: UIO[Unit] = loadedFilePath match {
    case Some(path) =>
      val dotFormat = directedGraph.toDot()
      ZIO.attempt(Files.writeString(Paths.get(path), dotFormat)).orDie *>
        Console.printLine(s"Graph saved to $path").orDie
    case None => Console.printLine("No file loaded to save").orDie
  }


  // Handler of Menu A-1 : What to Do ?
  def getAllVertices: UIO[Unit] =
    Console.printLine(s"Fetching all vertices : ${directedGraph.vertices}").orDie

  def getAllEdges: UIO[Unit] =
    Console.printLine(s"Fetching all edges : ${directedGraph.edges}").orDie

  def getNeighborsOfVertex: UIO[Unit] =
    (for {
      _ <- Console.print("Enter vertex to get neighbors: ")
      vertex <- Console.readLine.orDie
      _ <- Console.printLine(s"Neighbors of $vertex: ${directedGraph.neighbors(vertex)}")
    } yield ()).orDie

  def addEdge: UIO[Unit] =
    //Console.printLine(s"Adding an edge... ${directedGraph.addEdge}").orDie
    (for {
      _ <- Console.print("Enter vertexes to add an edge from: ...")
      from <- Console.readLine.orDie
      _ <- Console.print("Enter vertex to add edge to: ... ")
      to <- Console.readLine.orDie
      _ = directedGraph = directedGraph.addEdge(from, to)
      _ <- Console.printLine(s"Edge added from $from to $to")
    } yield ()).orDie

  def removeEdge: UIO[Unit] =
     (for {
      _ <- Console.print("Remove an edge from vertex:...")
      from <- Console.readLine.orDie
      _ <- Console.print("Enter vertex to remove edge to: ")
      to <- Console.readLine.orDie
      _ = directedGraph = directedGraph.removeEdge(from, to)
      _ <- Console.printLine(s"Edge removed from $from to $to")
    } yield ()).orDie
    

  def saveGraph: UIO[Unit] =
    (for {
      _ <- Console.printLine("Saving the graph as DOT and JSON.")
      dotFormat = directedGraph.toDot()
      _ <- Console.printLine(s"Graph in DOT format:\n$dotFormat")
      jsonFormat = directedGraph.toJson

      //find folder and save files to graphsResults folder
      _ <- ZIO.attempt {
        val currentDir = Paths.get("").toAbsolutePath
        val graphsDir = currentDir.resolve("app/src/main/scala/fr/efrei/scalaproject/graphsResults")
        if (!Files.exists(graphsDir)) Files.createDirectories(graphsDir)
        Files.write(graphsDir.resolve("graph.dot"), dotFormat.getBytes)
        Files.write(graphsDir.resolve("graph.json"), jsonFormat.getBytes)
      }.orDie

    } yield ()).orDie

}
