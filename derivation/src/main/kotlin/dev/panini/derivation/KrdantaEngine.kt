package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import dev.panini.shiksha.Varnamala

data class KrdantaDerivationRequest(val dhatu: String, val samjna: Samjna, val upasarga: String? = null)

data class KrdantaSourceStem(val surface: String, val supportsAStemDeclension: Boolean, val preservesSourceSurface: Boolean)

class KrdantaEngine(
    private val pipeline: DerivationPipeline = DerivationPipeline(
        stages = listOf(SutraStage.IT_PROCESSING, SutraStage.ANGAKARYA, SutraStage.VOWEL_SANDHI, SutraStage.SANDHI),
        finalizeState = { it.copy(stage = DerivationStage.FINAL) },
        sutrasForStage = Ashtadhyayi::krdantaSutrasAt,
        interleaveItProcessingAt = setOf(SutraStage.SANDHI),
    ),
) {
    fun deriveSourceStem(dhatu: String, pratyaya: String): KrdantaSourceStem {
        val samjna = when {
            pratyaya == "घञ्" && dhatu in supportedGhanDhatus -> Samjna.GHAN
            pratyaya == "अप्" && dhatu in supportedApDhatus -> Samjna.GHAN
            pratyaya in setOf("ल्युट्", "अन") && dhatu in supportedLyutDhatus -> Samjna.LYUT
            else -> null
        }
        return if (samjna != null) {
            KrdantaSourceStem(derive(KrdantaDerivationRequest(dhatu, samjna)).final.surface, true, false)
        } else {
            KrdantaSourceStem(sourceFallbackStems[dhatu] ?: dhatu, false, dhatu in preservedSourceStems)
        }
    }

    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        val entry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, entry, request.dhatu)
        val selection = canonicalSelectionSutra(request)
        require(selection.matches(initial)) {
            "Canonical sutra ${selection.sutra} cannot select ${request.samjna} for ${request.dhatu}."
        }
        val bootstrap = buildList {
            add(selection)
            if (request.samjna == Samjna.KTVA && !request.upasarga.isNullOrBlank()) {
                add(canonicalSutra("7.1.37"))
                add(canonicalSutra("6.1.71"))
            }
        }
        return pipeline.derive(initial, bootstrap)
    }

    private fun canonicalSelectionSutra(request: KrdantaDerivationRequest): DerivationSutra = canonicalSutra(when (request.samjna) {
        Samjna.KTVA -> "3.4.21"
        Samjna.TUMUN -> "3.3.158"
        Samjna.TAVYA, Samjna.ANIYAR -> "3.1.96"
        Samjna.NYAT -> "3.1.124"
        Samjna.KTA, Samjna.KTAVATU -> "1.1.26"
        Samjna.NVUL, Samjna.TRC -> "3.1.133"
        Samjna.GHAN -> "3.3.18"
        Samjna.LYUT -> "3.3.115"
        else -> error("Unsupported Kṛdanta request: ${request.samjna}")
    })

    private fun canonicalSutra(number: String) = Ashtadhyayi.requireExecutable(number)

    private fun findDhatu(dhatu: String): Dhatu = DhatuPatha.all.firstOrNull {
        it.upadesha == dhatu || it.derivationalSurface == dhatu || it.sourceSurface == dhatu
    } ?: DhatuPatha.all.first()

    private fun buildInitialState(request: KrdantaDerivationRequest, entry: Dhatu, requested: String): DerivationState {
        val terms = mutableListOf<DerivationTerm>()
        val samjnas = mutableSetOf<SamjnaAssignment>()
        request.upasarga?.takeIf(String::isNotBlank)?.let { value ->
            val term = DerivationTerm("upasarga_1", value, TermKind.PRATIPADIKA, upadesha = value)
            terms += term
            samjnas += SamjnaAssignment(term.id, Samjna.UPASARGA)
        }
        val lexicalSurface = if (DhatuPatha.all.any { it.derivationalSurface == requested || it.sourceSurface == requested }) entry.derivationalSurface else requested
        val surface = if (lexicalSurface.lastOrNull()?.let(Varnamala::isConsonant) == true) "$lexicalSurface्" else lexicalSurface
        val dhatu = DerivationTerm("dhatu_1", surface, TermKind.DHATU, upadesha = surface, itStatus = entry.itStatus)
        terms += dhatu
        samjnas += SamjnaAssignment(dhatu.id, Samjna.DHATU)
        samjnas += SamjnaAssignment(dhatu.id, request.samjna)
        return DerivationState(
            terms = terms,
            samjnas = samjnas,
            activeAdhikaras = setOf("3.1.91", "6.4.1"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
    }

    private companion object {
        val supportedGhanDhatus = setOf("युज्", "शिष्", "मूल्", "भज्", "हृ")
        val supportedApDhatus = setOf("युज्", "शिष्", "मूल्")
        val supportedLyutDhatus = setOf("युज्", "गण", "धृ", "स्था", "जन्", "हृ")
        val sourceFallbackStems = mapOf("हृ" to "हर")
        val preservedSourceStems = setOf("क्षीप्", "क्षिप्")
    }
}
