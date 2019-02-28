package net.swigg.nlrecipe

import com.opencsv.CSVReader
import edu.stanford.nlp.classify.ColumnDataClassifier
import edu.stanford.nlp.classify.LinearClassifier
import edu.stanford.nlp.classify.LinearClassifierFactory
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Paths
import java.util.*

fun main() {
    val source = Paths.get("./training/data/ingredient-classifier/ingredients")
    val properties = Properties()
    properties.load(FileInputStream(source.resolve("ingredients.properties").toString()))
    val columnClassifier = ColumnDataClassifier(properties)

    val classifier = LinearClassifier.readClassifier<String, String>("./training/data/ingredient-classifier/ingredients/ingredientsModel")
    println(classifier)
}