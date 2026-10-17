package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import kotlin.test.*

class CorpusPronounProvenanceTest {
    @Test fun `kim whole substitute uses exact varnas and preserves annotation and lexical identity`() {
        val rule = dev.panini.ashtadhyayi.adhyaya7.pada2.KimahKahSutra
        val stem = DerivationTerm("renamed-stem", "किँम्ऽ", TermKind.PRATIPADIKA, upadesha = "किम्")
        val initial = DerivationState(listOf(stem, dev.panini.core.SupAffix.SU.term("renamed-su")))
        assertTrue(rule.matches(initial))
        val result = rule.apply(initial).state
        val changed = result.terms.first()
        assertEquals("कँऽ", changed.surface)
        assertEquals(stem.id, changed.id)
        assertEquals(stem.upadesha, changed.upadesha)
        assertEquals(initial.terms.last(), result.terms.last())
        assertEquals(0, result.substitutions.single().sourceVarnaIndex)
        assertEquals(stem.varnas, result.substitutions.single().originalVarnas)
        assertEquals(listOf(dev.panini.shiksha.Vyanjana.KA, dev.panini.shiksha.Svara.A),
            result.substitutions.single().replacementVarnas)
        assertFalse(rule.matches(result))
        assertFalse(rule.matches(initial.copy(terms = listOf(stem,
            DerivationTerm("sup-su", "सुँ", TermKind.PRATYAYA)))))
        assertFalse(rule.matches(initial.copy(terms = listOf(stem.copy(upadesha = "अन्य"), initial.terms.last()))))
    }
    @Test fun `derived a stem and affix homographs do not acquire lexical avyaya status`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada1.SvaradiNipatamAvyayamSutra
        assertFalse(rule.matches(DerivationState(listOf(DerivationTerm("stem", "अ", TermKind.PRATIPADIKA, upadesha = "इदम्")))))
        assertFalse(rule.matches(DerivationState(listOf(dev.panini.core.SupAffix.AM_6.term("suffix")))))
        assertTrue(rule.matches(DerivationState(listOf(DerivationTerm("word", "स्वर्", TermKind.PRATIPADIKA)))))
    }
    @Test fun `all idam genders and case slots are forward verified before corpus admission`() {
        val masculine = listOf("अयम्", "इमौ", "इमे", "इमम्", "इमौ", "इमान्", "अनेन", "आभ्याम्", "एभिः",
            "अस्मै", "आभ्याम्", "एभ्यः", "अस्मात्", "आभ्याम्", "एभ्यः", "अस्य", "अनयोः", "एषाम्", "अस्मिन्", "अनयोः", "एषु")
        val feminine = listOf("इयम्", "इमे", "इमाः", "इमाम्", "इमे", "इमाः", "अनया", "आभ्याम्", "आभिः",
            "अस्यै", "आभ्याम्", "आभ्यः", "अस्याः", "आभ्याम्", "आभ्यः", "अस्याः", "अनयोः", "आसाम्", "अस्याम्", "अनयोः", "आसु")
        val neuter = listOf("इदम्", "इमे", "इमानि", "इदम्", "इमे", "इमानि") + masculine.drop(6)
        val failures = mutableListOf<String>()
        for ((linga, forms) in listOf(Linga.PUMS to masculine, Linga.STRI to feminine, Linga.NAPUMSAKA to neuter))
            for ((index, affix) in dev.panini.core.SupAffix.entries.withIndex()) {
                runCatching {
                    val result = SubantaEngine().derive(SubantaDerivationRequest("इदम्", affix.vibhakti, affix.vacana, linga))
                    assertEquals(forms[index], result.final.surface, "$linga $affix: " + result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
                    result.final.requireCompleteItProcessing()
                }.onFailure { failures += it.message.orEmpty() }
            }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }
    @Test fun `idam masculine oblique cases follow independently prescribed prefix operations`() {
        val expected = listOf("अनेन", "आभ्याम्", "एभिः", "अस्मै", "आभ्याम्", "एभ्यः",
            "अस्मात्", "आभ्याम्", "एभ्यः", "अस्य", "अनयोः", "एषाम्", "अस्मिन्", "अनयोः", "एषु")
        val failures = mutableListOf<String>()
        for ((index, affix) in dev.panini.core.SupAffix.entries.drop(6).withIndex()) {
            runCatching {
            val result = SubantaEngine().derive(SubantaDerivationRequest("इदम्", affix.vibhakti, affix.vacana, Linga.PUMS))
            assertEquals(expected[index], result.final.surface, "$affix: " + result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
            assertTrue(result.applications.any { it.sutra in setOf("7.2.112", "7.2.113") })
            result.final.requireCompleteItProcessing()
            }.onFailure { failures += "$affix: ${it.message}" }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
        for (number in listOf("7.2.112", "7.2.113"))
            assertTrue(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras.any { it.sutra == number })
    }
    @Test fun `idam identity retention preserves annotations and rejects wrong suffix domains`() {
        val retention = dev.panini.ashtadhyayi.adhyaya7.pada2.IdamoMahSutra
        val feminine = dev.panini.ashtadhyayi.adhyaya7.pada2.YahSauSutra
        val original = DerivationState(listOf(
            DerivationTerm("stem", "इदँम्ऽ", TermKind.PRATIPADIKA, upadesha = "इदम्"),
            dev.panini.core.SupAffix.SU.term("renamed")),
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)))
        assertTrue(retention.matches(original))
        assertFalse(feminine.matches(original))
        val retained = retention.apply(original).state
        assertEquals(original.terms.first().surface, retained.terms.first().surface)
        assertEquals(original.terms.first().phonologicalText.effectiveVarnas,
            retained.terms.first().phonologicalText.effectiveVarnas)
        assertEquals(3, retained.substitutions.single().sourceVarnaIndex)
        assertFalse(retention.matches(retained))
        assertTrue(feminine.matches(retained))
        assertEquals("इयँम्ऽ", feminine.apply(retained).state.terms.first().surface)
        for (suffix in listOf(dev.panini.core.SupAffix.AU.term("dual"),
            DerivationTerm("sup-su", "सुँ", TermKind.PRATYAYA))) {
            val wrong = original.copy(terms = listOf(original.terms.first(), suffix))
            assertFalse(retention.matches(wrong))
            assertFalse(feminine.matches(wrong))
        }
    }
    @Test fun `idam nominative singular preserves m and separates masculine and feminine substitutions`() {
        for ((linga, expected) in listOf(Linga.PUMS to "अयम्", Linga.STRI to "इयम्")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, linga))
            assertEquals(expected, result.final.surface, result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
            assertTrue(result.applications.any { it.sutra == "7.2.108" })
            assertFalse(result.applications.any { it.sutra == "7.2.102" })
            assertEquals(linga == Linga.STRI, result.applications.any { it.sutra == "7.2.110" })
            assertEquals(linga == Linga.PUMS, result.applications.any { it.sutra == "7.2.111" })
            result.final.requireCompleteItProcessing()
        }
        for (number in listOf("7.2.108", "7.2.110"))
            assertTrue(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras.any { it.sutra == number })
    }
    @Test fun `id prefix substitution preserves the remaining annotated carrier and typed su identity`() {
        val rule = dev.panini.ashtadhyayi.adhyaya7.pada2.IdoAyamSutra
        val original = DerivationState(listOf(
            DerivationTerm("stem", "इदँम्ऽ", TermKind.PRATIPADIKA, upadesha = "इदम्"),
            dev.panini.core.SupAffix.SU.term("renamed")),
            context = DerivationalContext(rupa = Rupa(linga = Linga.PUMS)))
        assertTrue(rule.matches(original))
        val result = rule.apply(original).state
        assertEquals("अयँम्ऽ", result.terms.first().surface)
        assertEquals(0, result.substitutions.single().sourceVarnaIndex)
        assertEquals(original.terms.first().phonologicalText.effectiveVarnas.drop(2).map { it.copy(sourceSpan = null) },
            result.terms.first().phonologicalText.effectiveVarnas.drop(2).map { it.copy(sourceSpan = null) })
        assertEquals(original.terms.last(), result.terms.last())
        assertFalse(rule.matches(result))
        assertFalse(rule.matches(original.copy(terms = listOf(original.terms.first(),
            DerivationTerm("sup-su", "सुँ", TermKind.PRATYAYA)))))
    }
    @Test fun `dvi feminine aa is supplied by tap not by atva`() {
        for (linga in Linga.entries) for (affix in dev.panini.core.SupAffix.entries.filter { it.vacana == Vacana.DVIVACANA }) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("द्वि", affix.vibhakti, affix.vacana, linga))
            val expected = when (affix.vibhakti) {
                Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> if (linga == Linga.PUMS) "द्वौ" else "द्वे"
                Vibhakti.SASTHI, Vibhakti.SAPTAMI -> "द्वयोः"
                else -> "द्वाभ्याम्"
            }
            assertEquals(expected, result.final.surface, "$linga $affix")
            val atva = result.applications.single { it.sutra == "7.2.102" }
            assertEquals(dev.panini.shiksha.Svara.A, atva.after.terms.first().varnas.last())
            assertEquals(2, atva.after.substitutions.last().sourceVarnaIndex)
            if (linga == Linga.STRI) assertTrue(result.final.droppedTerms.any { it.createdBySutra == "4.1.4" })
            result.final.requireCompleteItProcessing()
        }
    }
    @Test fun `idam dual and plural retain independent das ca substitutions`() {
        assertTrue(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras.any { it.sutra == "7.2.109" })
        for ((vacana, expected) in listOf(Vacana.DVIVACANA to "इमौ", Vacana.BAHUVACANA to "इमे")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, vacana, Linga.PUMS))
            assertEquals(expected, result.final.surface)
            val atva = result.applications.single { it.sutra == "7.2.102" }
            assertEquals(dev.panini.shiksha.Vyanjana.DA, atva.after.terms.first().varnas[1])
            val das = result.applications.single { it.sutra == "7.2.109" }
            assertEquals(1, das.after.substitutions.last().sourceVarnaIndex)
            assertTrue(result.applications.indexOf(atva) < result.applications.indexOf(das))
            result.final.requireCompleteItProcessing()
        }
    }
    @Test fun `tyadadi final substitution and base composition have separate exact traces`() {
        for (stem in listOf("यद्", "तद्", "एतद्")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.STRI))
            val atva = result.applications.single { it.sutra == "7.2.102" }
            val base = atva.after.terms.single { it.pendingBaseComposition != null }
            val pending = requireNotNull(base.pendingBaseComposition)
            val substitution = atva.after.substitutions.last()
            assertEquals(base.varnas.lastIndex, substitution.sourceVarnaIndex)
            val composition = result.applications.first { it.sutra == "6.1.97" }
            assertEquals(pending.index, composition.after.substitutions.last().sourceVarnaIndex)
            assertTrue(composition.after.terms.all { it.pendingBaseComposition == null })
            assertTrue(result.applications.indexOf(composition) < result.applications.indexOfFirst { it.sutra == "4.1.4" })
            result.final.requireCompleteItProcessing()
        }
    }
    @Test fun `derived sah retains su lopa before consonants without deleting a native repha`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        val engine = SandhiEngine()
        val right = SandhiEngine.Pada(DerivationTerm("right", "गच्छति", TermKind.PRATIPADIKA))
        val joined = engine.join(engine.padaFromDerivation(result, "left").copy(visargaSource = VisargaSource.ESTABLISHED_RU), right)
        assertEquals("सगच्छति", engine.render(joined).filterNot { it.isWhitespace() })
        assertTrue(joined.applications.any { it.sutra == "6.1.132" })
        val native = engine.join(SandhiEngine.Pada(DerivationTerm("native", "सर्", TermKind.PRATIPADIKA)), right)
        assertFalse(native.applications.any { it.sutra == "6.1.132" })
    }
    @Test fun `all feminine case forms preserve tap formation and case provenance`() {
        val endings = listOf("ा", "े", "ाः", "ाम्", "े", "ाः", "या", "ाभ्याम्", "ाभिः",
            "स्यै", "ाभ्याम्", "ाभ्यः", "स्याः", "ाभ्याम्", "ाभ्यः", "स्याः", "योः", "ासाम्", "स्याम्", "योः", "ासु")
        for ((stem, prefix) in listOf("किम्" to "क", "यद्" to "य", "तद्" to "त", "एतद्" to "एत")) {
            val requests = dev.panini.core.SupAffix.entries
            for ((index, affix) in requests.withIndex()) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana, Linga.STRI))
                val expected = when {
                    stem == "तद्" && index == 0 -> "सा"
                    stem == "एतद्" && index == 0 -> "एषा"
                    else -> prefix + endings[index]
                }
                assertEquals(expected, result.final.surface, "$stem $affix")
                val tap = result.final.droppedTerms.single { it.createdBySutra == "4.1.4" }
                assertEquals("टाप्", tap.upadesha)
                assertEquals(tap.feminineTargetId, tap.mergedIntoTermId)
                assertTrue(tap.itMarkerProvenance.isNotEmpty())
                assertTrue(result.applications.any { it.sutra == "6.1.101" })
                if (affix == dev.panini.core.SupAffix.AM_6) {
                    assertTrue(result.applications.any { it.sutra == "7.1.52" })
                    assertFalse(result.applications.any { it.sutra == "7.1.54" })
                    val sut = result.final.droppedTerms.single { it.createdBySutra == "7.1.52" }
                    assertEquals("सुँट्", sut.upadesha)
                    assertTrue(sut.itMarkerProvenance.isNotEmpty())
                }
                result.final.requireCompleteItProcessing()
            }
        }
    }
    @Test fun `feminine dative composes syat within its own suffix`() {
        val request = SubantaDerivationRequest("किम्", Vibhakti.CHATURTHI, Vacana.EKAVACANA, Linga.STRI)
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(request.initialState())
        assertEquals("कस्यै", result.final.surface, result.final.terms.toString() + result.final.blockedSutras)
    }
    @Test fun `pronoun gender analyses retain their own nominative forms`() {
        for ((stem, expected) in listOf("किम्" to "का", "यद्" to "या", "तद्" to "सा")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.STRI))
            assertEquals(expected, result.final.surface, "$stem: " + result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
        }
    }
    @Test fun `neuter plural num is not retargeted by tyadadi atva`() {
        for ((stem, expected) in listOf("यद्" to "यानि", "तद्" to "तानि", "किम्" to "कानि"))
            for (vibhakti in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
                assertEquals(expected, result.final.surface, "$stem $vibhakti: " + result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
                assertTrue(result.applications.count { it.sutra == "7.2.102" } <= 1)
                result.final.requireCompleteItProcessing()
            }
    }
    @Test fun `frequent corpus pronouns have complete forward nominal proofs`() {
        val entries = listOf("किम्", "यद्", "तद्", "एतद्", "इदम्").map { PratipadikaEntry(it, Linga.entries.toSet()) }
        val index = DerivedVisargaLexicon.build(entries)
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.map { it.request }.toString())
        for (form in listOf("कः", "यः", "सः", "एषः", "इमाः", "एभ्यः", "आभ्यः")) {
            val candidates = index.candidates(form)
            assertTrue(candidates.isNotEmpty(), form)
            assertTrue(candidates.all { it.source == VisargaSource.ESTABLISHED_RU })
            assertTrue(candidates.all { it.pada.sourceDerivation === it.derivation })
        }
    }
}
