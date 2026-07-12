package dev.sanskrit.derivation

import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class DerivationEngineTest {
    @Test
    fun `nominal request creates a structured prātipadika derivation state`() {
        val state = SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.EKAVACANA).initialState()

        assertEquals(TermKind.PRATIPADIKA, state.terms.single().kind)
        assertTrue(HasSemanticFeature(SemanticFeature.PRATHAMA).matches(state))
        assertTrue(HasSemanticFeature(SemanticFeature.EKAVACANA).matches(state))
    }

    @Test
    fun `sup inventory has one affix for every case and number slot`() {
        assertEquals(21, SupAffix.entries.size)
        assertEquals(SupAffix.SU, SupAffix.select(Vibhakti.PRATHAMA, Vacana.EKAVACANA))
        assertEquals(SupAffix.SUP, SupAffix.select(Vibhakti.SAPTAMI, Vacana.BAHUVACANA))
        assertEquals(SupAffix.SUP, SupAffix.fromFeatures(setOf(SemanticFeature.SAPTAMI, SemanticFeature.BAHUVACANA)))
        assertEquals(null, SupAffix.fromFeatures(setOf(SemanticFeature.SAPTAMI)))
        assertEquals(null, SupAffix.fromFeatures(setOf(SemanticFeature.SAPTAMI, SemanticFeature.EKAVACANA, SemanticFeature.BAHUVACANA)))
        assertEquals(SupAffix.AUT, SubantaFormPlans.find(Vibhakti.DVITIYA, Vacana.DVIVACANA)?.affix)
        assertEquals(SupAffix.SAS, SubantaFormPlans.find(Vibhakti.DVITIYA, Vacana.BAHUVACANA)?.affix)
        assertEquals(DerivationStage.FINAL, SubantaFormPlans.find(Vibhakti.PRATHAMA, Vacana.EKAVACANA)?.finalStage)
    }

    @Test
    fun `selects su for first singular nominal derivation`() {
        val result = DerivationEngine().derive(
            SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.EKAVACANA).initialState(),
        )

        assertTrue(result.applications.any { it.sutra == "4.1.2" })
        assertTrue(result.applications.any { it.sutra == "1.3.9" })
        assertEquals(SutraAction.PRATYAYA_SELECTION, result.applications.single { it.sutra == "4.1.2" }.action)
        assertEquals(
            "4.1.2 selects सुँ for the requested case and number.",
            result.applications.single { it.sutra == "4.1.2" }.explanation,
        )
        assertEquals("sup-su", result.applications.single { it.sutra == "4.1.2" }.delta.addedTerms.single().id)
        assertEquals(
            DerivationStage.PRATYAYA_SELECTED,
            result.applications.single { it.sutra == "4.1.2" }.delta.stageAfter,
        )
        assertEquals("सुँ", result.final.terms.single { it.id == "sup-su" }.upadesha)
        assertTrue(result.final.terms.single { it.id == "sup-su" }.itMarkers.isEmpty())
        assertEquals("रामः", result.final.surface)
        assertEquals("1.1.1", result.applications.first().sutra)
    }

    @Test
    fun `nominal engine exposes the complete first singular result`() {
        val result = SubantaEngine().derive(
            SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.EKAVACANA),
        )

        assertEquals("रामः", result.final.surface)
    }

    @Test
    fun `nominal engine derives first dual ramaau`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.DVIVACANA))

        assertEquals("रामौ", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.1.88" })
        assertEquals(1, result.applications.count { it.sutra == "1.1.1" })
    }

    @Test
    fun `nominal engine derives first plural ramaah`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.BAHUVACANA))

        assertEquals("रामाः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "1.3.9" })
        assertTrue(result.applications.any { it.sutra == "6.1.102" })
        assertTrue(result.applications.any { it.sutra == "8.2.66" })
    }

    @Test
    fun `nominal engine derives accusative singular ramam`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.DVITIYA, Vacana.EKAVACANA))

        assertEquals("रामम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.1.107" })
    }

    @Test
    fun `nominal engine derives accusative dual ramau through it designation and lopa`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.DVITIYA, Vacana.DVIVACANA))

        assertEquals("रामौ", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "1.3.3" })
        assertTrue(result.applications.any { it.sutra == "1.3.9" })
        assertTrue(result.applications.any { it.sutra == "6.1.88" })
    }

    @Test
    fun `nominal engine derives the implemented portion of an a-stem paradigm`() {
        val paradigm = SubantaEngine().deriveSupportedParadigm("राम")

        assertEquals(
            mapOf(
                SupAffix.SU to "रामः",
                SupAffix.AU to "रामौ",
                SupAffix.JAS to "रामाः",
                SupAffix.AM to "रामम्",
                SupAffix.AUT to "रामौ",
                SupAffix.SAS to "रामान्",
                SupAffix.TA to "रामेण",
                SupAffix.BHYAM_3 to "रामाभ्याम्",
                SupAffix.BHYAM_4 to "रामाभ्याम्",
                SupAffix.BHYAM_5 to "रामाभ्याम्",
                SupAffix.BHIS to "रामैः",
                SupAffix.NGE to "रामाय",
                SupAffix.BHYAS_4 to "रामेभ्यः",
                SupAffix.BHYAS_5 to "रामेभ्यः",
                SupAffix.NGASI to "रामात्",
                SupAffix.NGAS to "रामस्य",
                SupAffix.OS_6 to "रामयोः",
                SupAffix.AM_6 to "रामाणाम्",
                SupAffix.OS_7 to "रामयोः",
                SupAffix.NGI to "रामे",
                SupAffix.SUP to "रामेषु",
            ),
            paradigm.surfaces,
        )
        assertEquals(21, paradigm.forms.size)
    }

    @Test
    fun `nominal engine derives locative plural ramesu`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.SAPTAMI, Vacana.BAHUVACANA))

        assertEquals("रामेषु", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.3.59" })
    }

    @Test
    fun `nominal engine rejects a stem outside the implemented a-stem profile`() {
        assertFailsWith<IllegalArgumentException> {
            SubantaEngine().derive(
                SubantaDerivationRequest("हरि", Vibhakti.PRATHAMA, Vacana.EKAVACANA),
            )
        }
    }

    @Test
    fun `typed conditions inspect grammatical state and operations introduce an affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("bhu", "भू", TermKind.DHATU)),
            semanticFeatures = setOf(SemanticFeature.KARTARI),
        )
        val condition = HasTermKind(TermKind.DHATU)
        val operation = IntroduceTerm(
            term = DerivationTerm("sap", "शप्", TermKind.PRATYAYA, setOf(ItMarker.SIT)),
            explanation = "Introduced a demonstration pratyaya.",
        )

        assertTrue(condition.matches(state))
        assertTrue(HasSemanticFeature(SemanticFeature.KARTARI).matches(state))

        val result = operation.apply(state)

        assertEquals(DerivationStage.PRATYAYA_SELECTED, result.state.stage)
        assertEquals(TermKind.PRATYAYA, result.state.terms.single { it.id == "sap" }.kind)
        assertTrue(HasItMarker(ItMarker.SIT).matches(result.state))
    }

    @Test
    fun `conditions compose into a precise grammatical environment`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("bhu", "भू", TermKind.DHATU)),
            semanticFeatures = setOf(SemanticFeature.KARTARI),
        )
        val condition = AllOf(
            HasTermKind(TermKind.DHATU),
            HasSemanticFeature(SemanticFeature.KARTARI),
            Not(HasItMarker(ItMarker.KIT)),
        )

        assertTrue(condition.matches(state))
        assertTrue(AnyOf(HasTermKind(TermKind.PRATYAYA), HasTermKind(TermKind.DHATU)).matches(state))
    }

    @Test
    fun `operations compose into an ordered derivation step`() {
        val state = DerivationState(listOf(DerivationTerm("bhu", "भू", TermKind.DHATU)))
        val operation = SequenceOf(
            IntroduceTerm(DerivationTerm("suffix", "ति", TermKind.PRATYAYA), explanation = "Added tiṅ."),
            AdvanceDerivationStage(DerivationStage.IT_PROCESSED, "Processed it markers."),
        )

        val result = operation.apply(state)

        assertEquals(DerivationStage.IT_PROCESSED, result.state.stage)
        assertTrue(result.state.terms.any { it.id == "suffix" })
    }

    @Test
    fun `adhikara operations establish reusable derivation scope`() {
        val state = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))

        val result = ActivateAdhikara("3.1.68", "Opened kartari scope.").apply(state)

        assertTrue(HasActiveAdhikara("3.1.68").matches(result.state))
    }

    @Test
    fun `anuvrtti operations carry inherited conditions forward`() {
        val state = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))

        val result = CarryAnuvrtti("कर्तरि", "Carried कर्तरि forward.").apply(state)

        assertTrue(HasAnuvrtti("कर्तरि").matches(result.state))
    }

    @Test
    fun `optional operations retain both permitted derivation outcomes`() {
        val state = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))
        val optional = OptionalOperation(
            AdvanceDerivationStage(DerivationStage.FINAL, "Applied optional operation."),
            "Skipped optional operation.",
        )

        val outcomes = optional.applyAll(state)

        assertEquals(DerivationStage.FINAL, outcomes[0].state.stage)
        assertEquals(DerivationStage.INITIAL, outcomes[1].state.stage)
    }

    @Test
    fun `deriveAll follows both optional branches`() {
        val optionalSutra = object : Sutra<DerivationState, DerivationChange>(
            number = "9.1.1", text = "test", hindiExplanation = "test", type = SutraType.VIBHASHA,
            chapter = 9, pada = 1, optional = true, kramaValue = 910001,
            role = dev.sanskrit.sutra.SutraRole.Vibhasha, action = SutraAction.VIKALPA, scope = SutraScope.DERIVATION,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.stage == DerivationStage.INITIAL

            override fun apply(context: DerivationState): DerivationChange =
                AdvanceDerivationStage(DerivationStage.FINAL, "Applied optional change.").apply(context)

            override fun applyAll(state: DerivationState): List<DerivationChange> =
                OptionalOperation(
                    AdvanceDerivationStage(DerivationStage.FINAL, "Applied optional change."),
                    "Skipped optional change.",
                ).applyAll(state)
        }
        val initial = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))

        val results = DerivationEngine(listOf(optionalSutra)).deriveAll(initial)

        assertEquals(setOf(DerivationStage.INITIAL, DerivationStage.FINAL), results.map { it.final.stage }.toSet())
        assertTrue(results.all { it.events.any { event -> event is DerivationEvent.BranchCreated } })
        assertEquals(setOf(0, 1), results.map { it.applications.size }.toSet())
    }

    @Test
    fun `engine rejects a selected sutra that performs no grammatical operation`() {
        val noOpSutra = object : Sutra<DerivationState, DerivationChange>(
            number = "9.1.2", text = "test", hindiExplanation = "test", type = SutraType.NITYA,
            chapter = 9, pada = 1, optional = false, kramaValue = 910002,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.stage == DerivationStage.INITIAL
            override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "No operation.")
        }

        assertFailsWith<IllegalArgumentException> {
            DerivationEngine(listOf(noOpSutra)).derive(DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA))))
        }
    }

    @Test
    fun `engine detects a cycle before reaching the step limit`() {
        val toFinal = object : Sutra<DerivationState, DerivationChange>(
            number = "9.1.3", text = "test", hindiExplanation = "test", type = SutraType.NITYA,
            chapter = 9, pada = 1, optional = false, kramaValue = 910003,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.stage == DerivationStage.INITIAL
            override fun apply(context: DerivationState): DerivationChange = AdvanceDerivationStage(DerivationStage.FINAL, "Advance.").apply(context)
        }
        val toInitial = object : Sutra<DerivationState, DerivationChange>(
            number = "9.1.4", text = "test", hindiExplanation = "test", type = SutraType.NITYA,
            chapter = 9, pada = 1, optional = false, kramaValue = 910004,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.stage == DerivationStage.FINAL
            override fun apply(context: DerivationState): DerivationChange = AdvanceDerivationStage(DerivationStage.INITIAL, "Rewind.").apply(context)
        }

        assertFailsWith<IllegalArgumentException> {
            DerivationEngine(listOf(toFinal, toInitial)).derive(DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA))))
        }
    }

    @Test
    fun `nishedha operations block a sutra for the current derivation`() {
        val state = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))

        val result = BlockSutra("6.1.87", "1.1.4", "Blocked guṇa in this environment.").apply(state)

        assertEquals("1.1.4", result.state.blockedSutras["6.1.87"])
    }

    @Test
    fun `engine traces a dynamically blocked matching rule`() {
        val initial = DerivationState(
            terms = listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)),
            blockedSutras = mapOf("1.1.1" to "1.1.4"),
        )

        val result = DerivationEngine().derive(initial)

        assertTrue(
            DerivationEvent.RuleBlocked("1.1.1", "1.1.4") in result.events,
        )
    }

    @Test
    fun `samjna sutra changes derivation state rather than surface text`() {
        val initial = DerivationState(
            terms = listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)),
        )

        val result = DerivationEngine().derive(initial)

        assertEquals("राम", result.final.surface)
        assertTrue(SamjnaAssignment("stem", Samjna.VRDDHI) in result.final.samjnas)
        assertEquals(listOf("1.1.1"), result.applications.map { it.sutra })
        assertEquals("1.1.1", result.events.filterIsInstance<DerivationEvent.RuleConsidered>().single().sutra)
        assertEquals("1.1.1", result.events.filterIsInstance<DerivationEvent.RuleApplied>().single().sutra)
        assertEquals(1, (result.events.last() as DerivationEvent.Completed).applicationCount)
        assertEquals(1, dev.sanskrit.ashtadhyayi.adhyaya1.pada1.VrddhirAdaicSutra.applyAll(initial).size)
    }

    @Test
    fun `declarative guna definition records a samjna`() {
        val initial = DerivationState(
            terms = listOf(DerivationTerm("stem", "देव", TermKind.PRATIPADIKA)),
        )

        val result = DerivationEngine().derive(initial)

        assertTrue(SamjnaAssignment("stem", Samjna.GUNA) in result.final.samjnas)
        assertEquals(listOf("1.1.2"), result.applications.map { it.sutra })
    }
}
