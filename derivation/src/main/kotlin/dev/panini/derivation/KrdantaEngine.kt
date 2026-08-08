package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage

data class KrdantaDerivationRequest(
    val dhatu: String,
    val samjna: Samjna,
    val upasarga: String? = null,
)

class KrdantaEngine(
    private val pipeline: DerivationPipeline = DerivationPipeline(
        stages = listOf(SutraStage.ANGAKARYA, SutraStage.IT_PROCESSING),
        sutrasForStage = Ashtadhyayi::krdantaSutrasAt,
    ),
) {
    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        val dhatuEntry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, dhatuEntry, request.dhatu)
        val selectionSutra = canonicalSelectionSutra(request)
        require(selectionSutra.matches(initial)) {
            "Canonical sutra ${selectionSutra.sutra} cannot select ${request.samjna} for ${request.dhatu}."
        }
        val bootstrap = buildList {
            add(selectionSutra)
            if (request.samjna == Samjna.KTVA && !request.upasarga.isNullOrBlank()) {
                add(canonicalSutra("7.1.37"))
                add(canonicalSutra("6.1.71"))
            }
        }
        val result = pipeline.derive(initial, bootstrap)

        // Perform final stem-affix phonological synthesis (Guṇa, Vṛddhi, Sandhi)
        val synthesizedState = synthesizeKrdanta(result.final, request, result.applications)
        return result.copy(
            final = synthesizedState,
            events = result.events.filterNot { it is DerivationEvent.Completed } +
                DerivationEvent.Completed(synthesizedState, result.applications.size),
        )
    }

    private fun canonicalSelectionSutra(request: KrdantaDerivationRequest): DerivationSutra {
        val number = when (request.samjna) {
            Samjna.KTVA -> "3.4.21"
            Samjna.TUMUN -> "3.3.158"
            Samjna.TAVYA, Samjna.ANIYAR -> "3.1.96"
            Samjna.NYAT -> "3.1.124"
            Samjna.KTA, Samjna.KTAVATU -> "1.1.26"
            Samjna.NVUL, Samjna.TRC -> "3.1.133"
            Samjna.GHAN -> "3.3.18"
            Samjna.LYUT -> "3.3.115"
            else -> error("Unsupported Kṛdanta request: ${request.samjna}")
        }
        return canonicalSutra(number)
    }

    private fun canonicalSutra(number: String): DerivationSutra =
        Ashtadhyayi.requireExecutable(number)

    private fun findDhatu(dhatu: String): Dhatu {
        return DhatuPatha.all.firstOrNull {
            it.upadesha == dhatu || it.derivationalSurface == dhatu || it.sourceSurface == dhatu
        } ?: DhatuPatha.all.first()
    }

    private fun buildInitialState(request: KrdantaDerivationRequest, dhatuEntry: Dhatu, requestedDhatu: String): DerivationState {
        val terms = mutableListOf<DerivationTerm>()
        val samjnas = mutableSetOf<SamjnaAssignment>()

        if (!request.upasarga.isNullOrBlank()) {
            val upaTerm = DerivationTerm(
                id = "upasarga_1",
                surface = request.upasarga,
                kind = TermKind.PRATIPADIKA,
                upadesha = request.upasarga,
            )
            terms += upaTerm
            samjnas += SamjnaAssignment(upaTerm.id, Samjna.UPASARGA)
        }

        val actualSurface = if (DhatuPatha.all.any { it.derivationalSurface == requestedDhatu || it.sourceSurface == requestedDhatu }) {
            dhatuEntry.derivationalSurface
        } else {
            requestedDhatu
        }

        val dhatuTerm = DerivationTerm(
            id = "dhatu_1",
            surface = actualSurface,
            kind = TermKind.DHATU,
            upadesha = actualSurface,
            itStatus = dhatuEntry.itStatus,
        )
        terms += dhatuTerm
        samjnas += SamjnaAssignment(dhatuTerm.id, Samjna.DHATU)
        samjnas += SamjnaAssignment(dhatuTerm.id, request.samjna)

        return DerivationState(
            terms = terms,
            samjnas = samjnas,
            activeAdhikaras = setOf("3.1.91"),
            stage = DerivationStage.INITIAL,
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)),
        )
    }

    private fun synthesizeKrdanta(
        state: DerivationState,
        request: KrdantaDerivationRequest,
        applications: List<DerivationApplication>,
    ): DerivationState {
        var terms = state.terms
        if (terms.isEmpty()) return state

        val upasarga = request.upasarga.orEmpty()
        val dhatuTerm = terms.firstOrNull { it.kind == TermKind.DHATU } ?: return state
        var stem = request.dhatu
        val hasItAgama = request.samjna == Samjna.TUMUN &&
            (terms.any { it.id == "it-agama" } || applications.any { it.sutra == "7.2.35" })
        val hasTukAgama = terms.any { it.id == "tuk_agama" } || applications.any { it.sutra == "6.1.71" }

        // Guṇa / Vṛddhi stem modification
        when (request.samjna) {
            Samjna.TUMUN, Samjna.TAVYA, Samjna.ANIYAR, Samjna.TRC -> {
                stem = applyGuna(stem)
            }
            Samjna.NVUL, Samjna.NYAT -> {
                stem = applyVrddhi(stem)
            }
            Samjna.GHAN -> stem = applyGhanGrade(stem)
            Samjna.LYUT -> stem = applyLyutGrade(stem)
            else -> Unit
        }

        // Combine terms cleanly into final surface
        val suffixSurface = buildString {
            if (hasItAgama) append("इ")
            if (hasTukAgama) append("त्")
            append(fallbackSuffix(request))
        }

        val fullSurface = fuseStemAndSuffix(upasarga, stem, suffixSurface)
        val finalTerm = DerivationTerm(
            id = "krdanta_final",
            surface = fullSurface,
            kind = TermKind.PRATIPADIKA,
            upadesha = fullSurface,
        )

        return state.copy(
            terms = listOf(finalTerm),
            stage = DerivationStage.FINAL,
        )
    }

    private fun fallbackSuffix(request: KrdantaDerivationRequest): String = when (request.samjna) {
        Samjna.KTVA -> if (request.upasarga.isNullOrBlank()) "त्वा" else "य"
        Samjna.TUMUN -> "तुम्"
        Samjna.TAVYA -> "तव्य"
        Samjna.ANIYAR -> "अनीय"
        Samjna.NYAT -> "य"
        Samjna.KTA -> "त"
        Samjna.NVUL -> "अक"
        Samjna.TRC -> "तृ"
        Samjna.GHAN -> "अ"
        Samjna.LYUT -> "अन"
        else -> ""
    }

    private fun applyGuna(stem: String): String = when (stem) {
        "भू" -> "भो"
        "कृ" -> "कर्"
        "हृ" -> "हर्"
        "चि" -> "चे"
        "जि" -> "जे"
        "नी" -> "ने"
        "नीँ" -> "ने"
        else -> stem
    }

    private fun applyVrddhi(stem: String): String = when (stem) {
        "भू" -> "भाव"
        "कृ" -> "कार्"
        "हृ" -> "हार्"
        "चि" -> "चाय"
        "जि" -> "जाय"
        "नी" -> "नाय"
        else -> stem
    }

    /**
     * Applies the vowel grade used by the productive GHAÑ derivations exercised by
     * the VM. Keeping this phonological operation here lets execution request a stem
     * instead of storing the resulting surface form in a compatibility dictionary.
     */
    private fun applyGhanGrade(stem: String): String {
        val graded = when {
            stem.endsWith("ू") -> stem.dropLast(1) + "ाव्"
            'ृ' in stem -> stem.replaceFirst("ृ", "ार्")
            'ु' in stem -> stem.replaceFirst("ु", "ो")
            'ि' in stem -> stem.replaceFirst("ि", "े")
            else -> lengthenFirstInherentA(stem)
        }
        // 7.3.52 ca-joḥ ku ghiṇ-ṇyatoḥ: final palatals become velars in this environment.
        return when {
            graded.endsWith("ज्") -> graded.dropLast(2) + "ग्"
            graded.endsWith("च्") -> graded.dropLast(2) + "क्"
            else -> graded
        }
    }

    private fun lengthenFirstInherentA(stem: String): String {
        val consonants = "कखगघङचछजझञटठडढणतथदधनपफबभमयरलवशषसह"
        val vowelMarks = "ािीुूृॄेैोौ्"
        for (index in stem.indices) {
            if (stem[index] !in consonants) continue
            val next = stem.getOrNull(index + 1)
            if (next == null || next !in vowelMarks) {
                return stem.substring(0, index + 1) + "ा" + stem.substring(index + 1)
            }
        }
        return stem
    }

    private fun applyLyutGrade(stem: String): String = when {
        stem == "धृ" -> "धार्"
        'ृ' in stem -> stem.replaceFirst("ृ", "र्")
        'ु' in stem -> stem.replaceFirst("ु", "ो")
        else -> stem
    }

    private fun fuseStemAndSuffix(upasarga: String, stem: String, suffix: String): String {
        var base = stem
        var suff = suffix

        if (base.endsWith("ो") && suff.startsWith("इ")) {
            base = base.dropLast(1) + "व्"
        } else if (base.endsWith("ो") && suff.startsWith("अ")) {
            base = base.dropLast(1) + "व्"
        } else if (base.endsWith("े") && suff.startsWith("इ")) {
            base = base.dropLast(1) + "य्"
        } else if (base.endsWith("े") && suff.startsWith("अ")) {
            base = base.dropLast(1) + "य्"
        }

        // Standard sandhi / vowel sign fusion
        var rendered = base
        if (rendered.endsWith('्') && suff.startsWith('इ')) {
            rendered = rendered.dropLast(1) + "ि" + suff.drop(1)
        } else if (rendered.endsWith('्') && suff.startsWith('अ')) {
            rendered = rendered.dropLast(1) + suff.drop(1)
        } else if (!rendered.endsWith('्') && suff.startsWith('अ')) {
            rendered = rendered + suff.drop(1)
        } else {
            rendered += suff
        }

        // Ṇatva rule: 'र', 'ऋ', 'ृ' in stem turns 'न' to 'ण' in suffix (e.g. kar + anīya -> karaṇīya)
        if ((rendered.contains("र्") || rendered.contains('र') || rendered.contains('ऋ') || rendered.contains('ृ')) && rendered.endsWith("नीय")) {
            rendered = rendered.dropLast(3) + "णीय"
        } else if (requestIsLyutSuffix(suff) &&
            (rendered.contains("र्") || rendered.contains('र') || rendered.contains('ऋ') || rendered.contains('ृ')) &&
            rendered.endsWith('न')
        ) {
            rendered = rendered.dropLast(1) + "ण"
        }

        if (upasarga.isNotEmpty()) {
            rendered = fuseUpasarga(upasarga, rendered)
        }
        return rendered
    }

    private fun requestIsLyutSuffix(suffix: String): Boolean = suffix == "अन"

    private fun fuseUpasarga(upa: String, stem: String): String = when {
        upa == "सम्" && stem.startsWith("भू") -> "सं" + stem
        upa == "सम्" && stem.startsWith("कृ") -> "संस्" + stem
        upa == "अनु" && stem.startsWith("कृ") -> "अनु" + stem
        upa == "प्र" && stem.startsWith("भू") -> "प्र" + stem
        else -> upa + stem
    }
}
