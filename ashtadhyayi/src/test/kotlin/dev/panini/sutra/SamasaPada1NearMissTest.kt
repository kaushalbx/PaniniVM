package dev.panini.sutra

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.ashtadhyayi.adhyaya2.pada1.AnurYatSamayaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.AnyapadartheChaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.ChatuspadoGarbhinyaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.KumarahShramanadibhihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.SrenyadayahKrtadibhihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.YasyaChayamahSutra
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertFalse

class SamasaPada1NearMissTest {
    @Test
    fun `anu proximity and extent rules remain semantically distinct`() {
        val proximity = context("अनु", "वन")
        val extent = context(
            "अनु",
            "गङ्गा",
            semanticRelations = setOf(SamasaSemanticRelation.MEASURE_DIMENSION),
        )

        assertFalse(YasyaChayamahSutra.matches(proximity))
        assertFalse(AnurYatSamayaSutra.matches(extent))
    }

    @Test
    fun `lexically restricted pada one rules reject adjacent vocabulary`() {
        assertFalse(AnyapadartheChaSutra.matches(context("पञ्च", "वन")))
        assertFalse(SrenyadayahKrtadibhihSutra.matches(context("ग्राम", "कृत", SamasaType.KARMADHARAYA)))
        assertFalse(KumarahShramanadibhihSutra.matches(context("कुमार", "राज्ञी", SamasaType.KARMADHARAYA)))
        assertFalse(ChatuspadoGarbhinyaSutra.matches(context("गो", "धेनु", SamasaType.KARMADHARAYA)))
    }

    private fun context(
        purva: String,
        uttara: String,
        type: SamasaType = SamasaType.AVYAYIBHAVA,
        semanticRelations: Set<SamasaSemanticRelation> = emptySet(),
    ) = SamasaRuleContext(
        padas = listOf(SamasaPada(purva), SamasaPada(uttara)),
        samasaType = type,
        semanticRelations = semanticRelations,
    )
}
