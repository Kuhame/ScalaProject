## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).





# State Management 
This is a terminal-based app to edit graphs in DOT formats (can also be saved to JSON). Due to the simplicity of the app-terminal, our approach to state management focused on simple important mechanisms such as : 

### User input Handling 
The user is guided through the menus and the system ask which vertices, edges to add or remove, all of them have input prompt that highlight these operations. 

### Feedback
User receive immediate feedback on these actions.

### Error Handling
When the user try to add already existing edges or remove non-existent ones, error is handled so that the system is still running instead of crashing.

### In-memory state
When the terminal is on, the graph state is maintained in a mutable variable 'directedGraph', that would be saved later.

### Data Persistence
The data can persists in DOT and JSON format after saving it. The app can also reuse existing graph at a location specified by the user directly in the terminal.


