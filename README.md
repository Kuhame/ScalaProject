## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).





# State Management 
This is a terminal-based app to edit graphs in DOT formats (can also be saved to JSON). Due to the simplicity of the app-terminal we tried to keep it easy to understand, so our approach to state management focused exclusively on important mechanisms such as : 

### User input Handling 
The user is guided through the menus and the system ask which vertices, edges to add or remove, all of them have input prompt that highlight these operations. If he missclicked to "use existing graphs" he can type "new" in the path input instead, to create a new graph.

### Feedback
User receive immediate feedback on these actions. Though some are minimized since we wanted to have each functions do exactly what it needs to do (addEdge is exclusively adding edge but don't return the new graph structure).

### Error Handling
When the user try to add already existing edges or remove non-existent ones, error is handled so that the system is still running instead of crashing.

### In-memory state
When the terminal is on, the graph state is maintained in a mutable variable 'directedGraph', that would be saved later.

### Data Persistence
The data can persists in DOT and JSON format after saving it. The app can also reuse existing graph at a location specified by the user directly in the terminal.

# State Management in more complex Application 
What we can do in more complex app, is upscaling the technologies to dedicated alternatives such as : 

### Dedicated Database
We can think about storing graphs to a SQLite database (or similar) so that we have a centralized persistent graphs data where the user can query for specific ones.

### Threading and concurrency
ZIO have immutable data structure such as Ref or STM, that can help with functionnal purity and thread-safety. ZIO's primitives can also handle multiple user inputs if the app-terminal need to be operated by multiple users at the same time.

### Graphic User Interface
Instead of an app terminal, we could try to go on API-based type, where the user will be entering a web server. This can enhance user experience with a cleaner user-friendly interface that can be easily customized to fit our needs later on.
