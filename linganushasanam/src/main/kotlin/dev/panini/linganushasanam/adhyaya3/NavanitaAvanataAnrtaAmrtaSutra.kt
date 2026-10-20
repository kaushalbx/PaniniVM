package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 167: नवनीतावनतानृतामृतनिमित्तवित्तचित्तपित्तव्रतरजतवृत्तपलितानि । (अवनत स्थाने अवत इति केचित्) */
object NavanitaAvanataAnrtaAmrtaSutra : LinganushasanaSutra(
    number = "167", text = "नवनीतावनतानृतामृतनिमित्तवित्तचित्तपित्तव्रतरजतवृत्तपलितानि । (अवनत स्थाने अवत इति केचित्)", hindiExplanation = "सूत्र में पठित नवनीत आदि शब्द तथा अवत पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("नवनीत", "अवनत", "अवत", "अनृत", "अमृत", "निमित्त", "वित्त", "चित्त", "पित्त", "व्रत", "रजत", "वृत्त", "पलित")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

