package dev.sanskrit.sutra

abstract class BaseSutra<C, R>(
    override val metadata: SutraMetadata,
) : Sutra<C, R> {
    override fun toString(): String = metadata.toString()
}
