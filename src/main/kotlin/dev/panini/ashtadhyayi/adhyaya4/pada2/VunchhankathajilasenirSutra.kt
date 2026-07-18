package dev.panini.ashtadhyayi.adhyaya4.pada2

import dev.panini.derivation.*
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.*

/** 4.2.80: वुञ्छण्कठजिलशेनिरढञ्ण्ययफक्फिञिञ्ञ्यकक्ठकोऽरीहणकृशाश्वर्ष्यकुमुदकाशतृणप्रेक्षाश्मसखिसंकाशबलपक्षकर्णसुतङ्गमप्रगदिन्वराहकुमुदादिभ्यः. */
object VunchhankathajilasenirSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.80", text = "वुञ्छण्कठजिलशेनिरढञ्ण्ययफक्फिञिञ्ञ्यकक्ठकोऽरीहणकृशाश्वर्ष्यकुमुदकाशतृणप्रेक्षाश्मसखिसंकाशबलपक्षकर्णसुतङ्गमप्रगदिन्वराहकुमुदादिभ्यः",
    hindiExplanation = "चातुरर्थिक अर्थ में क्रमशः अरीहणादि से कुमुदादि तक यथासंख्य प्रत्यय होते हैं।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420080,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private data class Selection(val gana: Int, val upadesha: String, val surface: String)
    private val selections = listOf(
        Selection(84, "वुञ्", "अक"), Selection(85, "छण्", "ईय"), Selection(86, "क", "अ"),
        Selection(87, "ठच्", "इक"), Selection(88, "इल", "इल"), Selection(89, "स", "स"),
        Selection(90, "इनि", "इनि"), Selection(91, "र", "र"), Selection(92, "ढञ्", "एय"),
        Selection(93, "ण्य", "य"), Selection(94, "य", "य"), Selection(95, "फक्", "आयन"),
        Selection(96, "फिन्", "आयनि"), Selection(97, "इन्", "इ"), Selection(98, "ञ्य", "य"),
        Selection(99, "कक्", "क"), Selection(100, "ठक्", "इक"),
    )
    override fun matches(context: DerivationState) = HasDerivationalEnvironment(DerivationalEnvironment.CHATURARTHIKA).matches(context) && context.terms.any { term -> term.kind == TermKind.PRATIPADIKA && selections.any { GanaPatha.isEligibleMember(it.gana, term.surface, term.lexicalUses) } }
    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            val selection = selections.firstOrNull { GanaPatha.isEligibleMember(it.gana, term.surface, term.lexicalUses) }
            if (term.kind == TermKind.PRATIPADIKA && selection != null && context.terms.none { it.id == "${term.id}-4-2-80" }) listOf(term, DerivationTerm("${term.id}-4-2-80", selection.surface, TermKind.PRATYAYA, upadesha = selection.upadesha)) else listOf(term)
        }
        return DerivationChange(context.copy(terms = terms), "4.2.80 selects the yathāsaṅkhya affix for the eligible Gaṇa.")
    }
}
