package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.DhatuPrakriti

class DhatuCacheGanaResolutionTest {
    @Test
    fun `base root vikarana cannot override an active causative stem`() {
        val form = PaniniParser().parse("एक + अम् युज् + णिच् + श्नम् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas.filterIsInstance<TingantaPada>().single()
        assertNull(DhatuCache.resolve(form))
        assertEquals(form.sourceText, dev.panini.execution.PvmUktiSadhaka().sadhayaTinganta(form))
        assertIs<dev.panini.execution.ExecutionResult.Failure>(dev.panini.execution.PaniniVM().eval(
            "एक + अम् युज् + णिच् + श्नम् + लोट् + सिप् ।"))
    }
    @Test
    fun `causative shap does not reclassify the original root as bhvadi`() {
        for (root in listOf("युज्", "गण", "मुद्र्")) {
            val parser = PaniniParser()
            val implicit = parser.parse("एक + अम् $root + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<TingantaPada>().single()
            val explicit = implicit.copy(vikarana = dev.panini.vyakaranam.ast.Vikarana.SHAP)
            val expected = kotlin.test.assertNotNull(DhatuCache.resolve(implicit), root)
            assertEquals(expected.id, DhatuCache.resolve(explicit)?.id, root)
        }
    }

    @Test
    fun `root collisions never choose a registration-order winner`() {
        val byRoot = DhatuPatha.all.filter { it.operations.isNotEmpty() }.flatMap { dhatu ->
            listOf(dhatu.upadesha, dhatu.sourceSurface, dhatu.derivationalSurface)
                .flatMap { listOf(it, it.trimEnd('्', 'ँ')) }.distinct().map { it to dhatu }
        }.groupBy({ it.first }, { it.second })
        byRoot.forEach { (root, entries) ->
            val identities = entries.map { it.id }.distinct()
            val resolved = DhatuCache.resolve(DhatuPrakriti(sourceText = root, mulaDhatu = root))
            if (identities.size > 1) assertNull(resolved, "$root: $identities")
            else assertEquals(identities.single(), resolved?.id, root)
        }
    }

    @Test
    fun `source roots do not accept inflected or nominal aliases`() {
        listOf("तिष्ठति", "तिष्ठ", "स्थानम्", "स्थितिः").forEach { surface ->
            val tinganta = PaniniParser().parse("फल + अम् $surface + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<TingantaPada>().single()
            assertNull(DhatuCache.resolve(tinganta), surface)
            assertNull(DhatuCache.resolve(tinganta.dhatu), surface)
        }
    }

    @Test
    fun `canonical source roots and upadeshas remain accepted`() {
        listOf("स्था", "स्थाञँ").forEach { root ->
            val tinganta = PaniniParser().parse("फल + अम् $root + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<TingantaPada>().single()
            assertEquals("01.9901", DhatuCache.resolve(tinganta)?.id)
            assertEquals("01.9901", DhatuCache.resolve(tinganta.dhatu)?.id)
        }
    }

    @Test
    fun `explicit divadi vikarana selects executable lexical entry`() {
        val tinganta = PaniniParser()
            .parse("अक्ष + अम् दिव् + श्यन् + लोट् + सिप् ।")
            .grammaticalVakyas()
            .single()
            .padas
            .filterIsInstance<TingantaPada>()
            .single()

        assertEquals("04.9901", DhatuCache.resolve(tinganta)?.id)
        assertIs<dev.panini.execution.ExecutionResult.Success>(
            dev.panini.execution.PaniniVM().eval("अक्ष + अम् दिव् + श्यन् + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `contradictory vikarana does not fall back to an unrelated gana`() {
        val tinganta = PaniniParser().parse("फल + अम् स्था + श्नु + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas.filterIsInstance<TingantaPada>().single()
        assertNull(DhatuCache.resolve(tinganta))
    }
}
