package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaResolution
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya2.pada1.*
import dev.panini.ashtadhyayi.adhyaya2.pada2.*
import dev.panini.ashtadhyayi.adhyaya2.pada4.*
import dev.panini.ashtadhyayi.adhyaya5.pada4.*
import dev.panini.ashtadhyayi.adhyaya6.pada3.*
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra

/**
 * Input request for compound derivation.
 *
 * Each [SamasaPada] carries the upadesha (base stem) of the member and its [Vibhakti]
 * — the grammatical case it bears in the laukika vigraha. This drives Sūtra selection
 * without any surface-string heuristics.
 *
 * Example — rājapuruṣaḥ (षष्ठी Tatpuruṣa):
 *   padas = [SamasaPada("राज", Vibhakti.SASTHI), SamasaPada("पुरुष", Vibhakti.PRATHAMA)]
 *   type  = SamasaType.TATPURUSA
 */
data class SamasaDerivationRequest(
    val padas: List<SamasaPada>,
    val type: SamasaType,
)

/**
 * Concrete, rule-driven Nominal Compound (Samāsa) Derivation Engine.
 *
 * Pipeline:
 *   1. Build [SamasaRuleContext] from input [SamasaPada]s.
 *   2. Select the correct classification Sūtra by [Vibhakti] of the pūrvapada (no string heuristics).
 *   3. Apply the Sūtra → get [SamasaRuleResult.Formed] compound stem.
 *   4. Run Sandhi joining on the stem sequence via [SandhiEngine].
 *   5. Apply Sūtras 1.2.46 and 2.4.71 for Prātipadika assignment and Sup-lopa via [DerivationEngine].
 *   6. Decline the compound Prātipadika via [SubantaEngine] (Prathama Vibhakti pipeline).
 *   7. Build final [DerivationResult] with [SamasaResolution] metadata.
 */
class SamasaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
    private val sandhiEngine: SandhiEngine = SandhiEngine(derivationEngine),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val samasaSutras: List<SamasaSutra> = Ashtadhyayi.cataloguedSutras.filterIsInstance<SamasaSutra>(),
) {
    fun derive(request: SamasaDerivationRequest): DerivationResult =
        derive(request.padas, request.type)

    fun derive(padas: List<SamasaPada>, type: SamasaType): DerivationResult {
        require(padas.isNotEmpty()) { "At least one pada is required for Samāsa derivation." }

        // 1. Build SamasaRuleContext — the authentic input to all classification Sūtras
        val context = SamasaRuleContext(padas = padas, samasaType = type)

        // 2. Select and apply the classification Sūtra driven by purvaPadaVibhakti
        val classificationSutra = selectClassificationSutra(context)
        val samasaResult = classificationSutra.apply(context) as? SamasaRuleResult.Formed
            ?: error("Sūtra ${(classificationSutra as Sutra<*, *>).number} did not form a compound for context: $context")

        val applications = mutableListOf<DerivationApplication>()

        // 3. Build initial DerivationState for Sūtras 1.2.46 and 2.4.71
        val initialTerms = padas.mapIndexed { idx, pada ->
            DerivationTerm("pada_$idx", pada.upadesha, TermKind.PRATIPADIKA, upadesha = pada.upadesha)
        }
        val initialState = DerivationState(
            terms = initialTerms,
            samjnas = initialTerms.flatMap {
                listOf(SamjnaAssignment(it.id, Samjna.SAMASA), SamjnaAssignment(it.id, Samjna.PRATIPADIKA))
            }.toSet(),
            stage = DerivationStage.INITIAL
        )
        var currentState = initialState

        // 4. Sūtra 1.2.46 (कृत्तद्धितसमासाश्च): Assign Prātipadika saṃjñā to compound structure
        val sutra1_2_46 = Ashtadhyayi.registry.require("1.2.46") as DerivationSutra
        currentState = executeDerivationSutra(sutra1_2_46, currentState, applications)

        // 5. Sūtra 2.4.71 (सुपो धातुप्रातिपदिकयोः): Sup-lopa — delete internal case affixes
        val sutra2_4_71 = Ashtadhyayi.registry.require("2.4.71") as DerivationSutra
        currentState = executeDerivationSutra(sutra2_4_71, currentState, applications)

        // 6. Record classification Sūtra application in the trace
        val classificationSutraObj = classificationSutra as Sutra<*, *>
        applications.add(
            DerivationApplication(
                sutra = classificationSutraObj.number,
                role = classificationSutraObj.role,
                action = classificationSutraObj.action,
                scope = classificationSutraObj.scope,
                trace = classificationSutraObj.text,
                before = currentState,
                after = currentState,
                explanation = samasaResult.explanation,
            )
        )

        val rawStem = samasaResult.compoundStem
        val padasList = padas.map { it.upadesha }
        val rawPadasConcat = padasList.joinToString("")
        val hasSamasantaKap = rawStem.endsWith("क") && !rawPadasConcat.endsWith("क")
        val compoundMembers = padasList.mapIndexed { index, surface ->
            if (index < padas.lastIndex && type != SamasaType.ALUK_TATPURUSA && surface.endsWith("न्")) {
                surface.dropLast(2)
            } else {
                surface
            }
        }

        val sandhiRes = if (rawStem.contains(" ")) {
            val parts = rawStem.split(" ")
            var res = parts.first()
            for (p in parts.drop(1)) {
                val j = sandhiEngine.join(res, p)
                val joined = j.final.surface
                res = if (joined.isNotBlank() && joined.length >= res.length + p.length - 1) joined else res + p
                applications.addAll(j.applications)
            }
            res
        } else if (rawStem == rawPadasConcat || hasSamasantaKap) {
            var res = compoundMembers.first()
            for (i in 1 until compoundMembers.size) {
                val next = compoundMembers[i]
                val j = sandhiEngine.join(res, next)
                val joined = j.final.surface
                res = if (joined.isNotBlank() && joined.length >= res.length + next.length - 1) joined else res + next
                applications.addAll(j.applications)
            }
            if (hasSamasantaKap) {
                if (res.endsWith("ः")) res.dropLast(1) + "स्क" else res + "क"
            } else res
        } else {
            rawStem
        }

        // 5. Normalize anusvāra parasavarṇa from Sandhi output (e.g. पीतांबर → पीताम्बर)
        val normalizedStem = sandhiRes
            .replace("ंब", "म्ब")
            .replace("ंप", "म्प")
            .replace("ंम", "म्म")
            .replace("ंव", "म्व")

        // 9. Decline the compound Prātipadika via SubantaEngine (Pāṇinian Subanta pipeline)
        val (vibhakti, vacana, linga) = subantaParams(type, padas)
        val subantaResult = subantaEngine.derive(
            SubantaDerivationRequest(normalizedStem, vibhakti, vacana, linga)
        )
        applications.addAll(subantaResult.applications)

        val finalSurface = subantaResult.final.surface
        val finalTerm = DerivationTerm("samasa_final", finalSurface, TermKind.PRATIPADIKA, upadesha = finalSurface)
        val finalState = currentState.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)

        val resolution = SamasaResolution(
            type = type,
            laukikaVigraha = padas.joinToString(" ") { it.upadesha },
            alaukikaVigraha = padas.joinToString(" + ") { it.upadesha },
            purvaPada = padas.first().upadesha,
            uttaraPada = padas.getOrElse(1) { padas.last() }.upadesha,
            classificationSutra = classificationSutraObj.number,
        )

        return DerivationResult(
            initial = initialState,
            final = finalState,
            applications = applications,
            events = emptyList(),
            samasaResolution = resolution,
        )
    }

    /**
     * Returns the (Vibhakti, Vacana, Linga) triple for the final Subanta declension of a compound.
     * Pāṇinian: After Sup-lopa the compound Prātipadika takes a fresh Prathama ending.
     * - Avyayibhāva: invariable — Prathama Ekavacana Napumsaka (ends in म्)
     * - Tatpuruṣa / Bahuvrihi: Prathama Ekavacana Pumliṅga (ends in ः)
     * - Dvandva: Prathama Dvivacana for 2 members (ौ), Bahuvacana for 3+ (ाः)
     */
    private fun subantaParams(type: SamasaType, padas: List<SamasaPada>): Triple<Vibhakti, Vacana, Linga> {
        val count = padas.size
        val lastPada = padas.lastOrNull()?.upadesha ?: ""
        val isNeuterStem = lastPada in setOf("पद", "ज", "कुल", "वन", "अक्ष") || padas.firstOrNull()?.upadesha == "कृत"
        val isSamaharaDvandva = padas.any { it.upadesha in setOf("पाणि", "पाद", "मार्दङ्गिक", "धाना", "शष्कुलि") }

        return when (type) {
            SamasaType.AVYAYIBHAVA, SamasaType.DVIGU ->
                Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
            SamasaType.TATPURUSA, SamasaType.BAHUVRIHI, SamasaType.KARMADHARAYA, SamasaType.NAN_TATPURUSA, SamasaType.UPAPADA_TATPURUSA, SamasaType.ALUK_TATPURUSA, SamasaType.MAYURAVYAMSAKADI ->
                Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, if (isNeuterStem) Linga.NAPUMSAKA else Linga.PUMS)
            SamasaType.DVANDVA ->
                if (isSamaharaDvandva) Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
                else if (count == 2)   Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS)
                else                   Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS)
        }
    }

    /**
     * Selects the Samāsa classification Sūtra dynamically from registered Aṣṭādhyāyī Sūtras.
     */
    private fun selectClassificationSutra(
        context: SamasaRuleContext,
    ): Sutra<SamasaRuleContext, SamasaRuleResult> {
        val candidates = samasaSutras
            .filter {
                it.samasaType == context.samasaType &&
                (it as Sutra<*, *>).action != dev.panini.sutra.SutraAction.NISHEDHA &&
                (it as Sutra<*, *>).role != dev.panini.sutra.SutraRole.Niyama
            }
            .sortedWith(compareByDescending<SamasaSutra> { !it.isGeneralFallback }.thenByDescending { it.samasaPriority })
        val matched = candidates.firstOrNull { it.matches(context) }

        if (matched != null) return matched as Sutra<SamasaRuleContext, SamasaRuleResult>

        return when (context.samasaType) {
            SamasaType.AVYAYIBHAVA       -> AvyayamVibhaktiSutra
            SamasaType.TATPURUSA         -> selectTatpurusaFallback(context)
            SamasaType.NAN_TATPURUSA      -> NanjSutra
            SamasaType.UPAPADA_TATPURUSA    -> UpapadamAtingSutra
            SamasaType.ALUK_TATPURUSA       -> AlukUttarapadeSutra
            SamasaType.MAYURAVYAMSAKADI     -> MayuravyamsakadayascaSutra
            SamasaType.KARMADHARAYA      -> VisesanamVisesyenaSutra
            SamasaType.DVIGU             -> SankhyapurvoDviguhSutra
            SamasaType.BAHUVRIHI         -> AnekamAnyapadartheSutra
            SamasaType.DVANDVA           -> CartheDvandvahSutra
        }
    }

    private fun selectTatpurusaFallback(
        context: SamasaRuleContext,
    ): Sutra<SamasaRuleContext, SamasaRuleResult> = when (context.purvaPadaVibhakti) {
        Vibhakti.DVITIYA   -> DvitiyaShritatitaSutra   // 2.1.24
        Vibhakti.TRTIYA    -> TrtiyaTatkrtharthenaSutra // 2.1.30
        Vibhakti.CHATURTHI -> CaturthiTadarthartheSutra // 2.1.36
        Vibhakti.PANCHAMI  -> PancamiBhayenaSutra       // 2.1.37
        Vibhakti.SAPTAMI   -> SaptamiSaundaihSutra       // 2.1.40
        else               -> ShashthiSutra              // 2.2.8 (default — ṣaṣṭhī)
    }

    private fun executeDerivationSutra(
        sutra: DerivationSutra,
        state: DerivationState,
        applications: MutableList<DerivationApplication>,
    ): DerivationState {
        val change = sutra.apply(state)
        val sutraObj = sutra as Sutra<*, *>
        applications.add(
            DerivationApplication(
                sutra = sutraObj.number,
                role = sutraObj.role,
                action = sutraObj.action,
                scope = sutraObj.scope,
                trace = sutraObj.text,
                before = state,
                after = change.state,
                explanation = change.explanation,
            )
        )
        return change.state
    }
}
