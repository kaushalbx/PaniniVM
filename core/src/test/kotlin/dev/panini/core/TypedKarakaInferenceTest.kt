package dev.panini.core

import dev.panini.analysis.KarakaInference
import kotlin.test.Test
import kotlin.test.assertEquals

class TypedKarakaInferenceTest {
    @Test fun `typed syncretic slots preserve all active roles`() {
        assertEquals(setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates(SupAffix.candidates("भ्याम्"), Prayoga.KARTARI))
        assertEquals(setOf(Karaka.SAMBANDHA, Karaka.ADHIKARANA),
            KarakaInference.candidates(SupAffix.candidates("ओस्"), Prayoga.KARTARI))
    }

    @Test fun `typed inference respects voice and transitivity without reexpanding selected slots`() {
        assertEquals(setOf(Karaka.KARTR),
            KarakaInference.candidates(listOf(SupAffix.BHYAM_3), Prayoga.KARMANI))
        assertEquals(setOf(Karaka.SAMPRADANA),
            KarakaInference.candidates(listOf(SupAffix.BHYAM_4), Prayoga.KARMANI))
        assertEquals(emptySet(), KarakaInference.candidates(listOf(SupAffix.AM), Prayoga.KARTARI, false))
        assertEquals(emptySet(), KarakaInference.candidates(emptyList(), Prayoga.KARTARI))
    }

    @Test fun `source boundary and typed inference agree for every ending and voice`() {
        for (ending in SupAffix.entries.map { it.upadesha }.distinct() + "ङसिँ" + "unknown") {
            for (voice in Prayoga.entries) for (transitive in listOf(true, false)) {
                assertEquals(KarakaInference.candidates(ending, voice, transitive),
                    KarakaInference.candidates(SupAffix.candidates(ending), voice, transitive),
                    "$ending $voice $transitive")
            }
        }
    }
}
