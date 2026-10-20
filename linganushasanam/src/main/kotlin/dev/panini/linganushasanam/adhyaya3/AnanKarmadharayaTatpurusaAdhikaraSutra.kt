package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 127: अनङ्कर्मधारयस्तत्पुरुषः — compound-scope adhikāra. */
object AnanKarmadharayaTatpurusaAdhikaraSutra : LinganushasanaSutra(
    number = "127", text = "अनङ्कर्मधारयस्तत्पुरुषः ।",
    hindiExplanation = "यह अञ्-कर्मधारय से भिन्न तत्पुरुष समासों के अगले विधानों का अधिकारसूत्र है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA,
    kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext): Boolean = false }
