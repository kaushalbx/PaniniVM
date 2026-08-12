package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MemoryOrderQualifierTest {

    @Test
    fun `recognizes typed purva qualifier before target`() {
        val target = subanta("फल", "अम्")
        assertTrue(MemoryOrderQualifierResolver.before(target, listOf(subanta("पूर्व", "अम्"), target)).previous)
        assertFalse(MemoryOrderQualifierResolver.before(target, listOf(subanta("उत्तर", "अम्"), target)).previous)
    }

    private fun subanta(stem: String, sup: String) = SubantaPada(
        sourceText = "$stem + $sup",
        pratipadika = MulaPratipadika(stem, stem),
        sup = SupPratyaya(sup, sup),
    )
}
