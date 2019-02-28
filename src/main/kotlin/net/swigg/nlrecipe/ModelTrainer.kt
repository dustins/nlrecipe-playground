package net.swigg.nlrecipe

import edu.emory.mathcs.nlp.tokenization.EnglishTokenizer
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.simple.Sentence
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.SentenceIterator
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory
import java.io.InputStream
import java.nio.file.Paths
import java.util.*

fun main() {
    val properties = Properties()
    properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse")
    val annotator = StanfordCoreNLP(properties)

    val nlp4jTokenizer = EnglishTokenizer()
    val trainPath = Paths.get("./training/data/ingredient-classifier/ingredients/ingredients.train")
    val lines = trainPath.toFile().readLines().map { it.split("\t", limit = 2) }

    val vectorizer = Word2Vec.Builder()
        .minWordFrequency(5)
        .iterations(1)
        .windowSize(8)
        .layerSize(200)
        .iterate(TrainingIterator(lines.map { Pair(it[0], it[1]) }))
        .tokenizerFactory(TrainingTokenizerFactory(annotator))
//        .allowParallelTokenization(false)
        .build()
    vectorizer.fit()

    for ((label, text) in lines) {
        val nlp4jTokens = nlp4jTokenizer.segmentize(text)

        val corenlpDocument = CoreDocument(text)
        annotator.annotate(corenlpDocument)

        println(nlp4jTokens)
        println(corenlpDocument.tokens())
    }
}

class TrainingTokenizer(private val labels: List<CoreLabel>) : Tokenizer {
    var sequence = labels.asSequence().iterator()

    override fun hasMoreTokens(): Boolean {
        return sequence.hasNext()
    }

    override fun countTokens(): Int {
        return labels.count()
    }

    override fun nextToken(): String {
        return sequence.next().value()
    }

    override fun setTokenPreProcessor(tokenPreProcessor: TokenPreProcess?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokens(): MutableList<String> {
        return labels.map { it.value() }.toMutableList()
    }
}

class TrainingTokenizerFactory(val annotator: StanfordCoreNLP) : TokenizerFactory {
    override fun setTokenPreProcessor(preProcessor: TokenPreProcess?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokenPreProcessor(): TokenPreProcess? {
        return null
    }

    override fun create(toTokenize: String?): Tokenizer {
        val sentence = Sentence(toTokenize)
//        val document = CoreDocument(toTokenize)
//        annotator.annotate(document)
        return TrainingTokenizer(sentence.asCoreLabels())
    }

    override fun create(toTokenize: InputStream?): Tokenizer {
        return create(toTokenize?.bufferedReader().toString())
    }
}

class TrainingIterator(private val pairs: List<Pair<String, String>>) : SentenceIterator {
    var sequence = pairs.iterator()

    override fun setPreProcessor(preProcessor: SentencePreProcessor?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finish() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reset() {
        sequence = pairs.iterator()
    }

    override fun nextSentence(): String? {
        return if (sequence.hasNext()) {
            sequence.next().second
        } else {
            null
        }
    }

    override fun hasNext(): Boolean {
        return sequence.hasNext()
    }

    override fun getPreProcessor(): SentencePreProcessor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}