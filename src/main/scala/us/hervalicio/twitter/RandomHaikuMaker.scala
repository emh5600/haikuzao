package us.hervalicio.twitter

import us.hervalicio.ai.lstm.Network
import us.hervalicio.haiku.Writer

import scala.concurrent.duration._

class RandomHaikuMaker(api: Api, network: Network) extends Runnable {

  val writer = new Writer(network)

  override def run() = {
    while (true) {
      writer.sample.foreach { haiku =>
        println(
          api.post(haiku + "\n#haiku")
        )
        println("Entering deep slumber for some time...")
        Thread.sleep(2.hours.toMillis)
      }
    }
  }

}
