package net.swigg.nlrecipe

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.apache.tika.language.detect.LanguageDetector
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileWriter
import java.sql.Connection
import java.util.*

object Comments : Table("May2015") {
    val id = text("id").primaryKey()
    val body = text("body")
}

fun main() {
    val properties = Properties()
    properties.setProperty("annotators", "tokenize,ssplit")
    val pipeline = StanfordCoreNLP(properties)
    val languageDetector = LanguageDetector.getDefaultLanguageDetector()
    languageDetector.loadModels()

    val db = Database.connect("jdbc:sqlite:./training/data/ingredient-classifier/comments/database.sqlite", driver = "org.sqlite.JDBC")
    transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE, repetitionAttempts = 0) {
        val total = Comments.selectAll().count()
        val pageSize = 100
        val pages = total / pageSize
        var comments = 0
        val maxComments = 151_000

        val writer = FileWriter("./training/data/ingredient-classifier/comments/comments.txt", false)

//        val thign = Comments.select { Comments.id.eq("cquhkh9") }.first()
//        println(thign[Comments.body])

        loop@ for (page in 0..pages) {
            val query = Comments.select {
                Comments.body.notLike("%\n%")
                    .and(Comments.body.notLike("%\r%"))
            }.limit(pageSize, pageSize * page)
                .filterNot{ it[Comments.body].contains("\n") }

            for (comment in query) {
                val body = comment[Comments.body]

                val language = languageDetector.detectAll(body).firstOrNull()?.language

                val document = CoreDocument(body)
                pipeline.annotate(document)

                if (language == "en" && document.sentences().size == 1) {
//                    writer.appendln(body)
                    comments++

                    if (comments % 1000 == 0) {
                        writer.flush()
                    }

                    if (comments >= maxComments) {
                        break@loop
                    }
                }
            }
        }

        writer.flush()
    }
}