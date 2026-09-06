package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import dev.panini.shiksha.Varnamala

data class KrdantaDerivationRequest(
    val dhatu: String,
    val samjna: Samjna,
    val upasarga: String? = null,
    val sanadiPratyayas: List<String> = emptyList(),
)

data class KrdantaSourceStem(val surface: String, val supportsAStemDeclension: Boolean, val preservesSourceSurface: Boolean)

class KrdantaEngine(
    private val pipeline: DerivationPipeline = DerivationPipeline(
        stages = listOf(SutraStage.IT_PROCESSING, SutraStage.ANGAKARYA, SutraStage.VOWEL_SANDHI, SutraStage.SANDHI),
        finalizeState = { it.copy(stage = DerivationStage.FINAL) },
        sutrasForStage = Ashtadhyayi::krdantaSutrasAt,
        interleaveItProcessingAt = setOf(SutraStage.SANDHI),
    ),
) {
    fun deriveSourceStem(
        dhatu: String,
        pratyaya: String,
        sanadiPratyayas: List<String> = emptyList(),
    ): KrdantaSourceStem {
        val samjna = sourceAffixSamjna(pratyaya)
        val hasDhatu = DhatuPatha.all.any { it.matchesSurface(dhatu) }
        if (samjna == null || !hasDhatu) return KrdantaSourceStem(dhatu, false, true)

        return KrdantaSourceStem(
            surface = derive(KrdantaDerivationRequest(dhatu, samjna, sanadiPratyayas = sanadiPratyayas)).final.surface,
            supportsAStemDeclension = samjna in setOf(Samjna.GHAN, Samjna.LYUT),
            preservesSourceSurface = false,
        )
    }

    private fun sourceAffixSamjna(pratyaya: String): Samjna? = when (pratyaya) {
        "क्त" -> Samjna.KTA
        "क्तवतुँ" -> Samjna.KTAVATU
        "क्त्वा" -> Samjna.KTVA
        "तुमुँन्" -> Samjna.TUMUN
        "तव्यत्" -> Samjna.TAVYA
        "अनीयर्", "अनीयर" -> Samjna.ANIYAR
        "ण्यत्" -> Samjna.NYAT
        "ण्वुल्" -> Samjna.NVUL
        "तृच्" -> Samjna.TRC
        "घञ्" -> Samjna.GHAN
        "ल्युट्", "अन" -> Samjna.LYUT
        else -> null
    }

    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        require(request.sanadiPratyayas.all { it == "णिच्" }) {
            "Unsupported sanādi pratyaya in kṛdanta derivation: ${request.sanadiPratyayas.joinToString()}"
        }
        require(request.sanadiPratyayas.distinct().size == request.sanadiPratyayas.size) {
            "A sanādi affix may be introduced only once in one kṛdanta request."
        }
        val entry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, entry, request.dhatu)
        val selection = canonicalSelectionSutra(request)
        require(selection.matches(initial)) {
            "Canonical sutra ${selection.sutra} cannot select ${request.samjna} for ${request.dhatu}."
        }
        val bootstrap = buildList {
            if ("णिच्" in request.sanadiPratyayas) add(canonicalSutra("3.1.26"))
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

    private fun findDhatu(dhatu: String): Dhatu = DhatuPatha.all.firstOrNull { it.matchesSurface(dhatu) }
        ?: DhatuPatha.all.first()

    private fun Dhatu.matchesSurface(value: String): Boolean {
        val normalized = value.removeSuffix("्")
        return sequenceOf(upadesha, derivationalSurface, sourceSurface).any {
            it == value || it.removeSuffix("्") == normalized
        }
    }

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
        if ("णिच्" in request.sanadiPratyayas) samjnas += SamjnaAssignment(dhatu.id, Samjna.NIC)
        return DerivationState(
            terms = terms,
            samjnas = samjnas,
            activeAdhikaras = setOf("3.1.91", "6.4.1"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
    }
}
