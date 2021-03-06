package us.hervalicio.ai.lstm

import org.deeplearning4j.datasets.iterator.DataSetIterator
import us.hervalicio.ai.text.Loader

trait Training
    extends NetworkHolder with TrainingConfig with Sampling with Loading {

  lazy val trainingDataLoader = new Loader(trainingFiles, characterMap)

  def fit(trainingSet: DataSetIterator): Unit = {
    model.fit(trainingSet)

    println("Completed epoch")

    trainingSet.reset()
  }

  def runTraining() = {
    val trainingSet = trainingDataLoader.iterator(
        batchSize, exampleLength, examplesPerIteration)

    (0 to iterations).foreach { i =>
      println(s"Traning epoch ${i}")
      fit(trainingSet)

      println("Sampling:")
      println(sample(exampleLength, 1))

      save()
    }
  }
}
