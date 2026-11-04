package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals

class NominalGovernanceInferenceTest {
    @Test fun `sadhu nipuna requires respect and excludes linked prati pari anu`() {
        val respect = setOf(SemanticRelation.ARCHA_RESPECT)
        for (stem in listOf("साधु", "निपुण")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(stem, cases, respect, voice)
                assertEquals(listOf("2.3.43"), result.evidence.map { it.sutra })
                assertEquals(Vibhakti.SAPTAMI, result.resolvedVibhakti)
                assertEquals(Karaka.ANIRDHARITA, result.resolved)
                assertEquals(null, resolve(stem, setOf(Vibhakti.SASTHI), respect, voice).resolvedVibhakti)
            }
            assertEquals(null, resolve(stem, setOf(Vibhakti.SAPTAMI)).resolvedVibhakti)
            assertEquals(listOf("2.3.50"), resolve(stem, setOf(Vibhakti.SASTHI),
                setOf(SemanticRelation.SESHA_RELATION)).evidence.map { it.sutra })
            for (particle in listOf(SemanticRelation.PRATI_KARMAPRAVACANIYA_LINK,
                SemanticRelation.PARI_KARMAPRAVACANIYA_LINK, SemanticRelation.ANU_KARMAPRAVACANIYA_LINK)) {
                val linked = respect + particle + SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE
                val result = resolve(stem, setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI), linked)
                assertEquals(listOf("2.3.8"), result.evidence.map { it.sutra }, particle.name)
                assertEquals(Vibhakti.DVITIYA, result.resolvedVibhakti)
                assertEquals(null, resolve(stem, setOf(Vibhakti.SAPTAMI), linked).resolvedVibhakti)
            }
        }
        assertEquals(null, resolve(null, setOf(Vibhakti.SAPTAMI), respect).resolvedVibhakti)
        assertEquals(null, resolve("कुशल", setOf(Vibhakti.SAPTAMI), respect).resolvedVibhakti)
        assertEquals(null, resolve(null, setOf(Vibhakti.SAPTAMI)).resolvedVibhakti)
    }

    @Test fun `prasita utsuka preserves instrumental locative choices only in attachment sense`() {
        val attached = setOf(SemanticRelation.ENGROSSED_ATTACHMENT)
        val allowed = setOf(Vibhakti.TRTIYA, Vibhakti.SAPTAMI)
        for (stem in listOf("प्रसित", "उत्सुक")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                for (case in allowed) {
                    val result = resolve(stem, setOf(case), attached, voice)
                    assertEquals(Karaka.ANIRDHARITA, result.resolved)
                    assertEquals(case, result.resolvedVibhakti)
                    assertEquals(listOf("2.3.44"), result.evidence.map { it.sutra })
                    assertEquals(allowed - case, result.alternatives.map { it.vibhakti }.toSet())
                    assertEquals(false, result.alternatives.single().matchesObservedMorphology)
                }
            }
            val ambiguous = resolve(stem, allowed, attached)
            assertEquals(null, ambiguous.resolvedVibhakti)
            assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
            assertEquals(null, resolve(stem, allowed).resolvedVibhakti)
            assertEquals(null, resolve(stem, setOf(Vibhakti.SASTHI), attached).resolvedVibhakti)
        }
        assertEquals(null, resolve(null, allowed, attached).resolvedVibhakti)
        assertEquals(null, resolve("अज्ञात", allowed, attached).resolvedVibhakti)
    }

    private val cases = setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI)
    private fun governor(stem: String) = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya("सुँ", "सुँ"))
    private fun resolve(stem: String?, observed: Set<Vibhakti> = cases,
        relations: Set<SemanticRelation> = emptySet(), voice: Prayoga = Prayoga.KARTARI,
        extra: List<ParticipantFacts> = emptyList()): KarakaResolution {
        val p = ParticipantFacts("go", AvyayaPada("गवोः", "गवोः"), observed,
            semanticRelations = relations, governingNominal = stem?.let(::governor))
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अस्"), p, listOf(p) + extra, voice))
    }

    @Test fun `all seven specified governors retain genitive locative options without adhikarana designation`() {
        for (stem in listOf("स्वामिन्", "ईश्वर", "अधिपति", "दायाद", "साक्षिन्", "प्रतिभू", "प्रसूत")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                for (case in cases) {
                    val result = resolve(stem, setOf(case), voice = voice)
                    assertEquals(Karaka.ANIRDHARITA, result.resolved, stem)
                    assertEquals(case, result.resolvedVibhakti, stem)
                    assertEquals(listOf("2.3.39"), result.evidence.map { it.sutra }, stem)
                    assertEquals(cases - case, result.alternatives.map { it.vibhakti }.toSet(), stem)
                    assertEquals(false, result.alternatives.single().matchesObservedMorphology)
                }
            }
            val syncretic = resolve(stem)
            assertEquals(Karaka.ANIRDHARITA, syncretic.resolved)
            assertEquals(null, syncretic.resolvedVibhakti)
            assertEquals(true, syncretic.alternatives.single().matchesObservedMorphology)
        }
    }

    @Test fun `ownership alone synonyms and unrelated word presence cannot license special locative`() {
        assertEquals(null, resolve(null, relations = setOf(SemanticRelation.OWNERSHIP_SWAMIN)).resolvedVibhakti)
        for (stem in listOf("राजन्", "नृप", "स्वामी", "अस्वामिन्")) {
            assertEquals(null, resolve(stem, setOf(Vibhakti.SAPTAMI)).resolvedVibhakti, stem)
        }
        val unlinked = ParticipantFacts("owner", governor("स्वामिन्"), setOf(Vibhakti.PRATHAMA))
        assertEquals(null, resolve(null, extra = listOf(unlinked)).resolvedVibhakti)
        assertEquals(null, resolve("स्वामिन्", setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
        // A relation outside the specified lexical domain still has ordinary śeṣa genitive.
        val ordinary = resolve("राजन्", setOf(Vibhakti.SASTHI), setOf(SemanticRelation.SESHA_RELATION))
        assertEquals(Karaka.SAMBANDHA, ordinary.resolved)
        assertEquals(listOf("2.3.50"), ordinary.evidence.map { it.sutra })
    }

    @Test fun `ayukta kusala requires aseva as well as an established nominal link`() {
        val aseva = setOf(SemanticRelation.ENGROSSED_ATTACHMENT)
        for (stem in listOf("आयुक्त", "कुशल")) {
            for (case in cases) {
                val result = resolve(stem, setOf(case), aseva)
                assertEquals(listOf("2.3.40"), result.evidence.map { it.sutra })
                assertEquals(case, result.resolvedVibhakti)
                assertEquals(cases - case, result.alternatives.map { it.vibhakti }.toSet())
            }
            assertEquals(null, resolve(stem, relations = aseva).resolvedVibhakti)
            assertEquals(null, resolve(stem).resolvedVibhakti)
            val ordinaryLocus = resolve(stem, setOf(Vibhakti.SAPTAMI), setOf(SemanticRelation.LOCATION))
            assertEquals(Karaka.ADHIKARANA, ordinaryLocus.resolved)
            assertEquals(listOf("1.4.45", "2.3.36"), ordinaryLocus.evidence.map { it.sutra })
        }
        assertEquals(null, resolve(null, relations = aseva).resolvedVibhakti)
        assertEquals(null, resolve("निपुण", relations = aseva).resolvedVibhakti)
    }

    @Test fun `a derivational change to the governor does not inherit its lexical license`() {
        val derived = governor("स्वामिन्").copy(pratipadika = MulaPratipadika("स्वामिन् + त्व", "स्वामिन्",
            listOf(dev.panini.vyakaranam.ast.TaddhitaVikara("त्व", "त्व"))))
        val p = ParticipantFacts("go", AvyayaPada("गवोः", "गवोः"), cases, governingNominal = derived)
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अस्"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, result.resolvedVibhakti)
        assertEquals(emptyList(), result.evidence)
    }
}
