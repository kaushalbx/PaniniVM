package dev.sanskrit.sandhi

import dev.sanskrit.sutra.Ashtadhyayi

fun main(args: Array<String>) {
    if (args.singleOrNull() == "--coverage") {
        println("Ashtadhyayi sutra count: ${Ashtadhyayi.expectedSutraCount}")
        println("Pathita sutras: ${Ashtadhyayi.pathitaCount}")
        println("Kriyavat sutras: ${Ashtadhyayi.kriyavatCount}")
        println("Remaining sutras: ${Ashtadhyayi.remainingCount}")
        return
    }

    val words = args.flatMap { it.split(Regex("\\s+")) }.filter { it.isNotBlank() }
    if (words.size < 2) {
        println("Usage: gradle run --args=\"राम इति\"")
        println("Coverage: gradle run --args=\"--coverage\"")
        return
    }

    val result = SandhiEngine().join(words)
    println(result.output)

    if (result.applications.isNotEmpty()) {
        println()
        println("सूत्र:")
        result.applications.forEach {
            println("${it.sutra} ${it.sutraText}: ${it.before} -> ${it.after}")
            println("हिन्दी: ${it.hindiVyakhya}")
        }
    }
}
