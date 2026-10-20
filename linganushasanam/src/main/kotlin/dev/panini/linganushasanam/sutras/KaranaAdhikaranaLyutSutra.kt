package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 188: करणाधिकरणयोर्ल्युट् । */
object KaranaAdhikaranaLyutSutra : LinganushasanaSutra(
    "188", "करणाधिकरणयोर्ल्युट् च", "करण अथवा अधिकरण अर्थ में ल्युट्-प्रत्ययान्त शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 40,
) { override fun matches(context: LingaRuleContext) = context.pratyaya == "ल्युट्" && context.governingLinga != null && context.semanticClasses.any { it == LingaSemanticClass.INSTRUMENT || it == LingaSemanticClass.LOCATION } }
