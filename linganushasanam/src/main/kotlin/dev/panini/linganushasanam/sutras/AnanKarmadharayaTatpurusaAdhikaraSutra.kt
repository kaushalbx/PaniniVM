package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 126: अनङ्कर्मधारयस्तत्पुरुषः — compound-scope adhikāra. */
object AnanKarmadharayaTatpurusaAdhikaraSutra : LinganushasanaSutra(
    number = "126", text = "अनञ्कर्मधारयस्तत्पुरुषः",
    hindiExplanation = "यह अञ्-कर्मधारय से भिन्न तत्पुरुष समासों के अगले विधानों का अधिकारसूत्र है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA),
    kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext): Boolean = false }
