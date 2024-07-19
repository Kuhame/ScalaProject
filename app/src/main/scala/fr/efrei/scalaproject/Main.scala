import zio._
import zio.Console._
import zio.json._
import fr.efrei.scalaproject.graph.{DirectedGraph, DFS}
import java.io.IOException
import java.nio.file.{Files, Paths, NoSuchFileException}

object Main extends ZIOAppDefault {

  def run =
    for {
      // Initialize state
      directedGraphRef <- Ref.make(DirectedGraph[String]())
      loadedFilePathRef <- Ref.make(None: Option[String])

      // Menu A-B
      _ <- Console.printLine("What do you want to do?")
      _ <- Console.printLine("A. Create blank")
      _ <- Console.printLine("B. Use existing graph")

      firstChoice <- Console.readLine
      _ <- handleFirstChoice(firstChoice, directedGraphRef, loadedFilePathRef)
    } yield ()

  // Handler of Menu A-B
  def handleFirstChoice(choice: String, directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): UIO[Unit] = choice.trim.toUpperCase match {
    case "A" => handleCreateBlankGraph(directedGraph)
    case "B" => handleUseExistingGraph(directedGraph, loadedFilePath).orDie
    case _   => Console.printLine(s"Invalid choice: $choice").orDie
  }

  // A. Create a blank Graph
  def handleCreateBlankGraph(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
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
          |7. Perform DFS
          |8. Quit
        """.stripMargin
      ).orDie
      subChoice <- Console.readLine.orDie
      _ <- blankMenu(subChoice, directedGraph)
    } yield ()

  // B. Use existing Graph
  def handleUseExistingGraph(directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): IO[IOException, Unit] =
    for {
      _ <- Console.printLine("Choose file type to load:")
      _ <- Console.printLine("1. Load a DOT file")
      _ <- Console.printLine("2. Load a JSON file")
      fileTypeChoice <- Console.readLine.orDie
      _ <- fileTypeChoice.trim match {
        case "1" => handleLoadDotFile(directedGraph, loadedFilePath)
        case "2" => handleLoadJsonFile(directedGraph, loadedFilePath) // Placeholder for future implementation
        case _ => Console.printLine("Invalid choice. Please select 1 or 2.") *> handleUseExistingGraph(directedGraph, loadedFilePath)
      }
    } yield ()

  def handleLoadDotFile(directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): IO[IOException, Unit] =
    for {
      _ <- Console.printLine("Enter the path to the DOT file (or type 'new' to create a blank)")
      path <- Console.readLine.orDie
      _ <- if (path.trim.toLowerCase == "new") {
        Console.printLine("Setting new graph ... ") *> handleCreateBlankGraph(directedGraph)
      } else {
        loadGraphFromDot(path, directedGraph).catchAll {
          case _: NoSuchFileException =>
            Console.printLine("The specified path does not exist. Try again.") *> handleLoadDotFile(directedGraph, loadedFilePath)
          case e: IOException =>
            Console.printLine(s"An error occurred: ${e.getMessage}. Try again.") *> handleLoadDotFile(directedGraph, loadedFilePath)
        } *> {
          loadedFilePath.set(Some(path)) *> useExistingGraphMenu(directedGraph, loadedFilePath)
        }
      }
    } yield ()

  val graphDecoder: JsonDecoder[DirectedGraph[String]] = JsonDecoder[DirectedGraph[String]]
  val decoder: JsonDecoder[Map[String, List[String]]] = JsonDecoder[Map[String, List[String]]]

  def handleLoadJsonFile(directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): IO[IOException, Unit] =
  for {
    _ <- Console.printLine("Enter the path to the JSON file (or type 'new' to create a blank)")
    path <- Console.readLine.orDie
    result <- path.trim.toLowerCase match {
      case "new" =>
        Console.printLine("Setting new graph...") *> handleCreateBlankGraph(directedGraph)
      case _ =>
        ZIO.attempt(Files.readString(Paths.get(path)))
          .refineToOrDie[IOException] // Handle file read errors
          .flatMap { content =>
            content.fromJson[DirectedGraph[String]] match {
              case Right(graph) =>
                Console.printLine(s"Decoded Graph: $graph").orDie *>
                  directedGraph.set(graph) *>
                  loadedFilePath.set(Some(path)) *>
                  useExistingGraphMenu(directedGraph, loadedFilePath)
              case Left(error) =>
                Console.printLine(s"Decoding error: $error. Please ensure the JSON format is correct and try again.") *> 
                  handleLoadJsonFile(directedGraph, loadedFilePath)
            }
          }
          .catchAll {
            case _: NoSuchFileException =>
              Console.printLine("The specified file does not exist. Please try again.") *> 
                handleLoadJsonFile(directedGraph, loadedFilePath)
            case e: IOException =>
              Console.printLine(s"An I/O error occurred: ${e.getMessage}. Please try again.") *> 
                handleLoadJsonFile(directedGraph, loadedFilePath)
          }
    }
  } yield result

  // Handler of Menu A
  def blankMenu(choice: String, directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] = choice.trim match {
    case "1" => getAllVertices(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "2" => getAllEdges(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "3" => getNeighborsOfVertex(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "4" => addEdge(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "5" => removeEdge(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "6" => saveGraph(directedGraph)
    case "7" => performDFS(directedGraph) *> handleCreateBlankGraph(directedGraph)
    case "8" => ZIO.succeed(())
    case _   => Console.printLine(s"Invalid choice: $choice").orDie *> handleCreateBlankGraph(directedGraph)
  }

  def loadGraphFromDot(path: String, directedGraph: Ref[DirectedGraph[String]]): IO[IOException, Unit] =
    for {
      content <- ZIO.attempt(Files.readString(Paths.get(path))).refineToOrDie[IOException]
      lines = content.split("\n").filter(_.trim.nonEmpty) // Filter out empty lines
      _ <- ZIO.foreachDiscard(lines)(parseAndAddEdge(_, directedGraph))
    } yield ()

  val edgePattern = "([A-Za-z0-9]+) -> ([A-Za-z0-9]+);".r

  def parseAndAddEdge(line: String, directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    line.trim match {
      case edgePattern(from, to) => directedGraph.update(_.addEdge(from, to)).unit
      case _ => ZIO.unit
    }

  def useExistingGraphMenu(directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): UIO[Unit] =
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
          |7. Perform DFS
          |8. Quit
        """.stripMargin
      ).orDie
      subChoice <- Console.readLine.orDie
      _ <- useMenu(subChoice, directedGraph, loadedFilePath)
    } yield ()

  def useMenu(choice: String, directedGraph: Ref[DirectedGraph[String]], loadedFilePath: Ref[Option[String]]): UIO[Unit] = choice.trim match {
    case "1" => getAllVertices(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "2" => getAllEdges(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "3" => getNeighborsOfVertex(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "4" => addEdge(directedGraph)  *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "5" => removeEdge(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "6" => saveGraph(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "7" => performDFS(directedGraph) *> useExistingGraphMenu(directedGraph, loadedFilePath)
    case "8" => ZIO.succeed(())
    case _   => Console.printLine(s"Invalid choice: $choice").orDie *> useExistingGraphMenu(directedGraph, loadedFilePath)
  }

  def displayGraph(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    directedGraph.get.flatMap(graph => Console.printLine(graph.toDot())).orDie

  // Handler of Menu A-1: What to Do?
  def getAllVertices(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    directedGraph.get.flatMap(graph => Console.printLine(s"Fetching all vertices: ${graph.vertices}")).orDie

  def getAllEdges(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    directedGraph.get.flatMap(graph => Console.printLine(s"Fetching all edges: ${graph.edges}")).orDie

  def getNeighborsOfVertex(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    (for {
      _ <- Console.print("Enter vertex to get neighbors: ")
      vertex <- Console.readLine.orDie
      graph <- directedGraph.get
      _ <- Console.printLine(s"Neighbors of $vertex: ${graph.neighbors(vertex)}")
    } yield ()).orDie

  def addEdge(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    (for {
      _ <- Console.print("Enter vertexes to add an edge from: ")
      from <- Console.readLine.orDie
      _ <- Console.print("Enter vertex to add edge to:  ")
      to <- Console.readLine.orDie
      graph <- directedGraph.get
      _ <- if (graph.edges.exists(edge => edge._1 == from && edge._2 == to)) {
             Console.printLine(s"Edge from $from to $to already exists").orDie
           } else {
             directedGraph.update(_.addEdge(from, to)) *>
               Console.printLine(s"Edge added from $from to $to").orDie
           }
    } yield ()).orDie

  def removeEdge(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    (for {
      _ <- Console.print("Remove an edge from vertex: ")
      from <- Console.readLine.orDie
      _ <- Console.print("Enter vertex to remove edge to: ")
      to <- Console.readLine.orDie
      graph <- directedGraph.get
      _ <- if (graph.edges.exists(edge => edge._1 == from && edge._2 == to)) {
             directedGraph.update(_.removeEdge(from, to)) *>
               Console.printLine(s"Edge removed from $from to $to").orDie
           } else {
             Console.printLine(s"Edge from $from to $to does not exist").orDie
           }
    } yield ()).orDie

  def saveGraph(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    (for {
      _ <- Console.printLine("Saving the graph as DOT and JSON.")
      graph <- directedGraph.get
      dotFormat = graph.toDot()
      _ <- Console.printLine(s"Graph in DOT format:\n$dotFormat")
      jsonFormat = graph.toJson

      // Find folder and save files to graphsResults folder
      _ <- ZIO.attempt {
        val currentDir = Paths.get("").toAbsolutePath
        val graphsDir = currentDir.resolve("app/src/main/scala/fr/efrei/scalaproject/graphsResults")
        if (!Files.exists(graphsDir)) Files.createDirectories(graphsDir)
        Files.write(graphsDir.resolve("graph.dot"), dotFormat.getBytes)
        Files.write(graphsDir.resolve("graph.json"), jsonFormat.getBytes)
      }.orDie

      _ <- Console.printLine("Graph saved successfully as DOT format to graph.dot.")
      _ <- Console.printLine("Graph saved successfully as JSON format to graph.json.")

    } yield ()).orDie

  def performDFS(directedGraph: Ref[DirectedGraph[String]]): UIO[Unit] =
    (for {
      _ <- Console.print("Enter the starting vertex for DFS: ")
      start <- Console.readLine.orDie
      graph <- directedGraph.get
      _ <- if (graph.vertices.contains(start)) {
             val dfsResult = DFS.dfs(graph, start)
             Console.printLine(s"DFS starting from $start: ${dfsResult.mkString(", ")}").orDie
           } else {
             Console.printLine(s"Vertex $start does not exist in the graph.").orDie
           }
    } yield ()).orDie
}