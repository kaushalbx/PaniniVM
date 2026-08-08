package dev.panini.execution.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class KriyaMemorySourceTest {

    @Test
    fun `normalizes cache source with one terminal danda`() {
        assertEquals("एक + अम् कृ + लोट् + सिप् ।", KriyaMemorySource.normalize("  एक + अम् कृ + लोट् + सिप्  "))
        assertEquals("एक + अम् कृ + लोट् + सिप् ।", KriyaMemorySource.normalize(" एक + अम् कृ + लोट् + सिप् । "))
    }
}
