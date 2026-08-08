package dev.panini.execution.binding

import kotlin.test.Test
import kotlin.test.assertEquals

class ActionStemNormalizerTest {

    @Test
    fun `normalizes fused compatibility prefixes and terminal markers`() {
        assertEquals("योज", ActionStemNormalizer.normalize("निप्रयोजनम्"))
        assertEquals("गण", ActionStemNormalizer.normalize("प्रगणनम्"))
    }

    @Test
    fun `preserves established prefix ordering`() {
        assertEquals("नियोज", ActionStemNormalizer.normalize("प्रनियोजनम्"))
    }
}
