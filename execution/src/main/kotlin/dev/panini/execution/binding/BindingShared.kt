package dev.panini.execution.binding

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaGenerator

/**
 * Package-level singletons for the binding layer.
 *
 * [SankhyaEvaluator] and [SankhyaGenerator] are stateless; a single shared
 * instance avoids the cost of constructing identical objects in every object
 * that needs numeral evaluation or generation.
 */
internal val sharedSankhyaEvaluator = SankhyaEvaluator()
internal val sharedSankhyaGenerator = SankhyaGenerator()
