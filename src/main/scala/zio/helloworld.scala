package zio

import zio._
import java.io.IOException

object Main extends ZIOAppDefault {
    val myApp:ZIO[Any, IOException, Unit] = 
        Connsole.printLine("Hello, World !")

    def run = myApp
}
