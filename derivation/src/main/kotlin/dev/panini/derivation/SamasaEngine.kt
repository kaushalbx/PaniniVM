package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaResolution
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya2.pada1.AvyayamVibhaktiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.DvitiyaShritatitaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.PancamiBhayenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.TrtiyaTatkrtharthenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiSutra
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.shiksha.Samjna
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

        // 3. Sandhi joining of the stem sequence
        var joinedStem = padas.first().upadesha
        val applications = mutableListOf<DerivationApplication>()
        for (i in 1 until padas.size) {
            val nextStem = padas[i].upadesha
            val sandhiRes = sandhiEngine.join(joinedStem, nextStem)
            val resSurface = sandhiRes.final.surface
            joinedStem = resSurface.ifBlank { joinedStem + nextStem }
            applications.addAll(sandhiRes.applications)
        }

        // 4. Build initial DerivationState for Sūtras 1.2.46 and 2.4.71
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

        // 5. Sūtra 1.2.46 (कृत्तद्धितसमासाश्च): Assign Prātipadika saṃjñā to compound structure
        val sutra1_2_46 = Ashtadhyayi.registry.require("1.2.46") as DerivationSutra
        currentState = executeDerivationSutra(sutra1_2_46, currentState, applications)

        // 6. Sūtra 2.4.71 (सुपो धातुप्रातिपदिकयोः): Sup-lopa — delete internal case affixes
        val sutra2_4_71 = Ashtadhyayi.registry.require("2.4.71") as DerivationSutra
        currentState = executeDerivationSutra(sutra2_4_71, currentState, applications)

        // 7. Record classification Sūtra application in the trace
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

        // 8. Normalize anusvāra parasavarṇa from Sandhi output (e.g. पीतांबर → पीताम्बर)
        val normalizedStem = joinedStem
            .replace("ंब", "म्ब")
            .replace("ंप", "म्प")
            .replace("ंम", "म्म")
            .replace("ंव", "म्व")

        // 9. Decline the compound Prātipadika via SubantaEngine (Pāṇinian Subanta pipeline)
        val (vibhakti, vacana, linga) = subantaParams(type, padas.size)
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
    private fun subantaParams(type: SamasaType, count: Int): Triple<Vibhakti, Vacana, Linga> = when (type) {
        SamasaType.AVYAYIBHAVA ->
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
        SamasaType.TATPURUSA, SamasaType.BAHUVRIHI ->
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS)
        SamasaType.DVANDVA ->
            if (count == 2) Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS)
            else            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS)
    }

    /**
     * Selects the Samāsa classification Sūtra based on [SamasaRuleContext].
     * For Tatpuruṣa, selection is driven by purvaPadaVibhakti — zero string heuristics.
     */
    private fun selectClassificationSutra(
        context: SamasaRuleContext,
    ): Sutra<SamasaRuleContext, SamasaRuleResult> = when (context.samasaType) {
        SamasaType.AVYAYIBHAVA -> AvyayamVibhaktiSutra
        SamasaType.TATPURUSA   -> selectTatpurusaSutra(context.purvaPadaVibhakti)
        SamasaType.BAHUVRIHI   -> AnekamAnyapadartheSutra
        SamasaType.DVANDVA     -> CartheDvandvahSutra
    }

    /**
     * Dispatches Tatpuruṣa sub-type Sūtra by the pūrvapada's Vibhakti.
     * Pāṇinian: the case of the prior member determines the compound sub-type.
     */
    private fun selectTatpurusaSutra(
        vibhakti: Vibhakti,
    ): Sutra<SamasaRuleContext, SamasaRuleResult> = when (vibhakti) {
        Vibhakti.DVITIYA  -> DvitiyaShritatitaSutra   // 2.1.24
        Vibhakti.TRTIYA   -> TrtiyaTatkrtharthenaSutra // 2.1.30
        Vibhakti.PANCHAMI -> PancamiBhayenaSutra       // 2.1.37
        else              -> ShashthiSutra              // 2.2.8 (default — ṣaṣṭhī)
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

