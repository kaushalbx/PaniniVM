package dev.sanskrit.sutra

interface Sutra<C, R> {
    val metadata: SutraMetadata

    val sutra: String
        get() = metadata.sutraNumber

    val sutraText: String
        get() = metadata.sutraText

    val hindiVyakhya: String
        get() = metadata.hindiVyakhya

    val krama: Int
        get() = metadata.krama

    fun matches(context: C): Boolean

    fun apply(context: C): R
}
