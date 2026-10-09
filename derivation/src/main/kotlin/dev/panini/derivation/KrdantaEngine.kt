package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.core.KrtAffix
import dev.panini.core.SanadiAffix
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import dev.panini.derivation.matchesAffix
import dev.panini.shiksha.Varnamala

data class KrdantaDerivationRequest(
    val dhatu: String,
    val samjna: Samjna,
    val upasarga: String? = null,
    val sanadiPratyayas: List<String> = emptyList(),
)

sealed interface KrdantaSourceStem {
    val surface: String

    data class Productive(
        override val surface: String,
        val supportsAStemDeclension: Boolean,
        val derivation: DerivationResult,
    ) : KrdantaSourceStem

    data class Unresolved(
        override val surface: String,
        val reason: Reason,
    ) : KrdantaSourceStem {
        enum class Reason { UNKNOWN_DHATU, UNKNOWN_KRT_AFFIX, INVALID_AFFIX_CONTEXT, INCOMPLETE_DERIVATION }
    }
}

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
        upasargas: List<String> = emptyList(),
    ): KrdantaSourceStem {
        // ल्यप् is the contextual replacement of क्त्वा, not an independently
        // selected suffix. Use the existing 3.4.21 -> 7.1.37 derivation path.
        if (KrtAffix.fromUpadesha(pratyaya) == KrtAffix.LYAP && upasargas.isEmpty()) {
            return KrdantaSourceStem.Unresolved(dhatu, KrdantaSourceStem.Unresolved.Reason.INVALID_AFFIX_CONTEXT)
        }
        val samjna = sourceAffixSamjna(pratyaya)
        val hasDhatu = DhatuPatha.all.any { it.matchesSurface(dhatu) }
        if (samjna == null) return KrdantaSourceStem.Unresolved(dhatu, KrdantaSourceStem.Unresolved.Reason.UNKNOWN_KRT_AFFIX)
        if (!hasDhatu) return KrdantaSourceStem.Unresolved(dhatu, KrdantaSourceStem.Unresolved.Reason.UNKNOWN_DHATU)

        val result = derive(
            KrdantaDerivationRequest(
                dhatu = dhatu,
                samjna = samjna,
                upasarga = upasargas.joinToString("").ifBlank { null },
                sanadiPratyayas = sanadiPratyayas,
            ),
        )
        if (result.final.terms.any { it.matchesAffix(KrtAffix.LYAP) } &&
            result.final.terms.any { it.matchesAffix(SanadiAffix.NIC) }
        ) {
            // The remaining णिच् requires an additional context-sensitive rule
            // (notably 6.4.56). Do not advertise mechanical sandhi as a form.
            return KrdantaSourceStem.Unresolved(dhatu, KrdantaSourceStem.Unresolved.Reason.INCOMPLETE_DERIVATION)
        }
        return KrdantaSourceStem.Productive(
            surface = result.final.surface,
            supportsAStemDeclension = samjna in setOf(Samjna.KTA, Samjna.GHAN, Samjna.LYUT),
            derivation = result,
        )
    }

    private fun sourceAffixSamjna(pratyaya: String): Samjna? = when (KrtAffix.fromUpadesha(pratyaya)) {
        KrtAffix.KTA -> Samjna.KTA
        KrtAffix.KTAVATU -> Samjna.KTAVATU
        KrtAffix.KTVA, KrtAffix.LYAP -> Samjna.KTVA
        KrtAffix.TUMUN -> Samjna.TUMUN
        KrtAffix.TAVYAT -> Samjna.TAVYA
        KrtAffix.ANIYAR -> Samjna.ANIYAR
        KrtAffix.NYAT -> Samjna.NYAT
        KrtAffix.NVUL -> Samjna.NVUL
        KrtAffix.TRC -> Samjna.TRC
        KrtAffix.GHAN -> Samjna.GHAN
        KrtAffix.LYUT -> Samjna.LYUT
        else -> null
    }

    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        require(request.sanadiPratyayas.all { SanadiAffix.fromUpadesha(it) == SanadiAffix.NIC }) {
            "Unsupported sanādi pratyaya in kṛdanta derivation: ${request.sanadiPratyayas.joinToString()}"
        }
        require(request.sanadiPratyayas.distinct().size == request.sanadiPratyayas.size) {
            "A sanādi affix may be introduced only once in one kṛdanta request."
        }
        val entry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, entry)
        val selection = canonicalSelectionSutra(request)
        require(selection.matches(initial)) {
            "Canonical sutra ${selection.sutra} cannot select ${request.samjna} for ${request.dhatu}."
        }
        val bootstrap = buildList {
            if (request.sanadiPratyayas.any { SanadiAffix.fromUpadesha(it) == SanadiAffix.NIC }) add(canonicalSutra("3.1.26"))
            add(selection)
            if (request.samjna == Samjna.KTA || request.samjna == Samjna.KTAVATU) {
                add(canonicalSutra("1.1.26"))
            }
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
        Samjna.KTA, Samjna.KTAVATU -> "3.2.102"
        Samjna.NVUL, Samjna.TRC -> "3.1.133"
        Samjna.GHAN -> "3.3.18"
        Samjna.LYUT -> "3.3.115"
        else -> error("Unsupported Kṛdanta request: ${request.samjna}")
    })

    private fun canonicalSutra(number: String) = Ashtadhyayi.requireExecutable(number)

    private fun findDhatu(dhatu: String): Dhatu {
        val candidates = DhatuPatha.all.filter { it.matchesSurface(dhatu) }
        return candidates.firstOrNull { it.preferredForSourceDerivation }
            ?: candidates.firstOrNull { it.derivationalSurface.removeSuffix("्") == dhatu.removeSuffix("्") }
            ?: candidates.firstOrNull()
            ?: throw IllegalArgumentException("Unknown dhātu in kṛdanta derivation: $dhatu")
    }

    private fun Dhatu.matchesSurface(value: String): Boolean {
        val normalized = value.removeSuffix("्")
        return sequenceOf(upadesha, derivationalSurface, sourceSurface).any {
            it == value || it.removeSuffix("्") == normalized
        }
    }

    private fun buildInitialState(request: KrdantaDerivationRequest, entry: Dhatu): DerivationState {
        val terms = mutableListOf<DerivationTerm>()
        val samjnas = mutableSetOf<SamjnaAssignment>()
        request.upasarga?.takeIf(String::isNotBlank)?.let { value ->
            val term = DerivationTerm("upasarga_1", value, TermKind.PRATIPADIKA, upadesha = value)
            terms += term
            samjnas += SamjnaAssignment(term.id, Samjna.UPASARGA)
        }
        // The selected lexicon entry supplies the marker-free derivational root;
        // source upadeśas such as चिञ् must not retain their it letters here.
        val lexicalSurface = entry.derivationalSurface
        val surface = if (lexicalSurface.lastOrNull()?.let(Varnamala::isConsonant) == true) "$lexicalSurface्" else lexicalSurface
        val dhatu = DerivationTerm("dhatu_1", surface, TermKind.DHATU, upadesha = surface, itStatus = entry.itStatus)
        terms += dhatu
        samjnas += SamjnaAssignment(dhatu.id, Samjna.DHATU)
        samjnas += SamjnaAssignment(dhatu.id, request.samjna)
        if (request.sanadiPratyayas.any { SanadiAffix.fromUpadesha(it) == SanadiAffix.NIC }) {
            samjnas += SamjnaAssignment(dhatu.id, Samjna.NIC)
        }
        return DerivationState(
            terms = terms,
            samjnas = samjnas,
            activeAdhikaras = setOf("3.1.91", "6.4.1"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
    }
}
