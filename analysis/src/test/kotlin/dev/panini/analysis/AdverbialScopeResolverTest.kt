package dev.panini.analysis

import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class AdverbialScopeResolverTest {
    private val id = KriyaId("scope")
    private val words = listOf("न", "बहु", "शनैः", "यदा", "प्रत्येकम्", "अज्ञातम्", "बहु")
        .map { AnalyzedAvyaya(AvyayaPada(it, it)) }
    private val qualifications = AdverbialAnalyzer.analyze(id, words)

    @Test fun `equal words cannot substitute for another written source occurrence`() {
        assertEquals(words[1], words[6])
        assertNotSame(words[1].pada, words[6].pada)
        assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(
            qualifications[1].copy(sourceIndex = 6), words, AdverbialTarget.Event(id)))
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            qualifications.last(), words, AdverbialTarget.Event(id)))
    }

    @Test fun `equivalent analysis wrapper for the same source occurrence remains valid`() {
        val reanalyzed = words.toMutableList().also { it[1] = AnalyzedAvyaya(words[1].pada) }
        assertNotSame(words[1], reanalyzed[1])
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            qualifications[1], reanalyzed, AdverbialTarget.Event(id)))
    }

    @Test fun `negation and degree require explicit licensed attachments`() {
        assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(qualifications[0], words, null))
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            qualifications[0], words, AdverbialTarget.Proposition(id)))
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            qualifications[1], words, AdverbialTarget.Constituent(id, 6)))
        assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(
            qualifications[0], words, AdverbialTarget.Event(id)))
    }

    @Test fun `invalid occurrences and cross clause targets are rejected`() {
        listOf(AdverbialTarget.Constituent(id, 1), AdverbialTarget.Constituent(id, -1),
            AdverbialTarget.Constituent(id, words.size), AdverbialTarget.Event(KriyaId("other")))
            .forEach { assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(qualifications[1], words, it)) }
        assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(
            qualifications[2], words.reversed(), AdverbialTarget.Event(id)))
    }

    @Test fun `relatives distribution and unknown meanings do not become event modifiers`() {
        (3..5).forEach { assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(
            qualifications[it], words, AdverbialTarget.Event(id))) }
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            qualifications[2], words, AdverbialTarget.Event(id)))
    }

    @Test fun `case form alternatives must be excluded before selecting an adverbial reading`() {
        val ambiguous = qualifications[2].copy(competingNominalReading = true, morphology = AdverbialMorphology.CASE_FORM)
        assertIs<AdverbialResolution.Unresolved>(AdverbialScopeResolver.resolve(ambiguous, words, AdverbialTarget.Event(id)))
        assertIs<AdverbialResolution.Resolved>(AdverbialScopeResolver.resolve(
            ambiguous, words, AdverbialTarget.Event(id), nominalReadingExcluded = true))
    }
}
