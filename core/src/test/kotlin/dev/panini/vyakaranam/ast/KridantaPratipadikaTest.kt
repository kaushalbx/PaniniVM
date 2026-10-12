package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KridantaPratipadikaTest {

    @Test
    fun `classifies exact kta identity`() {
        assertEquals(KrtPratyayaIdentity.KTA, kridanta("क्त").krtPratyayaIdentity)
        assertEquals(dev.panini.core.KrtAffix.KTAVATU, kridanta("क्तवतुँ").krtPratyayaIdentity)
        assertEquals(dev.panini.core.KrtAffix.LYUT, kridanta("ल्युट्").krtPratyayaIdentity)
    }

    @Test
    fun `classifies exact ghan identity`() {
        assertEquals(KrtPratyayaIdentity.GHAN, kridanta("घञ्").krtPratyayaIdentity)
        assertEquals(dev.panini.core.KrtAffix.LYUT, kridanta("ल्युट्").krtPratyayaIdentity)
    }

    @Test
    fun `classifies canonical derived lexical identities`() {
        assertEquals(
            KridantaLexicalIdentity.ADHIKARA,
            kridanta("घञ्", listOf("अधि"), "कृ").lexicalIdentity,
        )
        assertEquals(
            KridantaLexicalIdentity.APAVADA,
            kridanta("घञ्", listOf("अप"), "वद्").lexicalIdentity,
        )
        assertNull(kridanta("ल्युट्", listOf("अधि"), "कृ").lexicalIdentity)
        assertNull(kridanta("घञ्", listOf("अधि"), "वद्").lexicalIdentity)
    }

    @Test fun `all supported krt identities share one catalog and unknowns remain unknown`() {
        for (affix in dev.panini.core.KrtAffix.entries) {
            val stem = kridanta(affix.upadesha)
            assertEquals(affix, stem.krtAffix)
            assertEquals(stem.krtAffix, stem.krtPratyayaIdentity)
        }
        assertNull(kridanta("unknown").krtPratyayaIdentity)
    }

    @Test fun `derived roots and unknown extra prefixes cannot impersonate lexical action nouns`() {
        val stem = kridanta("घञ्", listOf("अधि"), "कृ")
        assertEquals(KridantaLexicalIdentity.ADHIKARA, stem.copy(upasargas = listOf(" अधि ")).lexicalIdentity)
        assertNull(stem.copy(upasargas = listOf("unknown", "अधि")).lexicalIdentity)
        assertNull(stem.copy(dhatu = stem.dhatu.copy(sanadiPratyayas = listOf("णिच्"))).lexicalIdentity)
        assertNull(stem.copy(dhatu = stem.dhatu.copy(sanadiPratyayas = listOf("unknown"))).lexicalIdentity)
    }

    private fun kridanta(
        pratyaya: String,
        upasargas: List<String> = emptyList(),
        dhatu: String = "सिध्",
    ) = KridantaPratipadika(
        sourceText = (upasargas + dhatu + pratyaya).joinToString(" + "),
        upasargas = upasargas,
        dhatu = DhatuPrakriti(dhatu, dhatu),
        krtPratyaya = pratyaya,
    )
}
