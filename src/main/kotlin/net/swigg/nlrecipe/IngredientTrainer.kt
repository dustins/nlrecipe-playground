package net.swigg.nlrecipe

import edu.stanford.nlp.classify.ColumnDataClassifier
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

fun main() {
    val source = Paths.get("./training/data/ingredient-classifier/ingredients")

    val properties = Properties()
    properties.load(FileInputStream(source.resolve("ingredients.properties").toString()))

    val classifier = ColumnDataClassifier(properties)
//    classifier.trainClassifier(source.resolve(properties.getProperty("trainFile")).toString())
//    classifier.serializeClassifier(source.resolve(properties.getProperty("serializeTo")).toString())
    println("done")
}