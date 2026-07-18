package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.60: हलादिः शेषः. */
object HaladisSeshahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.60", text = "हलादिः शेषः",
    hindiExplanation = "अभ्यास में केवल आरम्भ का हल् रहता है; बाद के हलों का लोप होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740060,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val consonants = setOf(
        'क', 'ख', 'ग', 'घ', 'ङ', 'च', 'छ', 'ज', 'झ', 'ञ', 'ट', 'ठ', 'ड', 'ढ', 'ण',
        'त', 'थ', 'द', 'ध', 'न', 'प', 'फ', 'ब', 'भ', 'म', 'य', 'र', 'ल', 'व', 'श', 'ष', 'स', 'ह',
    )

    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            shortenedAbhyasa(abhyasa.surface) != abhyasa.surface
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val shortened = shortenedAbhyasa(abhyasa.surface)
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = shortened)),
            "7.4.60 retains only the initial consonant of the abhyāsa ${abhyasa.surface}.",
        )
    }

    private fun shortenedAbhyasa(surface: String): String {
        val retainInitialConsonant = surface.firstOrNull() in consonants
        var retainedInitialConsonant = false
        var removeVirama = false
        return buildString {
            surface.forEach { character ->
                if (removeVirama && character == '्') {
                    removeVirama = false
                } else if (character in consonants) {
                    if (retainInitialConsonant && !retainedInitialConsonant) {
                        append(character)
                        retainedInitialConsonant = true
                    } else {
                        removeVirama = true
                    }
                } else {
                    append(character)
                }
            }
        }
    }
}
