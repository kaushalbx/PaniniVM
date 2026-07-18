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
import dev.sanskrit.shiksha.Samjna

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
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
        assertTrue(!ArdhadhatukasyedValadehSutra.matches(state))
    }

    @Test
    fun `derivation engine applies 7 2 35 for an eligible seṭ root`() {
        val state = DerivationState(
            listOf(DerivationTerm("root", "भू", TermKind.DHATU, itStatus = ItStatus.SET), DerivationTerm("suffix", "त", TermKind.PRATYAYA)),
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
        val result = DerivationEngine(listOf(ArdhadhatukasyedValadehSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "7.2.35" })
        assertEquals("इ", result.final.terms.single { it.id == "it-agama" }.surface)
    }

    @Test
    fun `nominal request creates a structured prātipadika derivation state`() {
        val state = SubantaDerivationRequest("राम", Vibhakti.PRATHAMA, Vacana.EKAVACANA).initialState()

        assertEquals(TermKind.PRATIPADIKA, state.terms.single().kind)
        assertTrue(HasMorphosyntax(vibhakti = Vibhakti.PRATHAMA).matches(state))
        assertTrue(HasMorphosyntax(vacana = Vacana.EKAVACANA).matches(state))
        assertEquals(Vibhakti.PRATHAMA, state.context.rupa.vibhakti)
        assertEquals(Vacana.EKAVACANA, state.context.rupa.vacana)
        assertEquals(Linga.PUMS, state.context.rupa.linga)
    }

    @Test
    fun `typed declarative conditions query derivational context`() {
        val state = TaddhitaDerivationRequest("पाश", DerivationalMeaning.SAMUHA).initialState()
        assertEquals(DerivationalMeaning.SAMUHA, state.context.requestedMeaning)
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
        assertEquals(SupAffix.SUP, SupAffix.fromContext(DerivationalContext(rupa = Rupa(vibhakti = Vibhakti.SAPTAMI, vacana = Vacana.BAHUVACANA))))
        assertEquals(null, SupAffix.fromContext(DerivationalContext(rupa = Rupa(vibhakti = Vibhakti.SAPTAMI))))
        assertEquals(null, SupAffix.fromContext(DerivationalContext(rupa = Rupa(vacana = Vacana.BAHUVACANA))))
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

        fun sutras(affix: SupAffix) = paradigm.forms.getValue(affix).applications.map { it.sutra }
        assertTrue(sutras(SupAffix.AU).contains("6.1.88"))
        assertTrue(sutras(SupAffix.JAS).containsAll(setOf("1.3.9", "6.1.102", "8.2.66")))
        assertTrue(sutras(SupAffix.AM).contains("6.1.107"))
        assertTrue(sutras(SupAffix.AUT).containsAll(setOf("1.3.3", "1.3.9", "6.1.88")))
        assertTrue(sutras(SupAffix.BHYAM_3).contains("7.3.102"))
        assertTrue(sutras(SupAffix.SUP).contains("8.3.59"))

        val report = paradigm.coverage
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
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARTARI)),
        )
        val condition = HasTermKind(TermKind.DHATU)
        val operation = IntroduceTerm(
            term = DerivationTerm("sap", "शप्", TermKind.PRATYAYA, setOf(ItMarker.SIT)),
            explanation = "Introduced a demonstration pratyaya.",
        )

        assertTrue(condition.matches(state))
        assertTrue(HasMorphosyntax(prayoga = Prayoga.KARTARI).matches(state))

        val result = operation.apply(state)

        assertEquals(DerivationStage.PRATYAYA_SELECTED, result.state.stage)
        assertEquals(TermKind.PRATYAYA, result.state.terms.single { it.id == "sap" }.kind)
        assertTrue(HasItMarker(ItMarker.SIT).matches(result.state))
    }

    @Test
    fun `conditions compose into a precise grammatical environment`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("bhu", "भू", TermKind.DHATU)),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARTARI)),
        )
        val condition = AllOf(
            HasTermKind(TermKind.DHATU),
            HasMorphosyntax(prayoga = Prayoga.KARTARI),
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

    @Test
    fun `asiddhavat visibility reverts substitutions and restores dropped terms from other abhiya rules`() {
        val ruleA = object : Sutra<DerivationState, DerivationChange>(
            number = "6.4.70", text = "RuleA", hindiExplanation = "", type = SutraType.NITYA,
            chapter = 6, pada = 4, optional = false, kramaValue = 640070,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.terms.any { it.id == "marker" }
            override fun apply(context: DerivationState): DerivationChange {
                val root = context.terms.first { it.id == "root" }
                return DerivationChange(
                    context.removeTerm("marker", sutra = "6.4.70")
                        .replaceTerm("root", root.copy(surface = "रा"))
                        .addSubstitution(VarnaSubstitution("root", 'र', "रा", "6.4.70")),
                    "Rule A applied"
                )
            }
        }

        val ruleB = object : Sutra<DerivationState, DerivationChange>(
            number = "6.4.60", text = "RuleB", hindiExplanation = "", type = SutraType.NITYA,
            chapter = 6, pada = 4, optional = false, kramaValue = 640060,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean {
                return context.terms.any { it.id == "root" && it.surface == "र" } &&
                       context.terms.any { it.id == "marker" }
            }
            override fun apply(context: DerivationState): DerivationChange {
                val root = context.terms.first { it.id == "root" }
                return DerivationChange(
                    context.replaceTerm("root", root.copy(surface = "म")),
                    "Rule B applied"
                )
            }
        }

        val initial = DerivationState(
            listOf(
                DerivationTerm("root", "र", TermKind.DHATU),
                DerivationTerm("marker", "म्", TermKind.PRATYAYA)
            )
        )

        val stateAfterA = ruleA.apply(initial).state
        assertEquals("रा", stateAfterA.terms.single().surface)
        assertTrue(stateAfterA.droppedTerms.any { it.id == "marker" })

        val visibleStateForB = RuleVisibility.view(ruleB, stateAfterA, mapOf("6.4.70" to ruleA, "6.4.60" to ruleB))
        assertTrue(ruleB.matches(visibleStateForB))

        val engine = DerivationEngine(listOf(ruleA, ruleB))
        val result = engine.derive(initial)
//        println("APPLICATIONS: " + result.applications.map { "${it.sutra}: ${it.before.surface} -> ${it.after.surface}" })
//        println("EVENTS: " + result.events)
        assertEquals("म", result.final.surface)
    }

    @Test
    fun `optional branching config skips or applies optional rules`() {
        val optionalRule = object : Sutra<DerivationState, DerivationChange>(
            number = "1.2.3", text = "OptRule", hindiExplanation = "", type = SutraType.NITYA,
            chapter = 1, pada = 2, optional = true, kramaValue = 120003,
            role = dev.sanskrit.sutra.SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
        ), DerivationSutra {
            override fun matches(context: DerivationState): Boolean = context.stage == DerivationStage.INITIAL
            override fun apply(context: DerivationState): DerivationChange =
                DerivationChange(context.copy(stage = DerivationStage.FINAL), "Applied optional rule")
        }

        val initial = DerivationState(listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)))
        val engine = DerivationEngine(listOf(optionalRule))

        val resultApply = engine.derive(initial, DerivationConfig(OptionalRulePolicy.APPLY_ALL))
        assertEquals(DerivationStage.FINAL, resultApply.final.stage)

        val resultSkip = engine.derive(initial, DerivationConfig(OptionalRulePolicy.SKIP_ALL))
        assertEquals(DerivationStage.INITIAL, resultSkip.final.stage)

        val resultCustomFalse = engine.derive(initial, DerivationConfig(OptionalRulePolicy.CUSTOM, optionalRuleSelector = { false }))
        assertEquals(DerivationStage.INITIAL, resultCustomFalse.final.stage)

        val resultCustomTrue = engine.derive(initial, DerivationConfig(OptionalRulePolicy.CUSTOM, optionalRuleSelector = { it == "1.2.3" }))
        assertEquals(DerivationStage.FINAL, resultCustomTrue.final.stage)
    }
}
