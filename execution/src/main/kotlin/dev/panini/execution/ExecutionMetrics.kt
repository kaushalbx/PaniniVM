package dev.panini.execution

import java.util.concurrent.atomic.AtomicLong

/** Optional low-overhead counters describing interpreter work performed by one VM. */
class ExecutionMetrics {
    private val parsedFilesCounter = AtomicLong()
    private val parsedSentencesCounter = AtomicLong()
    private val projectCacheHitsCounter = AtomicLong()
    private val projectCacheMissesCounter = AtomicLong()
    private val executedAstNodesCounter = AtomicLong()
    private val prakriyaCallsCounter = AtomicLong()

    fun snapshot(): ExecutionMetricsSnapshot = ExecutionMetricsSnapshot(
        parsedFiles = parsedFilesCounter.get(),
        parsedSentences = parsedSentencesCounter.get(),
        projectCacheHits = projectCacheHitsCounter.get(),
        projectCacheMisses = projectCacheMissesCounter.get(),
        executedAstNodes = executedAstNodesCounter.get(),
        prakriyaCalls = prakriyaCallsCounter.get(),
        renderedOrReparsedSources = 0L,
    )

    fun reset() {
        parsedFilesCounter.set(0L)
        parsedSentencesCounter.set(0L)
        projectCacheHitsCounter.set(0L)
        projectCacheMissesCounter.set(0L)
        executedAstNodesCounter.set(0L)
        prakriyaCallsCounter.set(0L)
    }

    internal fun recordParsedFile() = parsedFilesCounter.incrementAndGet()
    internal fun recordParsedSentences(count: Int) = parsedSentencesCounter.addAndGet(count.toLong())
    internal fun recordProjectCacheHit() = projectCacheHitsCounter.incrementAndGet()
    internal fun recordProjectCacheMiss() = projectCacheMissesCounter.incrementAndGet()
    internal fun recordAstNode() = executedAstNodesCounter.incrementAndGet()
    internal fun recordPrakriyaCall() = prakriyaCallsCounter.incrementAndGet()
}

data class ExecutionMetricsSnapshot(
    val parsedFiles: Long,
    val parsedSentences: Long,
    val projectCacheHits: Long,
    val projectCacheMisses: Long,
    val executedAstNodes: Long,
    val prakriyaCalls: Long,
    /** Must remain zero: execution consumes stored AST rather than rendered source. */
    val renderedOrReparsedSources: Long,
)
