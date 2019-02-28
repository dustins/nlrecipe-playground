import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "net.swigg"
version = "0.0.1-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("org.apache.tika:tika-core:1.20")
    compile("org.apache.tika:tika-langdetect:1.20")
    compile("edu.emory.mathcs.nlp:nlp4j-tokenization:1.1.+")
    compile("org.deeplearning4j:deeplearning4j-nlp:1.0.0-beta3")
    compile("org.nd4j:nd4j-native-platform:1.0.0-beta3")
    compile("edu.stanford.nlp:stanford-corenlp:3.9.+")
    compile("edu.stanford.nlp:stanford-corenlp:3.9.+:models")
    compile("com.opencsv:opencsv:4.4")
    compile("org.jetbrains.exposed:exposed:0.12.+")
    compile("org.xerial:sqlite-jdbc:3.25.+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}