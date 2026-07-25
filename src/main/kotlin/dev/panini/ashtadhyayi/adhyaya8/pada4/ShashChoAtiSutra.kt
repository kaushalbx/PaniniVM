package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.63: śaś cho'ṭi.
 * After a jhay consonant, 'ś' is optionally replaced by 'ch' when followed by an 'aṭ' sound (vowels, y, v, r, h).
 */
object ShashChoAtiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.63",
    text = "शश्छोऽटि",
    hindiExplanation = "झय् से उत्तर श-कार के स्थान पर अट् परे रहते विकल्प से छ-कार आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840063,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || (!next.startsWith("श") && !next.startsWith("श्"))) return@any false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@any false
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, lastChar)) return@any false

            val rawFollower = next.dropWhile { it == 'श' || it == '्' }.firstOrNull() ?: return@any false
            val follower = if (Svara.fromMatra(rawFollower) != null) normalizeVowelMark(rawFollower) else rawFollower

            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, follower) || Varnamala.isVowel(follower)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || (!next.startsWith("श") && !next.startsWith("श्"))) return@first false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@first false
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, lastChar)) return@first false

            val rawFollower = next.dropWhile { it == 'श' || it == '्' }.firstOrNull() ?: return@first false
            val follower = if (Svara.fromMatra(rawFollower) != null) normalizeVowelMark(rawFollower) else rawFollower

            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, follower) || Varnamala.isVowel(follower)
        } + 1

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = if (surface.startsWith("श्")) {
            "छ्" + surface.drop(2)
        } else if (surface.startsWith("श")) {
            "छ" + surface.drop(1)
        } else {
            surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.63: Substituted 'ś' with 'ch' after jhay stop."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'श', "छ", sutra))) }
    }

    private fun normalizeVowelMark(mark: Char): Char = when (mark) {
        'ा' -> 'आ'
        'ि' -> 'इ'
        'ी' -> 'ई'
        'ु' -> 'उ'
        'ू' -> 'ऊ'
        'ृ' -> 'ऋ'
        'ॄ' -> 'ॠ'
        'े' -> 'ए'
        'ै' -> 'ऐ'
        'ो' -> 'ओ'
        'ौ' -> 'औ'
        else -> mark
    }
}
