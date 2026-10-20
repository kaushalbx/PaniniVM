package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 182: कबन्धौषधायुधान्ताः । */
object KabandhaAusadhaAyudhantaSutra : LinganushasanaSutra(
    "182", "कबन्धौषधायुधान्ताः ।", "कबन्ध, औषध अथवा आयुध-अन्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, 10,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); override fun matches(context: LingaRuleContext) = context.pratipadika.endsWithVarnas("कबन्ध") || context.pratipadika.endsWithVarnas("औषध") || context.pratipadika.endsWithVarnas("आयुध") }
