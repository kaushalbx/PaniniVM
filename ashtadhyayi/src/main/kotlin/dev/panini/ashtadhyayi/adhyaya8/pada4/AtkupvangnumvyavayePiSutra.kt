package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.DhatuGana
import dev.panini.core.TingAffix
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.2: aṭ-kup-vāṅ-num-vyavāye'pi.
 * Retroflexion of 'n' to 'ṇ' happens even if sounds of Aṭ, Ku (ka-varga),
 * Pu (pa-varga), Āṅ, or Num intervene between the trigger (r/ṣ) and the target.
 */
object AtkupvangnumvyavayePiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.2",
    text = "अट्कुप्वाङ्नुम्व्यवायेऽपि",
    hindiExplanation = "र् या ष् के बाद न् का ण् होता है, यदि बीच में अट्, क-वर्ग, प-वर्ग, आङ् या नुम् का व्यवधान हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840002,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.SANDHI,
    dependencies = setOf("8.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (HasMorphosyntax(vibhakti = Vibhakti.DVITIYA, vacana = Vacana.BAHUVACANA).matches(context)) return false
        val target = findTarget(context) ?: return false
        val positions = phonologicalPositions(context)

        // 8.4.37: padāntasya blocks retroflexion at the end of a word (ending in 'न्')
        if (target.targetPosition == positions.lastIndex) return false

        // 8.4.39: kṣubhnādiṣu ca blocks ṇatva for kṣubhnādi gaṇa words (e.g. bhuvana, kṣubdha)
        val allVarnas = positions.map { it.varna }
        if (allVarnas.containsSubsequence(bhuvana) || allVarnas.containsSubsequence(kshubdha)) return false

        // 8.4.35: No retroflexion if 'n' is followed by a dental consonant (t-varga: t, th, d, dh, n)
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.gana == DhatuGana.RUDHADI }
        val isStrongRudhadiShnam = target.term.id == dhatu?.id &&
            target.term.varnas.getOrNull(target.varnaIndex + 1) is Svara &&
            context.droppedTerms.any { it.upadesha == "श्नम्" }
        val isKryadiShnaNasal = context.terms.any { it.id == "shna" && Vyanjana.NA in it.varnas }

        // Guard: do not retroflex a nasal that lives inside a tiṅ affix term that is not
        // part of a known vikaraṇa nasal (Rudhādi श्नम् infix or Kryādi श्ना).
        // Without this, the 'न' in endings like आनि (LOT MIP) would be mistakenly
        // retroflexed when the root happens to contain र/ष (e.g. Curādi चोर-).
        // Vibhakti endings (रामाणाम्, ऋषिणा) are also PRATYAYA but are NOT tiṅ affixes,
        // so we specifically exclude only the tiṅ-affix family by upadeśa membership.
        if (!isStrongRudhadiShnam && !isKryadiShnaNasal) {
            val tingUpadeshas = TingAffix.entries.mapTo(mutableSetOf()) { it.upadesha }
            if (target.term.kind == TermKind.PRATYAYA && target.term.upadesha in tingUpadeshas) return false
        }

        positions.getOrNull(target.targetPosition + 1)?.varna?.let { next ->
            if (!isStrongRudhadiShnam && !isKryadiShnaNasal && next in dentalVarga) return false
        }

        return positions.subList(target.triggerPosition + 1, target.targetPosition).all { isAllowed(it.varna) }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = findTarget(context) ?: return DerivationChange(context, "8.4.2: Target 'n' not found in terms.")
        val result = target.term.varnas.toMutableList().also { it[target.varnaIndex] = Vyanjana.NNA }

        return DerivationChange(
            state = context.substituteTermVarnas(target.term.id, result, Vyanjana.NA, listOf(Vyanjana.NNA), sutra)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.2: Retroflexed 'n' to 'ṇ' with allowed intervenors."
        )
    }

    private fun isAllowed(varna: Varna): Boolean {
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.AC, varna) ||
            varna in atRemainder || varna in ku || varna in pu || varna == Ayogavaha.ANUSVARA
    }

    private fun findTarget(context: DerivationState): Target? {
        val positions = phonologicalPositions(context)
        val shna = context.terms.firstOrNull { it.id == "shna" }
        if (shna != null) {
            if (Vyanjana.NNA in shna.varnas) return null
            val targetPosition = positions.indexOfFirst { it.term.id == shna.id && it.varna == Vyanjana.NA }
            if (targetPosition < 0) return null
            val triggerPosition = (targetPosition - 1 downTo 0).firstOrNull { positions[it].varna in triggers } ?: return null
            val owned = positions[targetPosition]
            return Target(owned.term, owned.varnaIndex, triggerPosition, targetPosition)
        }
        val triggerPosition = positions.indexOfLast { it.varna in triggers }
        if (triggerPosition < 0) return null
        val targetPosition = (triggerPosition + 1..positions.lastIndex).firstOrNull { positions[it].varna == Vyanjana.NA } ?: return null
        val triggerOwned = positions[triggerPosition]
        val targetOwned = positions[targetPosition]

        // Guard: if the r/ṣ trigger is inside a DHATU term but the target न is in a
        // *different* (non-DHATU / suffix) term, the dhātu's internal r/ṣ cannot serve
        // as a cross-term Ṇatva trigger.  This prevents spurious matching in Curādi
        // forms like चोर-य-आनि where र is buried in the stem and न is a suffix sound.
        // Note: when both trigger and target are in the same DHATU (e.g. Rudhadi's
        // शनम्-infixed forms like रुनध्), the rule should still fire.
        if (triggerOwned.term.id != targetOwned.term.id) {
            val isKrdantaNimitta = context.samjnas.any {
                it.targetId == triggerOwned.term.id &&
                    it.samjna in setOf(dev.panini.shiksha.Samjna.ANIYAR, dev.panini.shiksha.Samjna.LYUT)
            }
            if (triggerOwned.term.kind == TermKind.DHATU && !isKrdantaNimitta) return null
        }
        return Target(targetOwned.term, targetOwned.varnaIndex, triggerPosition, targetPosition)
    }

    private fun phonologicalPositions(context: DerivationState): List<OwnedVarna> = context.terms.flatMap { term ->
        term.varnas.mapIndexed { index, varna -> OwnedVarna(term, index, varna) }
    }

    private fun List<Varna>.containsSubsequence(needle: List<Varna>): Boolean =
        needle.isNotEmpty() && windowed(needle.size).any { it == needle }

    private val triggers = setOf(Vyanjana.RA, Vyanjana.SSA, Svara.R, Svara.RR)
    private val atRemainder = setOf(Vyanjana.HA, Vyanjana.YA, Vyanjana.VA, Vyanjana.RA)
    private val ku = setOf(Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA)
    private val pu = setOf(Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA, Vyanjana.MA)
    private val dentalVarga = setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)
    private val bhuvana: List<Varna> = listOf(Vyanjana.BHA, Svara.U, Vyanjana.VA, Svara.A, Vyanjana.NA, Svara.A)
    private val kshubdha: List<Varna> = listOf(Vyanjana.KA, Vyanjana.SSA, Svara.U, Vyanjana.BA, Vyanjana.DHA, Svara.A)
    private data class OwnedVarna(val term: dev.panini.derivation.DerivationTerm, val varnaIndex: Int, val varna: Varna)
    private data class Target(
        val term: dev.panini.derivation.DerivationTerm,
        val varnaIndex: Int,
        val triggerPosition: Int,
        val targetPosition: Int,
    )
}
