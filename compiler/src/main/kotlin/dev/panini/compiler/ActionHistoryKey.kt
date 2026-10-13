package dev.panini.compiler

import dev.panini.core.ActionIdentity

/** Opaque JVM transport key; grammatical analysis keeps the structured identity. */
internal fun ActionIdentity.compiledHistoryKey(): String =
    "@action:${dhatupathaId.length}:$dhatupathaId/" +
        upasargas.joinToString(",") { it.name } + "/" + sanadi.joinToString(",") { it.name }
