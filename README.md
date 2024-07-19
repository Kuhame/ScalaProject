# Funtional Programming Project (Scala 3)

## Project Overview

This project is a terminal-based application that allows users to create, edit, and save directed graphs in DOT format. The application is written in Scala 3 and uses the ZIO library for effect management. The application is designed to be simple and easy to use, with a focus on functional programming principles.

The project is divided into two sub-projects:

1. **Graph Data Structure**: The project includes a graph data structure library with various operations for creating, editing, and saving directed graphs, undirected graphs and weighted directed graphs. It also includes graph operations such as DFS, BFS, Topological Sort, Cycle Detection, Floyd Algorithm and Dijkstra Algorithm.

2. **ZIO Application Integration**: The graph data structure library is integrated into a ZIO application that provides a terminal-based interface for users to interact with the graph data structure. 
**Currently, the app only support directed graphs**, but it can be easily extended to support other types of graphs.

## Pre-requisites

- Scala 3
- SBT
- Java 11 or higher

## Instructions

To run the application, you will need to have SBT installed on your machine. You can run the application by executing the following command in the root directory of the project:

```bash
sbt run
```

This will start the application in the terminal. You can then follow the on-screen instructions to create, edit, and save directed graphs. The application supports the following operations:

- Create a new graph from nothing
- Load an existing graph from a file (DOT format or JSON format)
- Add edges to the graph
- Remove edges from the graph
- List all vertices in the graph
- List all edges in the graph
- List neighbors of a specific vertex
- Save the graph to a file (DOT format or JSON format)
- Run graph algorithms (DFS, BFS, Topological Sort, Cycle Detection, Floyd Algorithm, Dijkstra Algorithm)
- Exit the application

You can also run the test and build the project by executing the following commands:

```bash
sbt test #to run the tests
sbt compile #to compile the project
```

## Design Decisions

### Graph Data Structure

The graph data structure library is designed to be simple and easy to use. It includes three types of graphs: directed graphs, undirected graphs, and weighted directed graphs. Each graph type has its own implementation with specific operations.

The graph data structure is implemented using an adjacency list representation, which is a common representation for graphs along with the adjacency matrix. The adjacency list is a map that maps each vertex to a list of its neighbors. This representation allows for efficient access to the neighbors of a vertex and is suitable for most graph operations. Compared to an adjacency matrix, the adjacency list is faster and uses less memory for sparse graphs where the number of edges is much smaller than the number of possible edges.

For the design of the graph data structure, we have followed functional programming principles such as immutability. The graph data structure is designed to be immutable, meaning that once a graph is created, it cannot be modified. Instead, operations on the graph return a new graph with the desired changes.

We have also implemented graph algorithms such as DFS, BFS, Topological Sort, Cycle Detection, Floyd Algorithm, and Dijkstra Algorithm. These algorithms are commonly used in graph theory and provide useful insights into the structure of a graph. 
For now, theses algorithms have been implemented and tested to work on directed graphs.

#### Graph Classes and Traits 

The graph data structure library includes the following classes and traits:

- `Graph`: A trait that represents a generic graph. It defines common operations such as adding and removing edges, getting neighbors of a vertex, list all vertices and edges and show the dot format of the graph.

- `DirectedGraph`: A class that extends the `Graph` trait for directed graphs. It uses an adjacency list representation to store the graph data.

- `UndirectedGraph`: A class that extends the `Graph` trait for undirected graphs. It uses an adjacency list representation similar to the directed graph.

- `WeightedGraph`: A class that extends the `Graph` trait for weighted directed graphs. It uses also uses an adjacency list representation and a weigthed edge class to store the weight of the edges.

All of theses classes extends the Graph trait because they share common operations such as adding and removing edges, getting neighbors of a vertex, etc ...

Each graph have an object companion that contains the apply method to create a new empty graph and the encode and decode methods to save and load the graph in JSON format.

#### Graph Operations

# TODO ADD ALGORITHMS DESCRIPTION

### State Management 

# TODO CHANGE STATE MANAGEMENT SECTION TO EXPLAIN HOW STATE IS MANAGED IN THE APPLICATION (IMMUTABILITY, ZIO, ETC.)

This is a terminal-based app to edit graphs in DOT formats (can also be saved to JSON). Due to the simplicity of the app-terminal we tried to keep it easy to understand, so our approach to state management focused exclusively on important mechanisms such as : 

#### User input Handling 
The user is guided through the menus and the system ask which vertices, edges to add or remove, all of them have input prompt that highlight these operations. If he missclicked to "use existing graphs" he can type "new" in the path input instead, to create a new graph.

#### Feedback
User receive immediate feedback on these actions. Though some are minimized since we wanted to have each functions do exactly what it needs to do (addEdge is exclusively adding edge but don't return the new graph structure).

#### Error Handling
When the user try to add already existing edges or remove non-existent ones, error is handled so that the system is still running instead of crashing.

#### In-memory state
When the terminal is on, the graph state is maintained in a mutable variable 'directedGraph', that would be saved later.

#### Data Persistence
The data can persists in DOT and JSON format after saving it. The app can also reuse existing graph at a location specified by the user directly in the terminal.

### Future Possible Improvements
What we can do in more complex app, is upscaling the technologies to dedicated alternatives such as : 

#### Dedicated Database
We can think about storing graphs to a SQLite database (or similar) so that we have a centralized persistent graphs data where the user can query for specific ones.

#### Threading and concurrency
ZIO have immutable data structure such as Ref or STM, that can help with functionnal purity and thread-safety. ZIO's primitives can also handle multiple user inputs if the app-terminal need to be operated by multiple users at the same time.

#### Graphic User Interface
Instead of an app terminal, we could try to go on API-based type, where the user will be entering a web server. This can enhance user experience with a cleaner user-friendly interface that can be easily customized to fit our needs later on.

## Testing

The project includes unit tests for the graph data structure library. The tests cover various scenarios for creating, editing, and saving directed, undirected, and weighted graphs and also for the graph algorithms.
The tests are written using the ScalaTest library and can be run using the `sbt test` command.

### Overview of Tests

Here's an overview of the test coverage for each type of graph and algorithms:

#### Common Graph Tests:

- **Initialization**: Confirms that a new graph starts empty with no vertices or edges.

- **Edge Management**: Verifies the correct addition and removal of edges, ensuring the integrity of the graph structure. For weighted graphs, this includes verifying the weights of the edges.

- **Neighbor Retrieval**: Ensures that the neighbors method returns the correct neighbors for each vertex. For directed and weighted graphs, this involves outgoing neighbors, while for undirected graphs, this includes both directions.

- **Vertex Filtering**: Checks that isolated vertices with empty edge sets are correctly filtered out.

- **Serialization/Deserialization**: Validates the correct serialization to JSON and deserialization from JSON, including error handling for invalid JSON.

- **DOT Format Serialization**: Ensures that the graph can be correctly serialized to the DOT format, suitable for visualization tools like Graphviz.

#### DirectedGraph Specific Tests:

- **Directed Edges**: Ensures that edges are correctly directed from one vertex to another and not bidirectional unless specified otherwise.

#### UndirectedGraph Specific Tests:

- **Bidirectional Edges**: Verifies that edges are correctly treated as bidirectional, with neighbors accessible from both vertices involved in the edge.

#### WeightedGraph Specific Tests:

- **Edge Weights**: Confirms that edges include weights and these weights are correctly handled during edge manipulations and neighbor retrieval.


#### Graph Algorithms Tests:
# TODO

## Usage Example

Here's an example of how to use the application:

### Start the Application

To start the application, run the following command in the root directory of the project:

```bash
sbt run
```

This will start the application in the terminal.

### Create a New Graph from a DOT File

To create a new graph from a DOT file, select the option to use an existing graph and enter the path to the DOT file when prompted. 

For example, to create a new graph from the `graph.dot` file, enter the following path:

```bash
C:\Users\Laure\Documents\graph.dot
```

The graph will be loaded from the file, and you can then perform operations on it.

### Add Edges to the Graph

To add edges to the graph, select the option 4 to add edges and enter the vertices for the edge when prompted.
The edge will be added to the graph, and you can continue adding more edges.

### List All Vertices in the Graph

To list all vertices in the graph, select the option 1 to list all vertices. The vertices will be displayed on the screen.

### List All Edges in the Graph

To list all edges in the graph, select the option 2 to list all edges. The edges will be displayed on the screen.

### Remove Edges from the Graph

To remove edges from the graph, select the option 5 to remove edges and enter the vertices for the edge when prompted.

### Apply DFS Algorithm

# TODO

### Save the Graph to a DOT and JSON File

To save the graph to a DOT and JSON file, select the option 6 to save the graph.
The graph will be saved in the graphsResults folder in the app project directory.

### Exit the Application

To exit the application, select the option 7 to exit the application.