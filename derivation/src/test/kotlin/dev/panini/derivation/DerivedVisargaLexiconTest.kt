package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import kotlin.test.*

class DerivedVisargaLexiconTest {
    private fun assertParadigm(stem: String, expected: List<String>) {
        for ((index, suffix) in SupAffix.entries.withIndex()) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, suffix.vibhakti, suffix.vacana, Linga.PUMS))
            assertEquals(expected[index], result.final.surface, "$stem $suffix")
            result.final.requireCompleteItProcessing()
        }
    }

    @Test fun `attested masculine ghata paradigm is forward verified`() {
        val expected = listOf("घटः", "घटौ", "घटाः", "घटम्", "घटौ", "घटान्", "घटेन", "घटाभ्याम्", "घटैः",
            "घटाय", "घटाभ्याम्", "घटेभ्यः", "घटात्", "घटाभ्याम्", "घटेभ्यः", "घटस्य", "घटयोः", "घटानाम्", "घटे", "घटयोः", "घटेषु")
        assertParadigm("घट", expected)
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("घट", setOf(Linga.PUMS))))
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        val candidate = assertNotNull(index.candidates("घटः").firstOrNull())
        assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
        assertTrue(candidate.derivation.applications.any { it.sutra == "8.2.66" })
        val right = SandhiEngine.Pada(DerivationTerm("right", "अस्ति", TermKind.PRATIPADIKA))
        assertEquals("घटोऽस्ति", SandhiEngine().render(SandhiEngine().join(candidate.pada, right)))
    }

    @Test fun `amarakosha masculine a and u stems are forward verified`() {
        val paradigms = mapOf(
            "पुत्र" to listOf("पुत्रः", "पुत्रौ", "पुत्राः", "पुत्रम्", "पुत्रौ", "पुत्रान्", "पुत्रेण", "पुत्राभ्याम्", "पुत्रैः",
                "पुत्राय", "पुत्राभ्याम्", "पुत्रेभ्यः", "पुत्रात्", "पुत्राभ्याम्", "पुत्रेभ्यः", "पुत्रस्य", "पुत्रयोः", "पुत्राणाम्", "पुत्रे", "पुत्रयोः", "पुत्रेषु"),
            "दोष" to listOf("दोषः", "दोषौ", "दोषाः", "दोषम्", "दोषौ", "दोषान्", "दोषेण", "दोषाभ्याम्", "दोषैः",
                "दोषाय", "दोषाभ्याम्", "दोषेभ्यः", "दोषात्", "दोषाभ्याम्", "दोषेभ्यः", "दोषस्य", "दोषयोः", "दोषाणाम्", "दोषे", "दोषयोः", "दोषेषु"),
            "गुरु" to listOf("गुरुः", "गुरू", "गुरवः", "गुरुम्", "गुरू", "गुरून्", "गुरुणा", "गुरुभ्याम्", "गुरुभिः",
                "गुरवे", "गुरुभ्याम्", "गुरुभ्यः", "गुरोः", "गुरुभ्याम्", "गुरुभ्यः", "गुरोः", "गुर्वोः", "गुरूणाम्", "गुरौ", "गुर्वोः", "गुरुषु"),
            "हेतु" to listOf("हेतुः", "हेतू", "हेतवः", "हेतुम्", "हेतू", "हेतून्", "हेतुना", "हेतुभ्याम्", "हेतुभिः",
                "हेतवे", "हेतुभ्याम्", "हेतुभ्यः", "हेतोः", "हेतुभ्याम्", "हेतुभ्यः", "हेतोः", "हेत्वोः", "हेतूनाम्", "हेतौ", "हेत्वोः", "हेतुषु"),
        )
        paradigms.forEach(::assertParadigm)
        val index = DerivedVisargaLexicon.build(paradigms.keys.map { PratipadikaEntry(it, setOf(Linga.PUMS)) })
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        for ((word, expected) in listOf("पुत्रः" to "पुत्रोऽस्ति", "दोषः" to "दोषोऽस्ति", "गुरुः" to "गुरुरस्ति", "हेतुः" to "हेतुरस्ति")) {
            val candidate = assertNotNull(index.candidates(word).firstOrNull(), word)
            assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
            assertTrue(candidate.derivation.applications.any { it.sutra == "8.2.66" })
            val right = SandhiEngine.Pada(DerivationTerm("right", "अस्ति", TermKind.PRATIPADIKA))
            assertEquals(expected, SandhiEngine().render(SandhiEngine().join(candidate.pada, right)))
        }
    }

    @Test fun `indra and shakra paradigms have forward derived visarga proof`() {
        val forms = mapOf(
            "इन्द्र" to listOf("इन्द्रः", "इन्द्रौ", "इन्द्राः", "इन्द्रम्", "इन्द्रौ", "इन्द्रान्", "इन्द्रेण", "इन्द्राभ्याम्", "इन्द्रैः",
                "इन्द्राय", "इन्द्राभ्याम्", "इन्द्रेभ्यः", "इन्द्रात्", "इन्द्राभ्याम्", "इन्द्रेभ्यः", "इन्द्रस्य", "इन्द्रयोः", "इन्द्राणाम्", "इन्द्रे", "इन्द्रयोः", "इन्द्रेषु"),
            "शक्र" to listOf("शक्रः", "शक्रौ", "शक्राः", "शक्रम्", "शक्रौ", "शक्रान्", "शक्रेण", "शक्राभ्याम्", "शक्रैः",
                "शक्राय", "शक्राभ्याम्", "शक्रेभ्यः", "शक्रात्", "शक्राभ्याम्", "शक्रेभ्यः", "शक्रस्य", "शक्रयोः", "शक्राणाम्", "शक्रे", "शक्रयोः", "शक्रेषु"))
        for ((stem, expected) in forms) for ((index, suffix) in SupAffix.entries.withIndex()) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, suffix.vibhakti, suffix.vacana, Linga.PUMS))
            assertEquals(expected[index], result.final.surface, "$stem $suffix")
            result.final.requireCompleteItProcessing()
        }
        val index = DerivedVisargaLexicon.build(forms.keys.map { PratipadikaEntry(it, setOf(Linga.PUMS)) })
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        for ((word, expected) in listOf("इन्द्रः" to "इन्द्रोवा", "शक्रः" to "शक्रोवा")) {
            val candidates = index.candidates(word)
            assertTrue(candidates.isNotEmpty())
            for (candidate in candidates) {
                assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
                assertSame(candidate.derivation, candidate.pada.sourceDerivation)
                val engine = SandhiEngine()
                assertEquals(expected, engine.render(engine.join(candidate.pada,
                    SandhiEngine.Pada(DerivationTerm("right", "वा", TermKind.PRATIPADIKA)))))
            }
        }
    }
    @Test fun `independently declared kapi and udadhi i stem paradigms are forward verified`() {
        val endings = listOf("िः", "ी", "यः", "िम्", "ी", "ीन्", "िना", "िभ्याम्", "िभिः",
            "ये", "िभ्याम्", "िभ्यः", "ेः", "िभ्याम्", "िभ्यः", "ेः", "्योः", "ीनाम्", "ौ", "्योः", "िषु")
        for ((stem, prefix) in listOf("कपि" to "कप", "उदधि" to "उदध")) {
            for ((index, suffix) in SupAffix.entries.withIndex()) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, suffix.vibhakti, suffix.vacana, Linga.PUMS))
                assertEquals(prefix + endings[index], result.final.surface, "$stem $suffix")
                result.final.requireCompleteItProcessing()
            }
        }
        val index = DerivedVisargaLexicon.build(listOf("कपि", "उदधि").map { PratipadikaEntry(it, setOf(Linga.PUMS)) })
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        for ((word, expected) in listOf("कपिः" to "कपिरपि", "उदधिः" to "उदधिरपि")) {
            val candidates = index.candidates(word)
            assertTrue(candidates.isNotEmpty())
            for (candidate in candidates) {
                assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
                assertSame(candidate.derivation, candidate.pada.sourceDerivation)
                val engine = SandhiEngine()
                assertEquals(expected, engine.render(engine.join(candidate.pada,
                    SandhiEngine.Pada(DerivationTerm("right", "अपि", TermKind.PRATIPADIKA)))))
            }
        }
    }
    @Test fun `rama and deva paradigms are independently forward verified`() {
        val forms = mapOf(
            "राम" to listOf("रामः", "रामौ", "रामाः", "रामम्", "रामौ", "रामान्", "रामेण", "रामाभ्याम्", "रामैः",
                "रामाय", "रामाभ्याम्", "रामेभ्यः", "रामात्", "रामाभ्याम्", "रामेभ्यः", "रामस्य", "रामयोः", "रामाणाम्", "रामे", "रामयोः", "रामेषु"),
            "देव" to listOf("देवः", "देवौ", "देवाः", "देवम्", "देवौ", "देवान्", "देवेन", "देवाभ्याम्", "देवैः",
                "देवाय", "देवाभ्याम्", "देवेभ्यः", "देवात्", "देवाभ्याम्", "देवेभ्यः", "देवस्य", "देवयोः", "देवानाम्", "देवे", "देवयोः", "देवेषु"))
        for ((stem, expected) in forms) for ((index, suffix) in SupAffix.entries.withIndex()) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, suffix.vibhakti, suffix.vacana, Linga.PUMS))
            assertEquals(expected[index], result.final.surface, "$stem $suffix")
            result.final.requireCompleteItProcessing()
        }
        val index = DerivedVisargaLexicon.build(forms.keys.map { PratipadikaEntry(it, setOf(Linga.PUMS)) })
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        for ((word, expected) in listOf("रामः" to "रामोऽत्र", "देवः" to "देवोऽत्र")) {
            val candidates = index.candidates(word)
            assertTrue(candidates.isNotEmpty())
            for (candidate in candidates) {
                assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
                assertSame(candidate.derivation, candidate.pada.sourceDerivation)
                val engine = SandhiEngine()
                assertEquals(expected, engine.render(engine.join(candidate.pada,
                    SandhiEngine.Pada(DerivationTerm("right", "अत्र", TermKind.PRATIPADIKA)))))
            }
        }
    }
    companion object {
        private val standard by lazy { DerivedVisargaLexicon.build(PaninianPratipadikaLexicon.declaredPratipadikas()) }
    }

    @Test fun `declared lexical forms reproduce inputs and retain full derivation evidence`() {
        for ((surface, expected) in listOf("प्रथमः" to "प्रथमोऽस्ति", "शब्दः" to "शब्दोऽस्ति")) {
            val candidates = standard.candidates(surface)
            assertTrue(candidates.isNotEmpty(), surface)
            for (candidate in candidates) {
                assertEquals(surface, candidate.pada.term.surface)
                assertSame(candidate.derivation, candidate.pada.sourceDerivation)
                assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
                val engine = SandhiEngine()
                val right = SandhiEngine.Pada(DerivationTerm("next", "अस्ति", TermKind.PRATIPADIKA))
                assertEquals(expected, engine.render(engine.join(candidate.pada, right)))
            }
        }
        assertTrue(standard.failures.isEmpty(), standard.failures.joinToString())
        assertTrue(standard.unresolvedSources.isEmpty(), standard.unresolvedSources.joinToString { unresolved ->
            "${unresolved.request}: ${unresolved.derivation.final.surface}; " +
                unresolved.derivation.final.substitutions.takeLast(3).joinToString()
        })
    }

    @Test fun `independently attested nominal examples supply forward visarga provenance`() {
        for ((word, right, expected) in listOf(
            Triple("वृक्षः", "अत्र", "वृक्षोऽत्र"),
            Triple("प्लक्षः", "अत्र", "प्लक्षोऽत्र"),
            Triple("पुरुषः", "याति", "पुरुषोयाति"),
            Triple("शिवः", "वन्द्यः", "शिवोवन्द्यः"))) {
            val candidates = standard.candidates(word)
            assertTrue(candidates.isNotEmpty(), word)
            for (candidate in candidates) {
                assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
                assertSame(candidate.derivation, candidate.pada.sourceDerivation)
                val engine = SandhiEngine()
                val result = engine.join(candidate.pada, SandhiEngine.Pada(DerivationTerm("next", right, TermKind.PRATIPADIKA)))
                assertEquals(expected, engine.render(result).filterNot { it.isWhitespace() })
            }
        }
        assertTrue(standard.failures.isEmpty(), standard.failures.toString())
        assertTrue(standard.unresolvedSources.isEmpty(), standard.unresolvedSources.toString())
    }

    @Test fun `homographic nominal cases retain separate candidate proofs and native repha source`() {
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("पितृ", setOf(Linga.PUMS))))
        val candidates = index.candidates("पितुः")
        assertTrue(candidates.size >= 2)
        assertEquals(setOf(VisargaSource.R), candidates.map { it.source }.toSet())
        assertTrue(candidates.map { it.request.vibhakti }.toSet().size >= 2)
        for (candidate in candidates) {
            val engine = SandhiEngine()
            val result = engine.join(candidate.pada,
                SandhiEngine.Pada(DerivationTerm("next", "अस्ति", TermKind.PRATIPADIKA)))
            assertEquals("पितुरस्ति", engine.render(result))
            assertTrue(result.applications.none { it.sutra == "8.2.66" || it.sutra == "6.1.113" })
        }
    }

    @Test fun `ap dual visarga follows authentic vowel and rutva rules`() {
        val candidates = standard.candidates("प्रथमयोः").filter { it.request.linga == Linga.STRI }
        assertTrue(candidates.isNotEmpty())
        for (candidate in candidates) {
            val rules = candidate.derivation.final.substitutions.map { it.sutra }
            for (number in listOf("7.3.105", "6.1.78", "8.2.66", "8.3.15")) {
                assertTrue(number in rules, "$number: $rules")
            }
            assertFalse("7.3.114" in rules)
            assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
        }
    }

    @Test fun `undeclared words and unknown lexical gender are not guessed`() {
        assertTrue(standard.candidates("अघोषितः").isEmpty())
        val entry = PratipadikaEntry("अज्ञात", emptySet())
        val index = DerivedVisargaLexicon.build(listOf(entry))
        assertEquals(listOf(entry), index.entriesWithoutGender)
        assertTrue(index.surfaces.isEmpty())
        assertTrue(index.failures.isEmpty())
    }

    @Test fun `agnir atra retains independently derived i stem rutva provenance`() {
        // Kāśikā on 6.1.113: अग्निरत्र is the counterexample to preceding अ.
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("अग्नि", setOf(Linga.PUMS))))
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        val candidates = index.candidates("अग्निः")
        assertTrue(candidates.isNotEmpty())
        for (candidate in candidates) {
            assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)
            assertSame(candidate.derivation, candidate.pada.sourceDerivation)
            val engine = SandhiEngine()
            val result = engine.join(candidate.pada,
                SandhiEngine.Pada(DerivationTerm("next", "अत्र", TermKind.PRATIPADIKA)))
            assertEquals("अग्निरत्र", engine.render(result))
            assertFalse(result.applications.any { it.sutra == "6.1.113" })
        }
    }

    @Test fun `declared ap provenance prevents false genitive homographs`() {
        val candidates = standard.candidates("प्रथमायाः")
        assertEquals(setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI), candidates.map { it.request.vibhakti }.toSet())
        assertTrue(candidates.all { it.request.stemFormation == NominalStemFormation.AP })
        assertTrue(candidates.all { it.derivation.applications.any { change -> change.sutra == "7.3.113" } })
        assertTrue(standard.candidates("प्रथमाः").none {
            it.request.vacana == Vacana.EKAVACANA && it.request.vibhakti in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)
        })
        assertTrue(standard.candidates("प्रथमाः").any { it.request.vacana == Vacana.BAHUVACANA })
    }

    @Test fun `optional ordinal sources retain both independently derived forms`() {
        for ((surface, producer) in listOf("द्वितीयस्याः" to "7.3.115", "द्वितीयायाः" to "7.3.113")) {
            val candidates = standard.candidates(surface)
            assertEquals(setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI), candidates.map { it.request.vibhakti }.toSet(), surface)
            assertTrue(candidates.all { it.request.pratipadikaUpadesha == "द्वितीय" })
            assertTrue(candidates.all { it.derivation.applications.any { change -> change.sutra == producer } })
            assertTrue(candidates.all { it.source == VisargaSource.ESTABLISHED_RU })
        }
        assertEquals(NominalStemFormation.UNSPECIFIED, PratipadikaEntry("अज्ञाता", setOf(Linga.STRI)).stemFormation)
    }

    @Test fun `failed candidate derivations are exposed rather than replaced by outputs`() {
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("@", setOf(Linga.PUMS))))
        assertEquals(SupAffix.entries.size, index.failures.size)
        assertTrue(index.surfaces.isEmpty())
        assertTrue(index.failures.all { it.errorType.isNotBlank() })
    }
}
