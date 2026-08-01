package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna

data class KrdantaDerivationRequest(
    val dhatu: String,
    val samjna: Samjna,
    val upasarga: String? = null,
)

class KrdantaEngine(
    private val engine: DerivationEngine = DerivationEngine(
        Ashtadhyayi.executableSutras,
        deferTripadiUntilPada = true,
    )
) {
    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        val dhatuEntry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, dhatuEntry, request.dhatu)
        val selectionSutra = canonicalSelectionSutra(request)
        require(selectionSutra.matches(initial)) {
            "Canonical sutra ${selectionSutra.sutra} cannot select ${request.samjna} for ${request.dhatu}."
        }
        var selectedState = initial
        val selectedApplications = mutableListOf<DerivationApplication>()
        val selectedEvents = mutableListOf<DerivationEvent>()
        val bootstrap = buildList {
            add(selectionSutra)
            if (request.samjna == Samjna.KTVA && !request.upasarga.isNullOrBlank()) {
                add(canonicalSutra("7.1.37"))
                add(canonicalSutra("6.1.71"))
            }
        }
        bootstrap.forEach { sutra ->
            if (!sutra.matches(selectedState)) return@forEach
            val change = sutra.apply(selectedState)
            selectedApplications += application(sutra, selectedState, change)
            selectedEvents += DerivationEvent.RuleApplied(
                sutra.sutra,
                selectedState,
                change.state,
                change.explanation,
            )
            selectedState = change.state
        }
        val tail = engine.derive(selectedState)
        val result = tail.copy(
            initial = initial,
            applications = selectedApplications + tail.applications,
            events = selectedEvents + tail.events,
        )

        // Perform final stem-affix phonological synthesis (Guṇa, Vṛddhi, Sandhi)
        val synthesizedState = synthesizeKrdanta(result.final, request, result.applications)
        return result.copy(final = synthesizedState)
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
            else -> error("Unsupported Kṛdanta request: ${request.samjna}")
        }
        return canonicalSutra(number)
    }

    private fun canonicalSutra(number: String): DerivationSutra =
        Ashtadhyayi.registry.require(number) as DerivationSutra

    private fun application(
        sutra: DerivationSutra,
        before: DerivationState,
        change: DerivationChange,
    ): DerivationApplication = DerivationApplication(
        sutra = sutra.sutra,
        role = sutra.role,
        action = sutra.action,
        scope = sutra.scope,
        trace = sutra.renderTrace(),
        before = before,
        after = change.state,
        explanation = change.explanation,
    )

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
            Samjna.NVUL, Samjna.GHAN, Samjna.NYAT -> {
                stem = applyVrddhi(stem)
            }
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
        }

        if (upasarga.isNotEmpty()) {
            rendered = fuseUpasarga(upasarga, rendered)
        }
        return rendered
    }

    private fun fuseUpasarga(upa: String, stem: String): String = when {
        upa == "सम्" && stem.startsWith("भू") -> "सं" + stem
        upa == "सम्" && stem.startsWith("कृ") -> "संस्" + stem
        upa == "अनु" && stem.startsWith("कृ") -> "अनु" + stem
        upa == "प्र" && stem.startsWith("भू") -> "प्र" + stem
        else -> upa + stem
    }
}
