package dev.sanskrit.derivation

import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.shiksha.LexicalUse
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.dhatupatha.Dhatu

/**
 * The shared state passed through an Ashtadhyayi derivation.
 */
class DerivationState(
    val terms: List<DerivationTerm>,
    val droppedTerms: List<DerivationTerm> = emptyList(),
    val samjnas: Set<SamjnaAssignment> = emptySet(),
    val stage: DerivationStage = DerivationStage.INITIAL,
    context: DerivationalContext = DerivationalContext(),
    val activeAdhikaras: Set<String> = emptySet(),
    val inheritedAnuvrtti: Set<String> = emptySet(),
    val blockedSutras: Map<String, String> = emptyMap(),
    val varnaComparisons: Set<VarnaComparison> = emptySet(),
    val substitutions: List<VarnaSubstitution> = emptyList(),
    semanticFeatures: Set<SemanticFeature> = emptySet(),
) {
    val context: DerivationalContext
    val semanticFeatures: Set<SemanticFeature>

    init {
        require(terms.isNotEmpty()) { "A derivation requires at least one term." }

        // Resolve constructor parameters dynamically
        if (semanticFeatures.isEmpty() && context != DerivationalContext()) {
            this.context = context
            this.semanticFeatures = mapContextToFeatures(context)
        } else if (semanticFeatures.isNotEmpty() && context == DerivationalContext()) {
            this.context = mapFeaturesToContext(semanticFeatures, context)
            this.semanticFeatures = semanticFeatures
        } else if (semanticFeatures.isEmpty() && context == DerivationalContext()) {
            this.context = context
            this.semanticFeatures = emptySet()
        } else {
            // Both are specified, merge them:
            this.context = mapFeaturesToContext(semanticFeatures, context)
            this.semanticFeatures = mapContextToFeatures(this.context)
        }
    }

    val surface: String
        get() = terms.fold("") { rendered, term ->
            val next = term.surface
            if (rendered.endsWith('्') && next.firstOrNull() == 'अ') {
                rendered.dropLast(1) + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() == 'आ') {
                rendered.dropLast(1) + "ा" + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() in setOf('इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')) {
                val vowelSign = when (next.first()) {
                    'इ' -> "ि"
                    'ई' -> "ी"
                    'उ' -> "ु"
                    'ऊ' -> "ू"
                    'ऋ' -> "ृ"
                    'ॠ' -> "ॄ"
                    'ऌ' -> "ॢ"
                    'ए' -> "े"
                    'ऐ' -> "ै"
                    'ओ' -> "ो"
                    'औ' -> "ौ"
                    else -> error("Unsupported independent vowel ${next.first()}")
                }
                rendered.dropLast(1) + vowelSign + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) {
                rendered.dropLast(1) + next
            } else {
                rendered + next
            }
        }

    val allEffectiveTerms: List<DerivationTerm>
        get() = terms + droppedTerms

    val effectiveContext: DerivationalContext
        get() = context

    fun withSamjnas(additions: Set<SamjnaAssignment>): DerivationState =
        copy(samjnas = samjnas + additions)

    fun replaceTerm(id: String, replacement: DerivationTerm): DerivationState =
        copy(terms = terms.map { if (it.id == id) replacement else it })

    fun removeTerm(id: String): DerivationState {
        val term = terms.find { it.id == id } ?: return this
        return copy(
            terms = terms.filter { it.id != id },
            droppedTerms = droppedTerms + term.copy(surface = "")
        )
    }

    fun addTerm(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        return copy(terms = terms + term)
    }

    fun activateAdhikara(sutraNumber: String): DerivationState =
        copy(activeAdhikaras = activeAdhikaras + sutraNumber)

    fun carryAnuvrtti(item: String): DerivationState =
        copy(inheritedAnuvrtti = inheritedAnuvrtti + item)

    fun blockSutra(sutraNumber: String, blocker: String): DerivationState =
        copy(blockedSutras = blockedSutras + (sutraNumber to blocker))

    fun addComparison(comparison: VarnaComparison): DerivationState =
        copy(varnaComparisons = varnaComparisons + comparison)

    fun addSubstitution(substitution: VarnaSubstitution): DerivationState =
        copy(substitutions = substitutions + substitution)

    fun copy(
        terms: List<DerivationTerm> = this.terms,
        droppedTerms: List<DerivationTerm> = this.droppedTerms,
        samjnas: Set<SamjnaAssignment> = this.samjnas,
        stage: DerivationStage = this.stage,
        context: DerivationalContext = this.context,
        activeAdhikaras: Set<String> = this.activeAdhikaras,
        inheritedAnuvrtti: Set<String> = this.inheritedAnuvrtti,
        blockedSutras: Map<String, String> = this.blockedSutras,
        varnaComparisons: Set<VarnaComparison> = this.varnaComparisons,
        substitutions: List<VarnaSubstitution> = this.substitutions,
        semanticFeatures: Set<SemanticFeature> = this.semanticFeatures,
    ): DerivationState {
        // Resolve target mapping in copy:
        val finalContext = if (semanticFeatures != this.semanticFeatures) {
            mapFeaturesToContext(semanticFeatures, context)
        } else {
            context
        }
        val finalFeatures = if (context != this.context && semanticFeatures == this.semanticFeatures) {
            mapContextToFeatures(context)
        } else {
            semanticFeatures
        }
        return DerivationState(
            terms = terms,
            droppedTerms = droppedTerms,
            samjnas = samjnas,
            stage = stage,
            context = finalContext,
            activeAdhikaras = activeAdhikaras,
            inheritedAnuvrtti = inheritedAnuvrtti,
            blockedSutras = blockedSutras,
            varnaComparisons = varnaComparisons,
            substitutions = substitutions,
            semanticFeatures = finalFeatures
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DerivationState) return false
        return terms == other.terms &&
            droppedTerms == other.droppedTerms &&
            samjnas == other.samjnas &&
            stage == other.stage &&
            context == other.context &&
            activeAdhikaras == other.activeAdhikaras &&
            inheritedAnuvrtti == other.inheritedAnuvrtti &&
            blockedSutras == other.blockedSutras &&
            varnaComparisons == other.varnaComparisons &&
            substitutions == other.substitutions &&
            semanticFeatures == other.semanticFeatures
    }

    override fun hashCode(): Int {
        var result = terms.hashCode()
        result = 31 * result + droppedTerms.hashCode()
        result = 31 * result + samjnas.hashCode()
        result = 31 * result + stage.hashCode()
        result = 31 * result + context.hashCode()
        result = 31 * result + activeAdhikaras.hashCode()
        result = 31 * result + inheritedAnuvrtti.hashCode()
        result = 31 * result + blockedSutras.hashCode()
        result = 31 * result + varnaComparisons.hashCode()
        result = 31 * result + substitutions.hashCode()
        result = 31 * result + semanticFeatures.hashCode()
        return result
    }

    override fun toString(): String {
        return "DerivationState(terms=$terms, droppedTerms=$droppedTerms, samjnas=$samjnas, stage=$stage, context=$context, activeAdhikaras=$activeAdhikaras, inheritedAnuvrtti=$inheritedAnuvrtti, blockedSutras=$blockedSutras, varnaComparisons=$varnaComparisons, substitutions=$substitutions, semanticFeatures=$semanticFeatures)"
    }
}

data class VarnaComparison(
    val leftTermId: String, val rightTermId: String,
    val left: Char, val right: Char,
    val samePlaceAndEffort: Boolean,
    val leftIsVowel: Boolean, val rightIsVowel: Boolean,
    val forbidden: Boolean = false,
)

data class VarnaSubstitution(
    val targetId: String, val source: Char,
    val replacement: String, val sutra: String,
)

data class DerivationTerm(
    val id: String,
    val surface: String,
    val kind: TermKind,
    val itMarkers: Set<ItMarker> = emptySet(),
    val upadesha: String? = null,
    val deletionType: LopaType? = null,
    val sthaniProps: SthaniProperties? = null,
    val lexicalUses: Set<LexicalUse> = emptySet(),
    val itStatus: ItStatus? = null,
) {
    companion object {
        /** Preserves Dhātupāṭha metadata when a root enters a derivation. */
        fun fromDhatu(dhatu: Dhatu, id: String = "dhatu"): DerivationTerm = DerivationTerm(
            id = id,
            surface = dhatu.derivationalSurface,
            kind = TermKind.DHATU,
            upadesha = dhatu.upadesha,
            itStatus = dhatu.itStatus,
        )
    }

    fun hasEffectiveMarker(marker: ItMarker): Boolean =
        marker in itMarkers || (sthaniProps?.itMarkers?.contains(marker) == true)

    fun matchesUpadesha(value: String): Boolean =
        upadesha == value || sthaniProps?.upadesha == value
}

data class SthaniProperties(
    val upadesha: String?,
    val itMarkers: Set<ItMarker>
)

enum class LopaType { LOPA, LUK, SHLU, LUP }
enum class TermKind { DHATU, PRATIPADIKA, PRATYAYA, AGAMA, AUGMENT }
enum class ItMarker { U, J, T, P, SH, NG, KIT, NGIT, NIT, SIT }
enum class DerivationStage { INITIAL, PRATYAYA_SELECTED, IT_PROCESSED, ANGAKARYA, PADA_FORMED, FINAL }

data class SamjnaAssignment(val targetId: String, val samjna: Samjna)

private fun mapFeaturesToContext(features: Set<SemanticFeature>, baseContext: DerivationalContext): DerivationalContext {
    var ctx = clearFeaturesFromContext(baseContext)
    features.forEach { feature ->
        ctx = when (feature) {
            SemanticFeature.KARTARI -> ctx.copy(rupa = ctx.rupa.copy(prayoga = Prayoga.KARTARI))
            SemanticFeature.STRI -> ctx.copy(rupa = ctx.rupa.copy(linga = Linga.STRI))
            SemanticFeature.PRATHAMA -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.PRATHAMA))
            SemanticFeature.EKAVACANA -> ctx.copy(rupa = ctx.rupa.copy(vacana = Vacana.EKAVACANA))
            SemanticFeature.BHAVISYAT -> ctx.copy(
                requestedMeaning = DerivationalMeaning.BHAVISYAT,
                derivedMeanings = ctx.derivedMeanings + DerivationalMeaning.BHAVISYAT,
                kala = Kala.BHAVISYAT
            )
            SemanticFeature.APADANA -> ctx.copy(requestedMeaning = DerivationalMeaning.APADANA)
            SemanticFeature.UNADI_LICENSED -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.UNADI_LICENSED)
            SemanticFeature.SAMUHA -> ctx.copy(requestedMeaning = DerivationalMeaning.SAMUHA)
            SemanticFeature.VISHAYA_DESE -> ctx.copy(requestedMeaning = DerivationalMeaning.VISHAYA_DESE)
            SemanticFeature.ADHYAYANA_VEDANA -> ctx.copy(requestedMeaning = DerivationalMeaning.ADHYAYANA_VEDANA)
            SemanticFeature.NIVASA -> ctx.copy(requestedMeaning = DerivationalMeaning.NIVASA)
            SemanticFeature.CHATURARTHIKA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.CHATURARTHIKA)
            SemanticFeature.JATA -> ctx.copy(requestedMeaning = DerivationalMeaning.JATA)
            SemanticFeature.KALAVRTTI -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.KALAVRTTI)
            SemanticFeature.TATRA_BHAVA -> ctx.copy(requestedMeaning = DerivationalMeaning.TATRA_BHAVA)
            SemanticFeature.VYAKHYANA -> ctx.copy(requestedMeaning = DerivationalMeaning.VYAKHYANA)
            SemanticFeature.TATAH_AGATA -> ctx.copy(requestedMeaning = DerivationalMeaning.TATAH_AGATA)
            SemanticFeature.ABHIJANA -> ctx.copy(requestedMeaning = DerivationalMeaning.ABHIJANA)
            SemanticFeature.GOTRA -> ctx.copy(requestedMeaning = DerivationalMeaning.GOTRA)
            SemanticFeature.PRAGDIVYATIYA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.PRAGDIVYATIYA)
            SemanticFeature.SVANGA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.SVANGA)
            SemanticFeature.APATYA -> ctx.copy(requestedMeaning = DerivationalMeaning.APATYA)
            SemanticFeature.UDICYA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.UDICYA)
            SemanticFeature.GUNA_REQUEST -> ctx.copy(phonologicalRequest = PhonologicalRequest.GUNA)
            SemanticFeature.VRDDHI_REQUEST -> ctx.copy(phonologicalRequest = PhonologicalRequest.VRDDHI)
            SemanticFeature.ARDHADHATUKA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.ARDHADHATUKA)
            SemanticFeature.ASATTVA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.ASATTVA)
            SemanticFeature.KRIYAYOGA -> ctx.copy(environments = ctx.environments + DerivationalEnvironment.KRIYAYOGA)
            SemanticFeature.TADRAJA -> ctx.copy(
                requestedMeaning = DerivationalMeaning.TADRAJA,
                derivedMeanings = ctx.derivedMeanings + DerivationalMeaning.TADRAJA
            )
            SemanticFeature.BHAVE -> ctx.copy(
                requestedMeaning = DerivationalMeaning.BHAVA,
                rupa = ctx.rupa.copy(prayoga = Prayoga.BHAVE)
            )
            SemanticFeature.KARTR_VEDANA -> ctx.copy(requestedMeaning = DerivationalMeaning.KARTR_VEDANA)
            SemanticFeature.SAPTAMI -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.SAPTAMI))
            SemanticFeature.BAHUVACANA -> ctx.copy(rupa = ctx.rupa.copy(vacana = Vacana.BAHUVACANA))
            SemanticFeature.VARTAMANA -> ctx.copy(kala = Kala.VARTAMANA)
            SemanticFeature.DVITIYA -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.DVITIYA))
            SemanticFeature.TRTIYA -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.TRTIYA))
            SemanticFeature.CHATURTHI -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.CHATURTHI))
            SemanticFeature.PANCHAMI -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.PANCHAMI))
            SemanticFeature.SASTHI -> ctx.copy(rupa = ctx.rupa.copy(vibhakti = Vibhakti.SASTHI))
            SemanticFeature.DVIVACANA -> ctx.copy(rupa = ctx.rupa.copy(vacana = Vacana.DVIVACANA))
        }
    }
    return ctx
}

private fun clearFeaturesFromContext(context: DerivationalContext): DerivationalContext {
    return context.copy(
        rupa = context.rupa.copy(
            vibhakti = null,
            vacana = null,
            prayoga = null,
            linga = if (context.rupa.linga == Linga.STRI) null else context.rupa.linga
        ),
        environments = emptySet(),
        derivedMeanings = emptySet(),
        requestedMeaning = null,
        phonologicalRequest = null,
        kala = null
    )
}

private fun mapContextToFeatures(context: DerivationalContext): Set<SemanticFeature> {
    val features = mutableSetOf<SemanticFeature>()
    if (context.rupa.prayoga == Prayoga.KARTARI) features.add(SemanticFeature.KARTARI)
    if (context.rupa.linga == Linga.STRI) features.add(SemanticFeature.STRI)
    if (context.rupa.vibhakti == Vibhakti.PRATHAMA) features.add(SemanticFeature.PRATHAMA)
    if (context.rupa.vacana == Vacana.EKAVACANA) features.add(SemanticFeature.EKAVACANA)
    if (DerivationalMeaning.BHAVISYAT in context.derivedMeanings || context.kala == Kala.BHAVISYAT) features.add(SemanticFeature.BHAVISYAT)
    if (DerivationalEnvironment.UNADI_LICENSED in context.environments) features.add(SemanticFeature.UNADI_LICENSED)
    if (DerivationalEnvironment.CHATURARTHIKA in context.environments) features.add(SemanticFeature.CHATURARTHIKA)
    if (DerivationalEnvironment.KALAVRTTI in context.environments) features.add(SemanticFeature.KALAVRTTI)
    if (DerivationalEnvironment.PRAGDIVYATIYA in context.environments) features.add(SemanticFeature.PRAGDIVYATIYA)
    if (DerivationalEnvironment.SVANGA in context.environments) features.add(SemanticFeature.SVANGA)
    if (DerivationalEnvironment.UDICYA in context.environments) features.add(SemanticFeature.UDICYA)
    if (context.phonologicalRequest == PhonologicalRequest.GUNA) features.add(SemanticFeature.GUNA_REQUEST)
    if (context.phonologicalRequest == PhonologicalRequest.VRDDHI) features.add(SemanticFeature.VRDDHI_REQUEST)
    if (DerivationalEnvironment.ARDHADHATUKA in context.environments) features.add(SemanticFeature.ARDHADHATUKA)
    if (DerivationalEnvironment.ASATTVA in context.environments) features.add(SemanticFeature.ASATTVA)
    if (DerivationalEnvironment.KRIYAYOGA in context.environments) features.add(SemanticFeature.KRIYAYOGA)
    if (DerivationalMeaning.TADRAJA in context.derivedMeanings || context.requestedMeaning == DerivationalMeaning.TADRAJA) features.add(SemanticFeature.TADRAJA)
    if (context.rupa.prayoga == Prayoga.BHAVE || context.requestedMeaning == DerivationalMeaning.BHAVA) features.add(SemanticFeature.BHAVE)
    if (context.rupa.vibhakti == Vibhakti.SAPTAMI) features.add(SemanticFeature.SAPTAMI)
    if (context.rupa.vacana == Vacana.BAHUVACANA) features.add(SemanticFeature.BAHUVACANA)
    if (context.kala == Kala.VARTAMANA) features.add(SemanticFeature.VARTAMANA)
    if (context.rupa.vibhakti == Vibhakti.DVITIYA) features.add(SemanticFeature.DVITIYA)
    if (context.rupa.vibhakti == Vibhakti.TRTIYA) features.add(SemanticFeature.TRTIYA)
    if (context.rupa.vibhakti == Vibhakti.CHATURTHI) features.add(SemanticFeature.CHATURTHI)
    if (context.rupa.vibhakti == Vibhakti.PANCHAMI) features.add(SemanticFeature.PANCHAMI)
    if (context.rupa.vibhakti == Vibhakti.SASTHI) features.add(SemanticFeature.SASTHI)
    if (context.rupa.vacana == Vacana.DVIVACANA) features.add(SemanticFeature.DVIVACANA)
    return features
}
