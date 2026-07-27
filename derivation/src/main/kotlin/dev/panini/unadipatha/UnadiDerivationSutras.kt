package dev.panini.unadipatha

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.33 / Varttika: Adds yuk-āgama (y) to vā/pā/ma before uṇ-pratyaya. */
object UnadiYugAgamaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.33.unadi", text = "वातो युक् (उणादौ)",
    hindiExplanation = "वा, पा, मा धातु से उण् प्रत्यय परे होने पर युक् (य्) आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false, kramaValue = 730033,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return (root.surface == "वा" || root.surface == "पा" || root.surface == "म") &&
            suffix.surface == "उ" &&
            context.allEffectiveTerms.none { it.id == "yuk-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val yukAgama = DerivationTerm("yuk-agama", "य्", TermKind.AGAMA, upadesha = "युक्")
        return DerivationChange(
            context.copy(
                terms = context.terms.take(rootIndex + 1) + yukAgama + context.terms.drop(rootIndex + 1)
            ),
            "7.3.33: Added yuk-āgama (य्) after root."
        )
    }
}

/** 2.8.unadi: Adds tut-āgama (t) and elides the 'si' suffix. */
object UnadiTutAgamaSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.8.unadi", text = "सि-लोपः तुगागमश्च (उणादौ)",
    hindiExplanation = "श्रु, वृ, दृ, प्रा से परे सिः प्रत्यय का लोप और तुक् (त्) आगम होता है।",
    type = SutraType.NITYA, chapter = 2, pada = 8, optional = false, kramaValue = 280000,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return (root.surface == "श्रु" || root.surface == "वृ" || root.surface == "दृ" || root.surface == "प्रा") &&
            suffix.surface == "सि"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val tutAgama = DerivationTerm("tut-agama", "त्", TermKind.AGAMA, upadesha = "तुक्")
        
        val newTerms = context.terms.take(rootIndex + 1) + tutAgama
        return DerivationChange(
            context.copy(
                terms = newTerms,
                stage = DerivationStage.PADA_FORMED
            ),
            "2.8.unadi: Elided suffix 'सि' and added tut-āgama (त्)."
        )
    }
}

/** 4.1.unadi: Performs guṇa and adds mut-āgama (m) for kr/vr/dr before kanin (an). */
object UnadiMutAgamaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.unadi", text = "मुडागमः गुणादेशश्च (उणादौ)",
    hindiExplanation = "कृ, वृ, दॄ धातुओं को गुण तथा मुट् (म्) आगम होता है कनिन् से पहले।",
    type = SutraType.NITYA, chapter = 4, pada = 1, optional = false, kramaValue = 410000,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return (root.surface == "कृ" || root.surface == "वृ" || root.surface == "दृ") &&
            suffix.surface == "अन्" &&
            context.allEffectiveTerms.none { it.id == "mut-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val oldRoot = context.terms[rootIndex]
        
        val newRootSurface = when (oldRoot.surface) {
            "कृ" -> "कर्"
            "वृ" -> "वर्"
            "दृ" -> "धर्"
            else -> oldRoot.surface
        }
        val newRoot = oldRoot.copy(surface = newRootSurface)
        val mutAgama = DerivationTerm("mut-agama", "म्", TermKind.AGAMA, upadesha = "मुट्")
        
        return DerivationChange(
            context.copy(
                terms = context.terms.take(rootIndex) + newRoot + mutAgama + context.terms.drop(rootIndex + 1)
            ),
            "4.1.unadi: Substituted guṇa root and added mut-āgama (म्)."
        )
    }
}

/** Unadi adjustments for vowel lengthening (upadhā-dīrgha) and specific root replacements. */
object UnadiAdjustmentsSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.adjust", text = "उणादि-दीर्घत्वम् आदेशाश्च",
    hindiExplanation = "स्वद्, अश् का उपधा दीर्घ तथा मि का म् आदेश होता है।",
    type = SutraType.NITYA, chapter = 1, pada = 1, optional = false, kramaValue = 110000,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return suffix.surface == "उ" &&
            (root.surface == "स्वद्" || root.surface == "अश्" || root.surface == "मि")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val oldRoot = context.terms[rootIndex]
        
        val newSurface = when (oldRoot.surface) {
            "स्वद्" -> "स्वाद्"
            "अश्" -> "आश्"
            "मि" -> "म"
            else -> oldRoot.surface
        }
        
        return DerivationChange(
            context.copy(
                terms = context.terms.take(rootIndex) + oldRoot.copy(surface = newSurface) + context.terms.drop(rootIndex + 1)
            ),
            "1.1.adjust: Applied Uṇādi root transformation."
        )
    }
}
