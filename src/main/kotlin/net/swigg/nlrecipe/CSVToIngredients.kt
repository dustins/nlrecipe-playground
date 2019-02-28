package net.swigg.nlrecipe

import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import com.opencsv.RFC4180Parser
import java.io.FileReader
import java.io.FileWriter
import java.lang.RuntimeException

fun main() {
    val reader =
    CSVReaderBuilder(FileReader("./training/data/ingredient-classifier/ingredients/nyt-ingredients-snapshot-2015.csv"))
        .withSkipLines(1)
        .withCSVParser(RFC4180Parser())
        .withMultilineLimit(10)
        .build()

    val writer = FileWriter("./training/data/ingredient-classifier/ingredients/ingredients.txt", false)

    for (line in reader) {
        if (line[1].isNotBlank()) {
            val cleaned = line[1].trim()
                .replace(Regex("^\n+"), "")
                .replace(Regex("\n+$"), "")
                .replace("\\n", " ")

            if (cleaned.contains("6 cups (about 2 pounds) pumpkin or butternut squash, peeled, seeded")) {
                println("here")
            }
            writer.appendln(cleaned)
        }

        if (line[0].toIntOrNull() ?: 0 % 1000 == 0) {
            writer.flush()
        }
    }
}