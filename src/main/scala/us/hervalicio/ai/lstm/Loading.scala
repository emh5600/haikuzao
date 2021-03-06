package us.hervalicio.ai.lstm

import java.io._

import org.apache.commons.io.FileUtils
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.factory.Nd4j

/**
  * Created by herval on 4/19/16.
  */
trait Loading extends NetworkHolder with NetworkConfig {

  def loadOrSeed(): MultiLayerNetwork = {
    try {
      load()
    } catch {
      case e: Exception => {
          println(s"Couldn't load - setting up a new network: ${e.getMessage}")
          defaultTopology
        }
    }
  }

  private def load(): MultiLayerNetwork = {
    storagePath.toFile.mkdirs()

    val confFromJson = MultiLayerConfiguration.fromJson(
        FileUtils.readFileToString(topologyFile))
    val dis = new DataInputStream(new FileInputStream(coefficientsFile))
    val newParams = Nd4j.read(dis)
    dis.close()

    println(s"Loading network from disk (${topologyFile.getAbsolutePath})")

    val model = new MultiLayerNetwork(confFromJson)
    model.init()
    model.setParameters(newParams)
    model
  }

  def save() = {
    val dos = new DataOutputStream(new FileOutputStream(coefficientsFile))
    Nd4j.write(model.params(), dos)
    dos.flush()
    dos.close()

    FileUtils.writeStringToFile(
        topologyFile, model.getLayerWiseConfigurations.toJson)
  }
}
