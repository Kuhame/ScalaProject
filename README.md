# Funtional Programming Project (Scala 3) - Functional Graph

## Project Overview

This project is a terminal-based application that allows users to create, edit, and save directed graphs in DOT format. The application is written in Scala 3 and uses the ZIO library for effect management. The application is designed to be simple and easy to use, with a focus on functional programming principles.

The project is divided into two sub-projects:

1. **Graph Data Structure**: The project includes a graph data structure library with various operations for creating, editing, and saving directed graphs, undirected graphs and weighted directed graphs. It also includes graph operations such as DFS, BFS, Topological Sort, Cycle Detection, Floyd Algorithm and Dijkstra Algorithm.

2. **ZIO Application Integration**: The graph data structure library is integrated into a ZIO application that provides a terminal-based interface for users to interact with the graph data structure.

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

You can also run the test and build the project by executing the following commands:

```bash
sbt test #to run the tests
sbt compile #to compile the project
```

## Design Decisions

### Graph Data Structure


### State Management 
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

### State Management in more complex Application 
What we can do in more complex app, is upscaling the technologies to dedicated alternatives such as : 

#### Dedicated Database
We can think about storing graphs to a SQLite database (or similar) so that we have a centralized persistent graphs data where the user can query for specific ones.

#### Threading and concurrency
ZIO have immutable data structure such as Ref or STM, that can help with functionnal purity and thread-safety. ZIO's primitives can also handle multiple user inputs if the app-terminal need to be operated by multiple users at the same time.

#### Graphic User Interface
Instead of an app terminal, we could try to go on API-based type, where the user will be entering a web server. This can enhance user experience with a cleaner user-friendly interface that can be easily customized to fit our needs later on.
