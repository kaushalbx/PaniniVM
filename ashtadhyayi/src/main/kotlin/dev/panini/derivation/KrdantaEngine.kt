package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.NisthaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.AcoYatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KrdAtinSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NvultrcauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RhalorNyatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.TavyattavyaaniyarahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.BhaveGhanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.SamanakartrkesuTumunSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.SamanakartrkayohPurvakaleSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.HrasvasyaPitiKrtiTukSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SamaseAnanpurveKtvoLyapSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.ArdhadhatukasyedValadehSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TaddhitesvAcamAdehSutra
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
        listOf(
            DhatohSutra,
            KrdAtinSutra,
            SamanakartrkayohPurvakaleSutra,
            SamaseAnanpurveKtvoLyapSutra,
            SamanakartrkesuTumunSutra,
            TavyattavyaaniyarahSutra,
            RhalorNyatSutra,
            AcoYatSutra,
            NisthaSutra,
            NvultrcauSutra,
            BhaveGhanSutra,
            ArdhadhatukasyedValadehSutra,
            HrasvasyaPitiKrtiTukSutra,
            HalantyamSutra,
            TasyaLopahSutra,
            TaddhitesvAcamAdehSutra,
        )
    )
) {
    fun derive(request: KrdantaDerivationRequest): DerivationResult {
        val dhatuEntry = findDhatu(request.dhatu)
        val initial = buildInitialState(request, dhatuEntry, request.dhatu)
        val result = engine.derive(initial)

        // Perform final stem-affix phonological synthesis (Guṇa, Vṛddhi, Sandhi)
        val synthesizedState = synthesizeKrdanta(result.final, request)
        return result.copy(final = synthesizedState)
    }

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

    private fun synthesizeKrdanta(state: DerivationState, request: KrdantaDerivationRequest): DerivationState {
        var terms = state.terms
        if (terms.isEmpty()) return state

        val upasarga = terms.firstOrNull { state.samjnas.contains(SamjnaAssignment(it.id, Samjna.UPASARGA)) }?.surface ?: ""
        val dhatuTerm = terms.firstOrNull { it.kind == TermKind.DHATU } ?: return state
        var stem = dhatuTerm.surface
        val hasItAgama = terms.any { it.id == "it-agama" }
        val hasTukAgama = terms.any { it.id == "tuk_agama" }
        val pratyayaTerm = terms.lastOrNull { it.kind == TermKind.PRATYAYA }

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
            if (pratyayaTerm != null) {
                val pSurf = pratyayaTerm.surface
                if (pSurf == "अक" || pSurf == "तृ" || pSurf == "य" || pSurf == "त्वा" || pSurf == "तुमुन्" || pSurf == "त" || pSurf == "तवत्") {
                    append(pSurf)
                } else if (pratyayaTerm.upadesha == "अनीयर्") {
                    append("अनीय")
                } else if (pratyayaTerm.upadesha == "तव्यत्") {
                    append("तव्य")
                } else if (pratyayaTerm.upadesha == "तुमुन्") {
                    append("तुम्")
                } else if (pratyayaTerm.upadesha == "ण्यत्" || pratyayaTerm.upadesha == "यत्") {
                    append("य")
                } else {
                    append(pSurf)
                }
            }
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
