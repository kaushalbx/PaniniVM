package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.parser.PaniniParser

/** Compatibility facade retained while callers migrate to [PaniniParser]. */
object PurvaparaPipelineCompiler {
    private val parser = PaniniParser()

    fun compile(source: String): Pipeline? = parser.parseOrNull(source)?.body as? Pipeline
}
