package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KridantaPratipadikaTest {

    @Test
    fun `classifies exact kta identity`() {
        assertEquals(KrtPratyayaIdentity.KTA, kridanta("क्त").krtPratyayaIdentity)
        assertNull(kridanta("क्तवतुँ").krtPratyayaIdentity)
        assertNull(kridanta("ल्युट्").krtPratyayaIdentity)
    }

    @Test
    fun `classifies exact ghan identity`() {
        assertEquals(KrtPratyayaIdentity.GHAN, kridanta("घञ्").krtPratyayaIdentity)
        assertNull(kridanta("ल्युट्").krtPratyayaIdentity)
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
