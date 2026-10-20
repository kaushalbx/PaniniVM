package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 190: करणाधिकरणयोर्ल्युट् । */
object KaranaAdhikaranaLyutSutra : LinganushasanaSutra(
    "190", "करणाधिकरणयोर्ल्युट् ।", "करण अथवा अधिकरण अर्थ में ल्युट्-प्रत्ययान्त शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, 40,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.pratyaya == "ल्युट्" && context.governingLinga != null && context.semanticClasses.any { it == LingaSemanticClass.INSTRUMENT || it == LingaSemanticClass.LOCATION } }
