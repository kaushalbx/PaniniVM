package dev.sanskrit.derivation

import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import dev.sanskrit.ashtadhyayi.adhyaya7.pada2.ArdhadhatukasyedValadehSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada2.VartamaneLatSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.StriyamAdhikaraSutra
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.shiksha.Linga

class DerivationEngineTest {
    @Test
    fun `lakāra inventory retains every upadeśa`() {
        assertEquals(
            setOf("लट्", "लिट्", "लुट्", "लृट्", "लेट्", "लोट्", "लङ्", "लिङ्", "लुङ्", "लृङ्"),
            Lakara.entries.mapTo(mutableSetOf()) { it.upadesha },
        )
    }

    @Test
    fun `kāla inventory contains present past and future`() {
        assertEquals(setOf(Kala.VARTAMANA, Kala.BHUTA, Kala.BHAVISYAT), Kala.entries.toSet())
    }

    @Test
    fun `dhatu factory carries dhatupatha iṭ status into derivation`() {
        val dhatu = requireNotNull(DhatuPatha.find("01.0001"))
        val term = DerivationTerm.fromDhatu(dhatu)
        assertEquals("भू", term.surface)
        assertEquals(ItStatus.SET, term.itStatus)
    }

    @Test
    fun `seṭ root receives iṭ before consonant-initial ārddhadhātuka affix`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, itStatus = ItStatus.SET),
                DerivationTerm("suffix", "त", TermKind.PRATYAYA),
            ),
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
        assertTrue(ArdhadhatukasyedValadehSutra.matches(state))
        assertEquals("इट्", ArdhadhatukasyedValadehSutra.apply(state).state.terms.single { it.id == "it-agama" }.upadesha)
    }

    @Test
    fun `aniṭ root does not receive iṭ under 7 2 35`() {
        val state = DerivationState(
            listOf(DerivationTerm("root", "भू", TermKind.DHATU, itStatus = ItStatus.ANIT), DerivationTerm("suffix", "त", TermKind.PRATYAYA)),
            semanticFeatures = setOf(SemanticFeature.ARDHADHATUKA),
        )
        assertTrue(!ArdhadhatukasyedValadehSutra.matches(state))
    }

    @Test
    fun `derivation engine applies 7 2 35 for an eligible seṭ root`() {
        val state = DerivationState(
            listOf(DerivationTerm("root", "भू", TermKind.DHATU, itStatus = ItStatus.SET), DerivationTerm("suffix", "त", TermKind.PRATYAYA)),
            semanticFeatures = setOf(SemanticFeature.ARDHADHATUKA),
        )
        val result = DerivationEngine(listOf(ArdhadhatukasyedValadehSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "7.2.35" })
        assertEquals("इ", result.final.terms.single { it.id == "it-agama" }.surface)
    }
    @Test
    fun `nominal request creates a structured prātipadika derivation state`() {
        val state = SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.EKAVACANA).initialState()

        assertEquals(TermKind.PRATIPADIKA, state.terms.single().kind)
        assertTrue(HasSemanticFeature(SemanticFeature.PRATHAMA).matches(state))
        assertTrue(HasSemanticFeature(SemanticFeature.EKAVACANA).matches(state))
        assertEquals(Vibhakti.PRATHAMA, state.context.rupa.vibhakti)
        assertEquals(Vacana.EKAVACANA, state.context.rupa.vacana)
        assertEquals(Linga.PUMS, state.context.rupa.linga)
    }

    @Test
    fun `taddhita request keeps requested meaning separate from grammatical facts`() {
        val state = TaddhitaDerivationRequest("पाश", DerivationalMeaning.SAMUHA).initialState()
        assertEquals(DerivationalMeaning.SAMUHA, state.context.requestedMeaning)
        assertTrue(state.semanticFeatures.isEmpty())
    }

    @Test
    fun `legacy features project into typed effective context during migration`() {
        val state = DerivationState(
            listOf(DerivationTerm("stem", "पाश", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.SAMUHA, SemanticFeature.STRI, SemanticFeature.SAPTAMI),
        )
        assertEquals(DerivationalMeaning.SAMUHA, state.effectiveContext.requestedMeaning)
        assertEquals(Linga.STRI, state.effectiveContext.rupa.linga)
        assertEquals(Vibhakti.SAPTAMI, state.effectiveContext.rupa.vibhakti)
    }

    @Test
    fun `legacy bhāve and vartamāna features project to their distinct typed axes`() {
        val state = DerivationState(
            listOf(DerivationTerm("stem", "लोहित", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.BHAVE, SemanticFeature.VARTAMANA),
        )

        assertEquals(DerivationalMeaning.BHAVA, state.effectiveContext.requestedMeaning)
        assertEquals(Kala.VARTAMANA, state.effectiveContext.kala)
        assertEquals(Prayoga.BHAVE, state.effectiveContext.rupa.prayoga)
    }

    @Test
    fun `legacy guṇa and vṛddhi requests project to a typed phonological request`() {
        val guna = DerivationState(
            listOf(DerivationTerm("stem", "इ", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.GUNA_REQUEST),
        )
        val vrddhi = guna.copy(semanticFeatures = setOf(SemanticFeature.VRDDHI_REQUEST))

        assertEquals(PhonologicalRequest.GUNA, guna.effectiveContext.phonologicalRequest)
        assertEquals(PhonologicalRequest.VRDDHI, vrddhi.effectiveContext.phonologicalRequest)
        assertTrue(HasPhonologicalRequest(PhonologicalRequest.VRDDHI).matches(vrddhi))
    }

    @Test
    fun `typed declarative conditions query derivational context`() {
        val state = TaddhitaDerivationRequest("पाश", DerivationalMeaning.SAMUHA).initialState()
        assertTrue(HasRequestedMeaning(DerivationalMeaning.SAMUHA).matches(state))
        assertTrue(!HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(state))
    }

    @Test
    fun `typed tense and gender contexts drive executable sutras`() {
        assertTrue(VartamaneLatSutra.matches(TingantaDerivationRequest("भू").initialState()))

        val feminine = DerivationState(
            terms = listOf(DerivationTerm("stem", "रमा", TermKind.PRATIPADIKA)),
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)),
        )
        assertTrue(StriyamAdhikaraSutra.matches(feminine))
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
        assertTrue(result.applications.any { it.sutra == "1.1.1" })
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
    fun `a-stem paradigm surfaces are executable derivation surfaces`() {
        val paradigm = SubantaEngine().deriveSupportedParadigm("राम")

        assertEquals(paradigm.derivationSurfaces, paradigm.surfaces)
        assertEquals("रामैः", paradigm.surfaces[SupAffix.BHIS])
    }

    @Test
    fun `a-stem coverage report exposes actual derivation traces`() {
        val report = SubantaEngine().deriveSupportedParadigm("राम").coverage

        assertEquals(21, report.size)
        val bhis = report.single { it.affix == SupAffix.BHIS }
        assertEquals("रामैः", bhis.actualSurface)
        assertTrue("4.1.2" in bhis.appliedSutras)
        assertTrue("7.1.9" in bhis.appliedSutras)
        assertTrue("8.3.15" in bhis.appliedSutras)

        val bhyam = report.single { it.affix == SupAffix.BHYAM_3 }
        assertEquals("रामाभ्याम्", bhyam.actualSurface)
        assertTrue("7.3.102" in bhyam.appliedSutras)
    }

    @Test
    fun `bhyam forms are actually lengthened by the derivation engine`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("राम", Vibhakti.TRTIYA, Vacana.DVIVACANA))

        assertEquals("रामाभ्याम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "7.3.102" })
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

        val result = DerivationEngine(listOf(dev.sanskrit.ashtadhyayi.adhyaya1.pada1.VrddhirAdaicSutra)).derive(initial)

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

        val result = DerivationEngine(listOf(dev.sanskrit.ashtadhyayi.adhyaya1.pada1.AdengGunaSutra)).derive(initial)

        assertTrue(SamjnaAssignment("stem", Samjna.GUNA) in result.final.samjnas)
        assertEquals(listOf("1.1.2"), result.applications.map { it.sutra })
    }
}
