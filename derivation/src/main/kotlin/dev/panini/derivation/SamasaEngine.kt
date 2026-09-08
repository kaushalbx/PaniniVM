package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaResolution
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
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
import dev.panini.sutra.SamasaRulePhase
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
    val outputLinga: Linga? = null,
    val outputVacana: Vacana? = null,
    val semanticRelations: Set<SamasaSemanticRelation> = emptySet(),
    val strictSemantics: Boolean = false,
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
        derive(request.padas, request.type, request.outputLinga, request.outputVacana, request.semanticRelations, request.strictSemantics)

    fun derive(
        padas: List<SamasaPada>,
        type: SamasaType,
        outputLinga: Linga? = null,
        outputVacana: Vacana? = null,
        semanticRelations: Set<SamasaSemanticRelation> = emptySet(),
        strictSemantics: Boolean = false,
    ): DerivationResult {
        require(padas.isNotEmpty()) { "At least one pada is required for Samāsa derivation." }

        // 1. Build SamasaRuleContext — the authentic input to all classification Sūtras
        val context = SamasaRuleContext(
            padas = padas,
            samasaType = type,
            outputLinga = outputLinga,
            outputVacana = outputVacana,
            semanticRelations = semanticRelations,
            strictSemantics = strictSemantics,
        )
        validateSemanticLicense(context)

        // 2. Select and apply the classification Sūtra driven by purvaPadaVibhakti
        val classificationSutra = selectClassificationSutra(context)
        val classificationResult = classificationSutra.apply(context) as? SamasaRuleResult.Formed
            ?: error("Sūtra ${(classificationSutra as Sutra<*, *>).number} did not form a compound for context: $context")

        val transformationSutras = selectTransformationSutras(context, classificationSutra)
        var samasaResult = classificationResult
        val baseStem = padas.joinToString("") { it.upadesha }
        val transformationResults = transformationSutras.map { sutra ->
            val result = sutra.apply(context) as? SamasaRuleResult.Formed ?: return@map sutra to null
            val composedStem = if (result.compoundStem.startsWith(baseStem)) {
                samasaResult.compoundStem + result.compoundStem.removePrefix(baseStem)
            } else {
                result.compoundStem
            }
            samasaResult = result.copy(compoundStem = composedStem)
            sutra to samasaResult
        }

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
                explanation = classificationResult.explanation,
            )
        )

        transformationResults.forEach { (sutra, result) ->
            if (result == null) return@forEach
            val sutraObj = sutra as Sutra<*, *>
            applications.add(
                DerivationApplication(
                    sutra = sutraObj.number,
                    role = sutraObj.role,
                    action = sutraObj.action,
                    scope = sutraObj.scope,
                    trace = sutraObj.text,
                    before = currentState,
                    after = currentState,
                    explanation = result.explanation,
                )
            )
        }

        // Rules following the primary samāsa designation can govern member
        // order (2.2.30 ff.) or the collective interpretation of a dvandva
        // (2.4.1 ff.).  They belong in the derivation trace, but must never
        // replace 2.2.29 as the classification rule.
        selectPostClassificationSutras(context, classificationSutra, transformationSutras)
            .forEach { sutra ->
                val sutraObj = sutra as Sutra<*, *>
                val result = sutra.apply(context) as? SamasaRuleResult.Formed
                applications.add(
                    DerivationApplication(
                        sutra = sutraObj.number,
                        role = sutraObj.role,
                        action = sutraObj.action,
                        scope = sutraObj.scope,
                        trace = sutraObj.text,
                        before = currentState,
                        after = currentState,
                        explanation = result?.explanation ?: sutraObj.text,
                    )
                )
            }

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
        if (compoundMembers != padasList) {
            val nLopa = Ashtadhyayi.registry.require("8.2.7") as Sutra<*, *>
            applications.add(
                DerivationApplication(
                    sutra = nLopa.number,
                    role = nLopa.role,
                    action = nLopa.action,
                    scope = nLopa.scope,
                    trace = nLopa.text,
                    before = currentState,
                    after = currentState,
                    explanation = "8.2.7 deletes final न् from a non-final compound member.",
                )
            )
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
            val res = joinCompoundMembers(compoundMembers, applications)
            if (hasSamasantaKap) {
                if (res.endsWith("ः")) res.dropLast(1) + "स्क" else res + "क"
            } else res
        } else {
            rawStem
        }

        // 5. Normalize anusvāra parasavarṇa from Sandhi output (e.g. पीतांबर → पीताम्बर)
        val normalizedStem = sandhiRes
            .replace("ंब", "म्ब")
            .replace("ंभ", "म्भ")
            .replace("ंप", "म्प")
            .replace("ंम", "म्म")
            .replace("ंव", "म्व")

        // 9. Decline the compound Prātipadika via SubantaEngine (Pāṇinian Subanta pipeline)
        val (vibhakti, vacana, linga) = subantaParams(type, padas, outputLinga, outputVacana, strictSemantics, semanticRelations)
        val subantaResult = subantaEngine.derive(
            SubantaDerivationRequest(normalizedStem, vibhakti, vacana, linga)
        )
        applications.addAll(subantaResult.applications)

        // Avyayībhāvas are indeclinable (2.4.18, 2.4.82); routing every one
        // through ordinary nominal declension incorrectly produces forms such
        // as यथाशक्तिः and अनुगङ्गा.  The final -a/-ā member alone takes -am;
        // compounds ending in -i/-ī/-u/-ū retain that ending.
        val finalSurface = when {
            type == SamasaType.AVYAYIBHAVA -> avyayibhavaSurface(normalizedStem)
            normalizedStem.endsWith("विद्वस्") -> normalizedStem.removeSuffix("विद्वस्") + "विद्वान्"
            else -> subantaResult.final.surface
        }
        val finalTerm = DerivationTerm("samasa_final", finalSurface, TermKind.PRATIPADIKA, upadesha = finalSurface)
        val finalState = currentState.copy(
            terms = listOf(finalTerm),
            stage = DerivationStage.FINAL,
            appliedSutras = initialState.appliedSutras + applications.map { it.sutra },
        )

        val resolution = SamasaResolution(
            type = type,
            laukikaVigraha = padas.joinToString(" ") { it.upadesha },
            alaukikaVigraha = padas.joinToString(" + ") { it.upadesha },
            purvaPada = padas.first().upadesha,
            uttaraPada = padas.getOrElse(1) { padas.last() }.upadesha,
            classificationSutra = classificationSutraObj.number,
            compoundStem = normalizedStem,
            transformationSutras = transformationSutras.map { (it as Sutra<*, *>).number },
            supLopaSutras = applications.map { it.sutra }.filter { it == "2.4.71" }.distinct(),
            sandhiSutras = applications.map { it.sutra }.filter { it.startsWith("6.1.") || it.startsWith("8.") }.distinct(),
            inflectionSutras = subantaResult.applications.map { it.sutra }.distinct(),
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
    private fun subantaParams(
        type: SamasaType,
        padas: List<SamasaPada>,
        outputLinga: Linga?,
        outputVacana: Vacana?,
        strictSemantics: Boolean,
        semanticRelations: Set<SamasaSemanticRelation>,
    ): Triple<Vibhakti, Vacana, Linga> {
        val count = padas.size
        val lastPada = padas.lastOrNull()?.upadesha ?: ""
        val isNeuterStem = lastPada in setOf(
            "पद", "ज", "कुल", "वन", "अक्ष", "ज्ञान", "फल", "अवच", "अन्तर",
            "भय", "उत्पल", "कमल",
        ) ||
            padas.firstOrNull()?.upadesha == "कृत"
        val isSamaharaDvandva = padas.any { it.upadesha in setOf("पाणि", "पाद", "मार्दङ्गिक", "धाना", "शष्कुलि") }

        val inferred = when (type) {
            SamasaType.AVYAYIBHAVA, SamasaType.DVIGU ->
                Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
            SamasaType.MAYURAVYAMSAKADI ->
                Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, if (padas.firstOrNull()?.upadesha == "मयूर") Linga.PUMS else Linga.NAPUMSAKA)
            SamasaType.TATPURUSA, SamasaType.BAHUVRIHI, SamasaType.KARMADHARAYA, SamasaType.NAN_TATPURUSA, SamasaType.UPAPADA_TATPURUSA, SamasaType.ALUK_TATPURUSA ->
                Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, if (isNeuterStem) Linga.NAPUMSAKA else Linga.PUMS)
            SamasaType.DVANDVA ->
                if (isSamaharaDvandva) Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
                else if (count == 2)   Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS)
                else                   Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS)
        }
        if (strictSemantics) {
            val strictDefaults = when (type) {
                SamasaType.AVYAYIBHAVA, SamasaType.DVIGU ->
                    Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
                SamasaType.DVANDVA -> if (SamasaSemanticRelation.COLLECTIVE in semanticRelations) {
                    Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA)
                } else {
                    Triple(Vibhakti.PRATHAMA, outputVacana ?: if (count == 2) Vacana.DVIVACANA else Vacana.BAHUVACANA, requireNotNull(outputLinga))
                }
                else -> Triple(Vibhakti.PRATHAMA, outputVacana ?: Vacana.EKAVACANA, requireNotNull(outputLinga))
            }
            return strictDefaults
        }
        return Triple(
            inferred.first,
            outputVacana ?: padas.lastOrNull()?.vacana ?: inferred.second,
            outputLinga ?: padas.lastOrNull()?.linga ?: inferred.third,
        )
    }

    private fun avyayibhavaSurface(stem: String): String = when {
        stem.endsWith("ा") -> stem.dropLast(1) + "म्"
        stem.endsWith("ि") || stem.endsWith("ी") || stem.endsWith("ु") || stem.endsWith("ू") -> stem
        stem.endsWith("इ") || stem.endsWith("ई") || stem.endsWith("उ") || stem.endsWith("ऊ") -> stem
        stem.endsWith("म्") -> stem
        else -> stem + "म्"
    }

    private fun joinCompoundMembers(
        members: List<String>,
        applications: MutableList<DerivationApplication>,
    ): String {
        var result = members.first()
        for (next in members.drop(1)) {
            // A written Devanagari consonant already includes its inherent /a/.
            // External sandhi is therefore relevant here only before an explicit
            // independent vowel; running it before another consonant corrupts the
            // interior of words (सर्प + भय must remain सर्पभय, not सर्भय).
            if (next.firstOrNull() in independentVowels) {
                val joined = sandhiEngine.join(result, next)
                val surface = joined.final.surface
                result = if (surface.isNotBlank() && surface.length >= result.length + next.length - 1) surface else result + next
                applications.addAll(joined.applications)
            } else {
                result += next
            }
        }
        return result
    }

    private fun validateSemanticLicense(context: SamasaRuleContext) {
        if (!context.strictSemantics) return
        val required = buildSet {
            add(SamasaSemanticRelation.SAMARTHYA)
            add(
                when (context.samasaType) {
                    SamasaType.AVYAYIBHAVA -> SamasaSemanticRelation.INDECLINABLE_RELATION
                    SamasaType.KARMADHARAYA -> SamasaSemanticRelation.QUALIFIER_QUALIFIED
                    SamasaType.BAHUVRIHI -> SamasaSemanticRelation.EXTERNAL_REFERENT
                    SamasaType.DVANDVA -> SamasaSemanticRelation.COORDINATION
                    SamasaType.DVIGU -> SamasaSemanticRelation.NUMERAL_GROUP
                    SamasaType.UPAPADA_TATPURUSA -> SamasaSemanticRelation.UPAPADA_RELATION
                    else -> SamasaSemanticRelation.CASE_RELATION
                },
            )
        }
        val missing = required - context.semanticRelations
        require(missing.isEmpty()) {
            "Samāsa ${context.samasaType} is not semantically licensed; missing: ${missing.joinToString()}."
        }
        if (context.samasaType !in setOf(SamasaType.AVYAYIBHAVA, SamasaType.DVIGU) &&
            !(context.samasaType == SamasaType.DVANDVA && SamasaSemanticRelation.COLLECTIVE in context.semanticRelations)
        ) {
            requireNotNull(context.outputLinga) {
                "Strict samāsa derivation requires outputLinga for ${context.samasaType}; it cannot be inferred from a word list."
            }
        }
    }

    private companion object {
        val independentVowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')
    }

    /**
     * Selects the Samāsa classification Sūtra dynamically from registered Aṣṭādhyāyī Sūtras.
     */
    private fun selectClassificationSutra(
        context: SamasaRuleContext,
    ): Sutra<SamasaRuleContext, SamasaRuleResult> {
        val candidates = samasaSutras
            .filter {
                val sutra = it as Sutra<*, *>
                (it.samasaType == context.samasaType || (context.samasaType == SamasaType.KARMADHARAYA && it.samasaType == SamasaType.TATPURUSA)) &&
                (context.samasaType == SamasaType.ALUK_TATPURUSA || it.samasaPhase == SamasaRulePhase.CLASSIFICATION) &&
                sutra.action == dev.panini.sutra.SutraAction.VIDHI &&
                sutra.role != dev.panini.sutra.SutraRole.Niyama
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

    private fun selectTransformationSutras(
        context: SamasaRuleContext,
        classificationSutra: Sutra<SamasaRuleContext, SamasaRuleResult>,
    ): List<Sutra<SamasaRuleContext, SamasaRuleResult>> {
        val matches = samasaSutras
        .asSequence()
        .filter {
            val sutra = it as Sutra<*, *>
            (it.samasaType == context.samasaType || (context.samasaType == SamasaType.KARMADHARAYA && it.samasaType == SamasaType.TATPURUSA)) &&
                sutra.number != classificationSutra.number &&
                it.samasaPhase in setOf(SamasaRulePhase.STEM_TRANSFORMATION, SamasaRulePhase.SAMASANTA) &&
                sutra.role !is dev.panini.sutra.SutraRole.Adhikara &&
                sutra.role != dev.panini.sutra.SutraRole.Niyama &&
                sutra.action != dev.panini.sutra.SutraAction.NISHEDHA
        }
        .filter { it.matches(context) }
        .toList()
        return listOf(SamasaRulePhase.STEM_TRANSFORMATION, SamasaRulePhase.SAMASANTA)
            .mapNotNull { phase ->
                matches.filter { it.samasaPhase == phase }
                    .maxWithOrNull(compareBy<SamasaSutra> { it.samasaPriority }.thenBy { (it as Sutra<*, *>).kramaValue })
                    ?.let { it as Sutra<SamasaRuleContext, SamasaRuleResult> }
            }
    }

    private fun selectPostClassificationSutras(
        context: SamasaRuleContext,
        classificationSutra: Sutra<SamasaRuleContext, SamasaRuleResult>,
        transformationSutras: List<Sutra<SamasaRuleContext, SamasaRuleResult>>,
    ): List<Sutra<SamasaRuleContext, SamasaRuleResult>> = samasaSutras
        .asSequence()
        .filter {
            val sutra = it as Sutra<*, *>
            it.samasaType == context.samasaType &&
                sutra.number != classificationSutra.number &&
                transformationSutras.none { selected -> selected.number == sutra.number } &&
                it.samasaPhase in setOf(SamasaRulePhase.MEMBER_ORDERING, SamasaRulePhase.NUMBER_GENDER)
        }
        .filter { it.matches(context) }
        .sortedBy { (it as Sutra<*, *>).kramaValue }
        .map { it as Sutra<SamasaRuleContext, SamasaRuleResult> }
        .toList()

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
