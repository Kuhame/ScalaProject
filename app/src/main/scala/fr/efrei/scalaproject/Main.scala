
import zio._


object Main extends ZIOAppDefault {
    def run =
    //Console.printLine("Hello, World!")
    /*  Console.printLine(
      """This is line 1.
        |This is line 2.
        |This is line 3.""".stripMargin
    ) */
    for {
        _ <- Console.printLine("What do you want to do ?")
        _ <- Console.printLine("A. Create blank")
        _ <- Console.printLine("B. Use existing graph")
        firstChoice <- Console.readLine
        _ <- Console.printLine(s"You have chosen $firstChoice!")
    } yield()
      
}
