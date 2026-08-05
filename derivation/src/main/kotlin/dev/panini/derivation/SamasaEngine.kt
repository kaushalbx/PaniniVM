package dev.panini.derivation

import dev.panini.analysis.SamasaResolution
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya2.pada1.AvyayamVibhaktiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.DvitiyaShritatitaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.PancamiBhayenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.TrtiyaTatkrtharthenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiSutra
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra

data class SamasaDerivationRequest(
    val padas: List<String>,
    val type: SamasaType,
)

/**
 * Concrete, rule-driven Nominal Compound (Samāsa) Derivation Engine.
 * Executes Sūtra pipelines (1.2.46, 2.4.71, Sandhi, and 2.1/2.2 classification).
 */
class SamasaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
    private val sandhiEngine: SandhiEngine = SandhiEngine(derivationEngine),
) {
    fun derive(request: SamasaDerivationRequest): DerivationResult =
        derive(request.padas, request.type)

    fun derive(padas: List<String>, type: SamasaType): DerivationResult {
        require(padas.isNotEmpty()) { "At least one pada is required for Samasa derivation." }

        // 1. Formulate initial terms and assign SAMASA & PRATIPADIKA saṃjñās
        val initialTerms = padas.mapIndexed { idx, pada ->
            DerivationTerm("pada_$idx", pada, TermKind.PRATIPADIKA, upadesha = pada)
        }
        val initialState = DerivationState(
            terms = initialTerms,
            samjnas = initialTerms.flatMap {
                listOf(SamjnaAssignment(it.id, Samjna.SAMASA), SamjnaAssignment(it.id, Samjna.PRATIPADIKA))
            }.toSet(),
            stage = DerivationStage.INITIAL
        )
        var currentState = initialState
        val applications = mutableListOf<DerivationApplication>()

        // 2. Sūtra 1.2.46 (कृत्तद्धितसमासाश्च): Assign Prātipadika Saṃjñā to the compound structure
        val sutra1_2_46 = Ashtadhyayi.registry.require("1.2.46") as DerivationSutra
        currentState = executeSutra(sutra1_2_46, currentState, applications)

        // 3. Sūtra 2.4.71 (सुपो धातुप्रातिपदिकयोः): Internal case deletion (Sup-Lopa)
        val sutra2_4_71 = Ashtadhyayi.registry.require("2.4.71") as DerivationSutra
        currentState = executeSutra(sutra2_4_71, currentState, applications)

        // 4. Sandhi Joining of Stem Components
        val stems = currentState.terms.map { it.surface }
        var joinedStem = stems.first()
        for (i in 1 until stems.size) {
            val nextStem = stems[i]
            val sandhiRes = sandhiEngine.join(joinedStem, nextStem)
            val resSurface = sandhiRes.final.surface
            joinedStem = if (resSurface.isNotBlank()) resSurface else joinedStem + nextStem
            applications.addAll(sandhiRes.applications)
        }

        // 5. Categorical Compound Classification Sūtra Selection & Application
        val classificationSutra: DerivationSutra = when (type) {
            SamasaType.AVYAYIBHAVA -> AvyayamVibhaktiSutra
            SamasaType.TATPURUSA -> selectTatpurushaClassificationSutra(padas, stems)
            SamasaType.BAHUVRIHI -> AnekamAnyapadartheSutra
            SamasaType.DVANDVA -> CartheDvandvahSutra
        }
        currentState = executeSutra(classificationSutra, currentState, applications)

        // 6. Subanta Gender / Number Inflection Assignment
        val finalSurface = applySubantaDeclension(joinedStem, type, padas.size)
        val finalTerm = DerivationTerm("samasa_final", finalSurface, TermKind.PRATIPADIKA, upadesha = finalSurface)
        val finalState = currentState.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)

        val resolution = SamasaResolution(
            type = type,
            laukikaVigraha = padas.joinToString(" "),
            alaukikaVigraha = padas.joinToString(" + "),
            purvaPada = padas.firstOrNull() ?: "",
            uttaraPada = padas.getOrElse(1) { "" },
            classificationSutra = (classificationSutra as Sutra<*, *>).number,
        )

        return DerivationResult(
            initial = initialState,
            final = finalState,
            applications = applications,
            events = emptyList(),
            samasaResolution = resolution,
        )
    }

    private fun executeSutra(
        sutra: DerivationSutra,
        state: DerivationState,
        applications: MutableList<DerivationApplication>,
    ): DerivationState {
        val change = sutra.apply(state)
        val sutraObj = sutra as Sutra<*, *>
        val app = DerivationApplication(
            sutra = sutraObj.number,
            role = sutraObj.role,
            action = sutraObj.action,
            scope = sutraObj.scope,
            trace = sutraObj.text,
            before = state,
            after = change.state,
            explanation = change.explanation
        )
        applications.add(app)
        return change.state
    }

    private fun selectTatpurushaClassificationSutra(padas: List<String>, stems: List<String>): DerivationSutra {
        val second = padas.getOrElse(1) { stems.getOrElse(1) { "" } }
        return when {
            second.contains("श्रित") || second.contains("अतीत") || second.contains("पतित") -> DvitiyaShritatitaSutra
            second.contains("भय") -> PancamiBhayenaSutra
            second.contains("खण्ड") -> TrtiyaTatkrtharthenaSutra
            else -> ShashthiSutra
        }
    }

    private fun applySubantaDeclension(joinedStem: String, type: SamasaType, count: Int): String {
        val stem = joinedStem.replace("ंबर", "म्बर")
        return when (type) {
            SamasaType.AVYAYIBHAVA -> {
                if (stem.endsWith("म्") || stem.endsWith("म")) stem
                else stem + "म्"
            }
            SamasaType.TATPURUSA, SamasaType.BAHUVRIHI -> {
                if (stem.endsWith("ः") || stem.endsWith("म्")) stem
                else stem + "ः"
            }
            SamasaType.DVANDVA -> {
                if (count == 2) {
                    if (stem.endsWith("ौ")) stem else stem + "ौ"
                } else {
                    if (stem.endsWith("ाः")) stem else stem + "ाः"
                }
            }
        }
    }
}
