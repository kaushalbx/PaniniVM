package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ActionHistoryMorphologyParityTest {
    @Test fun `unrelated krt meanings cannot read an available completed actions result or participants`() {
        for ((index, ending) in listOf("क्त", "क्तवतुँ", "तृच्", "ण्वुल्").withIndex()) {
            for (referent in listOf("फल", "कर्मन्")) {
                val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
                    "युज् + $ending + ङस् $referent + अम् मुद्र् + णिच् + लोट् + सिप् ।"
                val results = PaniniVM().evalScript(source)
                assertIs<ExecutionResult.Failure>(results.last(), "$ending $referent: $results")
                assertFails("$ending $referent must not alias completed action history") {
                    BytecodeCompiler.compileAndLoad(source, "InvalidHistoryKrt${index}${if (referent == "फल") "Result" else "Participant"}")
                }
            }
        }
    }
}
