package dev.panini.sutra

import dev.panini.analysis.SamasaMorphologicalFeature
import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.ashtadhyayi.adhyaya2.pada2.*
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertFalse

class SamasaPada2NearMissTest {
    @Test
    fun `genitive prohibitions require their own semantic cause`() {
        val ordinary = context("राजन्", "मत", SamasaType.TATPURUSA, Vibhakti.SASTHI)
        listOf(
            NaNirdhareSutra,
            KtenaChAPujayamSutra,
            AdhikaranavacinasChaSutra,
            KarmaniChaSutra,
            TrjjakabhyamKartariSutra,
            KartariChaSutra,
        ).forEach { assertFalse(it.matches(ordinary), "${it.number} accepted an unlicensed genitive") }

        val agentWithoutKrt = ordinary.copy(semanticRelations = setOf(SamasaSemanticRelation.AGENT_RELATION))
        assertFalse(KartariChaSutra.matches(agentWithoutKrt))
        val krtWithoutAgent = ordinary.copy(
            padas = listOf(
                ordinary.purvaPada,
                ordinary.uttaraPada.copy(morphologicalFeatures = setOf(SamasaMorphologicalFeature.KRIT_DERIVED)),
            ),
        )
        assertFalse(KartariChaSutra.matches(krtWithoutAgent))
    }

    @Test
    fun `special ordering rules reject neighboring compounds`() {
        assertFalse(RajadantadisuSutra.matches(context("राज", "पुरुष", SamasaType.TATPURUSA)))
        assertFalse(SaptamiVisesaneBahuvrihauSutra.matches(context("कण्ठ", "काल", SamasaType.BAHUVRIHI)))
        assertFalse(NisthaBahuvrihauSutra.matches(context("नील", "कण्ठ", SamasaType.BAHUVRIHI)))
        assertFalse(VahitagnyadisuSutra.matches(context("अग्नि", "पुरुष", SamasaType.BAHUVRIHI)))
        assertFalse(KadaradahKarmadharayeSutra.matches(context("नील", "उत्पल", SamasaType.KARMADHARAYA)))
    }

    private fun context(
        purva: String,
        uttara: String,
        type: SamasaType,
        purvaVibhakti: Vibhakti = Vibhakti.PRATHAMA,
    ) = SamasaRuleContext(
        padas = listOf(SamasaPada(purva, purvaVibhakti), SamasaPada(uttara)),
        samasaType = type,
    )
}
