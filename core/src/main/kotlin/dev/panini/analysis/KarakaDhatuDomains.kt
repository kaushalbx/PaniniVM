package dev.panini.analysis

import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas
import java.util.concurrent.ConcurrentHashMap

/**
 * Typed lexical domains used by kāraka rules.
 *
 * Spellings are parsed once at this model boundary. Matching below is entirely
 * over phonological varṇa sequences, so individual sūtras do not normalize or
 * search Devanāgarī text.
 */
object KarakaDhatuDomains {
    /** The action being analyzed may belong to a nominal, rather than the enclosing verb. */
    fun actionRoot(context: KarakaRuleContext): String? = context.governingKrt?.dhatu?.mulaDhatu
        ?: (context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada)?.dhatu?.mulaDhatu

    fun actionPrefixes(context: KarakaRuleContext): List<String>? = context.governingKrt?.upasargas
        ?: (context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada)?.upasargas

    fun governingRoot(context: VibhaktiRuleContext): DhatuIdentity? {
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return context.dhatu
        if (verb.upasargaIdentities.any { it == null }) return null
        return DhatuIdentity(verb.dhatu.mulaDhatu)
    }

    fun isHu(context: VibhaktiRuleContext): Boolean = governingRoot(context)?.exact("हु") == true

    /** Kāśikā excludes tanādi मनु; the written root alone cannot distinguish homonyms without a lexical entry. */
    fun isMan(context: VibhaktiRuleContext): Boolean = governingRoot(context)?.exact("मन्", "मन") == true &&
        context.baseDhatu?.gana.let { it == null || it == dev.panini.core.DhatuGana.DIVADI }

    fun isJna(context: VibhaktiRuleContext): Boolean = governingRoot(context)?.exact("ज्ञा") == true

    fun isRemembering(context: VibhaktiRuleContext): Boolean {
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
        val root = governingRoot(context) ?: return false
        if (root.exact("स्मृ", "स्मर्")) return true
        if (verb == null) return root.exact("अधी")
        return root.exact("इ", "इक्") && verb.upasargaIdentities == listOf(dev.panini.core.Upasarga.ADHI)
    }

    fun isDay(dhatu: DhatuIdentity): Boolean = dhatu.exact("दय्", "दय")
    fun isIsh(dhatu: DhatuIdentity): Boolean = dhatu.exact("ईश्", "ईश")

    fun isKr(context: VibhaktiRuleContext): Boolean = governingRoot(context)?.exact("कृ", "कृञ्") == true
    fun isNath(context: VibhaktiRuleContext): Boolean =
        governingRoot(context)?.exact("नाथ्", "नाथ", "नाध्", "नाध") == true

    fun isVyavahrOrPan(context: VibhaktiRuleContext): Boolean {
        val root = governingRoot(context) ?: return false
        if (root.exact("पण्", "पण")) return true
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
            ?: return root.exact("व्यवहृ")
        return root.exact("हृ") && verb.upasargaIdentities ==
            listOf(dev.panini.core.Upasarga.VI, dev.panini.core.Upasarga.AVA)
    }

    fun isPrefixedDiv(context: VibhaktiRuleContext): Boolean {
        val root = governingRoot(context) ?: return false
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return false
        return root.exact("दिव्", "दिव", "दीव्") && verb.upasargaIdentities.isNotEmpty()
    }

    fun isDivTransactionRoot(context: VibhaktiRuleContext): Boolean =
        governingRoot(context)?.exact("दिव्", "दिव", "दीव्") == true
    fun isYaj(context: VibhaktiRuleContext): Boolean = governingRoot(context)?.exact("यज्", "यज") == true

    fun isPresyaOrBruhi(context: VibhaktiRuleContext): Boolean {
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return false
        val root = governingRoot(context) ?: return false
        if (verb.lakara != dev.panini.core.Lakara.LOT ||
            verb.ting.affix != dev.panini.core.TingAffix.SIP ||
            verb.dhatu.sanadiPratyayas.isNotEmpty() || verb.vikarana == dev.panini.vyakaranam.ast.Vikarana.YAK) return false
        if (root.exact("ब्रू")) return true
        val lexical = context.baseDhatu ?: return false
        return root.exact("इष्", "इष") && verb.upasargaIdentities == listOf(dev.panini.core.Upasarga.PRA) &&
            lexical.gana == dev.panini.core.DhatuGana.DIVADI && DhatuIdentity(lexical.sourceSurface).exact("इष्", "इष")
    }

    fun isSpecifiedInjuryVerb(context: VibhaktiRuleContext): Boolean {
        val root = governingRoot(context) ?: return false
        if (root.exact("जस्", "जस")) {
            val lexical = context.baseDhatu ?: return false
            return lexical.gana == dev.panini.core.DhatuGana.CURADI &&
                DhatuIdentity(lexical.sourceSurface).exact("जस्", "जस")
        }
        if (root.exact("नट्", "नट", "क्रथ्", "क्रथ", "पिष्", "पिष")) return true
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
        if (verb == null) return root.exact("निप्रहन्", "प्रणिहन्", "निहन्", "प्रहन्")
        if (!root.exact("हन्", "हन")) return false
        val ni = dev.panini.core.Upasarga.NI
        val pra = dev.panini.core.Upasarga.PRA
        return verb.upasargaIdentities in listOf(listOf(ni), listOf(pra), listOf(ni, pra), listOf(pra, ni))
    }

    fun isExcludedDiseaseVerb(context: VibhaktiRuleContext): Boolean {
        val root = governingRoot(context) ?: return false
        if (root.exact("ज्वर्", "ज्वर", "सन्तप्", "संतप्", "सन्ताप्", "संताप्")) return true
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
        return root.exact("तप्", "तप") && dev.panini.core.Upasarga.SAM in verb?.upasargaIdentities.orEmpty()
    }

    /** Verified lexical slice of the open rujārtha class; sense still requires independent evidence. */
    fun isDiseasePainVerb(context: VibhaktiRuleContext): Boolean =
        !isExcludedDiseaseVerb(context) && (governingRoot(context)?.exact("रुज्", "रुज", "अम्", "अम") == true ||
            SemanticRelation.RUJA_ACTION_SENSE in context.participant?.semanticRelations.orEmpty())

    /** Causation is independent of active/passive voice; yak does not erase ṇic. */
    fun isCausativeConstruction(context: KarakaRuleContext): Boolean {
        val nominal = context.governingKrt
        val finite = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
        val action = nominal?.dhatu ?: finite?.dhatu
            ?: return context.prayoga == dev.panini.core.Prayoga.CAUSATIVE
        val prefixes = nominal?.upasargaIdentities ?: finite?.upasargaIdentities.orEmpty()
        return prefixes.none { it == null } && action.sanadiAffixes.none { it == null } &&
            dev.panini.core.SanadiAffix.NIC in action.sanadiAffixes
    }

    /** Action-sense evidence is independent of a participant's case or general category. */
    private fun hasEstablishedActionMeaning(context: KarakaRuleContext, relation: SemanticRelation): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return context.baseDhatu?.let { it.sourceVarnas == root.varnas && relation in it.semanticRelations } == true ||
            (context.allParticipants + context.participant).any { other ->
                other.governingNominal === context.participant.governingNominal &&
                    other.purposeGovernor?.id == context.participant.purposeGovernor?.id &&
                    other.meaningEvidenceStrength(relation) >= 2
            }
    }

    private val forms = ConcurrentHashMap<String, List<Varna>>()

    private fun form(value: String): List<Varna> = forms.computeIfAbsent(value) { it.toVarnas() }

    private fun DhatuIdentity.exact(vararg values: String): Boolean = values.any { varnas == form(it) }

    fun isAbhinivish(context: KarakaRuleContext): Boolean {
        context.governingKrt?.let { governor ->
            return governor.upasargaIdentities.none { it == null } &&
                governor.dhatu.mulaDhatu in setOf("विश्", "विश") &&
                governor.upasargaIdentities.takeLast(2) == listOf(dev.panini.core.Upasarga.ABHI, dev.panini.core.Upasarga.NI)
        }
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
            ?: return false
        if (verb.upasargaIdentities.any { it == null }) return false
        return verb.dhatu.mulaDhatu in setOf("विश्", "विश") &&
            verb.upasargaIdentities.takeLast(2) == listOf(dev.panini.core.Upasarga.ABHI, dev.panini.core.Upasarga.NI)
    }

    fun isExcludedAdhikaranaLocus(context: KarakaRuleContext): Boolean {
        if (isAbhinivishLocativeUsage(context)) return false
        val rawPrefixes = actionPrefixes(context).orEmpty()
        val prefixes = rawPrefixes.map(dev.panini.core.Upasarga::fromUpadesha)
        if (prefixes.any { it == null }) return true
        val root = actionRoot(context)
        if (isFastingVas(context)) return false
        return isAbhinivish(context) ||
            (dev.panini.core.Upasarga.ADHI in prefixes && root in setOf("शी", "शीङ्", "स्था", "आस्", "आस")) ||
            (isResidenceVas(context) && prefixes.any { it in setOf(
                dev.panini.core.Upasarga.UPA, dev.panini.core.Upasarga.ANU,
                dev.panini.core.Upasarga.ADHI, dev.panini.core.Upasarga.ANG,
            ) }) || (isResidenceVas(context) && "आ" in rawPrefixes)
    }

    fun isAbhinivishLocativeUsage(context: KarakaRuleContext): Boolean =
        isAbhinivish(context) && context.participant.meaningEvidenceStrength(SemanticRelation.ABHINIVESH_LOCATIVE_USAGE) >= 2

    /** Śap in vasati selects bhvādi nivāsa, not adādi ācchādana. */
    fun isResidenceVas(context: KarakaRuleContext): Boolean {
        return actionRoot(context) in setOf("वस", "वस्", "वसँ") &&
            (context.baseDhatu == null || context.baseDhatu.gana == dev.panini.core.DhatuGana.BHVADI)
    }

    fun isFastingVas(context: KarakaRuleContext): Boolean {
        return actionRoot(context) in setOf("वस", "वस्", "वसँ") &&
            (context.participant.meaningEvidenceStrength(SemanticRelation.FASTING_SENSE) >= 2 ||
                SemanticRelation.FASTING_SENSE in context.baseDhatu?.semanticRelations.orEmpty())
    }

    /** Kāśikā/Kaumudī's attested classes plus independently established meaning equivalents. */
    fun isDvikarmaka(dhatu: DhatuIdentity, prefixes: List<dev.panini.core.Upasarga?> = emptyList(),
        hasEstablishedSense: Boolean = false): Boolean = prefixes.none { it == null } &&
        (hasEstablishedSense || dhatu.exact(
            "दुह्", "याच्", "रुध्", "प्रच्छ्", "भिक्ष्", "चि", "ब्रू", "शास्", "जि", "मन्थ्", "मथ्", "मुष्", "नी", "हृ", "कृष्", "वह्", "पच्", "दण्ड्",
            "भाष्", "वच्",
        ) || dhatu.exact("वद्") && prefixes.lastOrNull() == dev.panini.core.Upasarga.ABHI)

    fun isDvikarmaka(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return isDvikarmaka(root, actionPrefixes(context).orEmpty().map(dev.panini.core.Upasarga::fromUpadesha),
            hasEstablishedDvikarmakaSense(context))
    }

    fun hasEstablishedDvikarmakaSense(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return context.baseDhatu?.let {
            it.sourceVarnas == root.varnas && SemanticRelation.DVIKARMAKA_ACTION_SENSE in it.semanticRelations
        } == true || (context.allParticipants + context.participant).any { other ->
            other.governingNominal === context.participant.governingNominal &&
                other.purposeGovernor?.id == context.participant.purposeGovernor?.id &&
                other.meaningEvidenceStrength(SemanticRelation.DVIKARMAKA_ACTION_SENSE) >= 2
        }
    }

    /** Known non-causative dvikarmaka pattern; a general meaning-class declaration alone does not select promotion. */
    fun promotesSecondaryObjectInPassive(context: KarakaRuleContext): Boolean? {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        val prefixes = actionPrefixes(context).orEmpty().map(dev.panini.core.Upasarga::fromUpadesha)
        if (prefixes.any { it == null }) return null
        if (hasEstablishedActionMeaning(context, SemanticRelation.PRIMARY_OBJECT_PROMOTING_SENSE)) return false
        if (hasEstablishedActionMeaning(context, SemanticRelation.SECONDARY_OBJECT_PROMOTING_SENSE)) return true
        if (root.exact("नी", "हृ", "कृष्", "वह्")) return false
        return true.takeIf { isDvikarmaka(root, prefixes) }
    }

    fun isHiding(dhatu: DhatuIdentity): Boolean = dhatu.exact("निली", "तिरोभू")

    fun isHiding(context: KarakaRuleContext): Boolean {
        if (context.participant.hasEstablishedMeaning(SemanticRelation.SELF_CONCEALMENT)) return true
        val prefixes = actionPrefixes(context) ?: return isHiding(context.dhatu)
        if (prefixes.any { dev.panini.core.Upasarga.fromUpadesha(it) == null }) return false
        if (actionRoot(context)?.let { isHiding(DhatuIdentity(it)) } == true) return true
        return actionRoot(context) in setOf("ली", "लीङ्") &&
            dev.panini.core.Upasarga.NI in prefixes.map(dev.panini.core.Upasarga::fromUpadesha)
    }
    fun isFearOrProtection(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("भी", "बिभ", "बिभेति", "बिभ्यति", "बिभ्यात्", "भीति", "त्रा", "त्राय", "त्रायते", "त्रै", "रक्ष्", "रक्ष", "उद्विज्", "उद्विज")
    /** The written action owns root and prefixes; 1.4.25 is a meaning class, not a closed list. */
    fun isFearOrProtection(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: return isFearOrProtection(context.dhatu) ||
            hasEstablishedActionMeaning(context, SemanticRelation.FEAR_PROTECTION_ACTION_SENSE)
        val prefixes = actionPrefixes(context).orEmpty().map(dev.panini.core.Upasarga::fromUpadesha)
        if (prefixes.any { it == null }) return false
        return isFearOrProtection(root) || (root.exact("विज्", "विज") && dev.panini.core.Upasarga.UD in prefixes) ||
            hasEstablishedActionMeaning(context, SemanticRelation.FEAR_PROTECTION_ACTION_SENSE)
    }
    fun isOriginBhu(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("भू", "भव्", "भव", "प्रभू", "प्रभव्", "प्रभव")
    fun isBirth(dhatu: DhatuIdentity): Boolean = dhatu.exact("जन्", "जन", "जाय", "जाय्", "जायते")
    fun isParaji(dhatu: DhatuIdentity): Boolean = dhatu.exact("पराजि", "पराजय", "पराजयते")

    fun isParaji(context: KarakaRuleContext): Boolean {
        context.governingKrt?.let { governor ->
            return governor.upasargaIdentities.none { it == null } &&
                dev.panini.core.Upasarga.PARA in governor.upasargaIdentities &&
                governor.dhatu.mulaDhatu in setOf("जि", "जि॒")
        }
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
            ?: return isParaji(context.dhatu)
        return verb.upasargaIdentities.none { it == null } &&
            dev.panini.core.Upasarga.PARA in verb.upasargaIdentities &&
            verb.dhatu.mulaDhatu in setOf("जि", "जि॒")
    }
    fun isVarana(dhatu: DhatuIdentity): Boolean = dhatu.exact("वृ", "वारय")

    fun isVarana(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context) ?: return isVarana(context.dhatu) ||
            hasEstablishedActionMeaning(context, SemanticRelation.VARANA_ACTION_SENSE)
        val prefixes = actionPrefixes(context).orEmpty().map(dev.panini.core.Upasarga::fromUpadesha)
        if (prefixes.any { it == null }) return false
        if (isVarana(DhatuIdentity(root)) || hasEstablishedActionMeaning(context, SemanticRelation.VARANA_ACTION_SENSE)) return true
        return root in setOf("वृत्", "वृत") && dev.panini.core.Upasarga.NI in prefixes &&
            isCausativeConstruction(context)
    }

    fun isDiv(dhatu: DhatuIdentity): Boolean = dhatu.exact("दिव्", "दिव", "दीव्")
    fun isDiv(context: KarakaRuleContext): Boolean =
        isDiv(actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu)
    fun isDhari(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return root.exact("धृ") && isCausativeConstruction(context)
    }
    fun isHrKr(context: KarakaRuleContext): Boolean =
        (actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu).exact("हृ", "कृ")

    fun isCausativeObjectDomain(context: KarakaRuleContext): Boolean {
        if (context.participant.meaningEvidenceStrength(SemanticRelation.PRIOR_CAUSATIVE_AGENT) >= 2) return false
        val verb = (context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada).takeIf { context.governingKrt == null }
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        if (isAbhivadSalutation(context)) return false
        // Bāla-manoramā states the śabdāyati exclusion; the speech additions follow the Bhāṣya list it reports.
        if (root.exact("शब्दाय", "शब्दाय्", "ह्वे", "ह्वेञ्", "क्रन्द्", "क्रन्द")) return false
        val speechPrefixes = actionPrefixes(context)?.map(dev.panini.core.Upasarga::fromUpadesha).orEmpty()
        if (speechPrefixes.none { it == null } && (root.exact("जल्प्", "जल्प") ||
                root.exact("लप्", "लप") && speechPrefixes == listOf(dev.panini.core.Upasarga.VI) ||
                root.exact("भाष्", "भाष") && speechPrefixes == listOf(dev.panini.core.Upasarga.ANG))) return true
        if (root.exact("दृश्", "दृश")) return verb != null &&
            verb.upasargaIdentities.none { it == null } &&
            verb.ting.affix?.pada == dev.panini.core.PadaType.PARASMAIPADA
        // Kāśikā's nī and adi/khādi prohibitions override general semantic classes.
        if (root.exact("नी", "अद्", "अद", "खाद्", "खाद")) return false
        if (root.exact("वह्", "वह")) {
            return hasEstablishedActionMeaning(context, SemanticRelation.VAH_NIYANTR_KARTRKA) &&
                !hasEstablishedActionMeaning(context, SemanticRelation.VAH_ANIYANTR_KARTRKA)
        }
        if (root.exact("भक्ष्", "भक्ष")) {
            return hasEstablishedActionMeaning(context, SemanticRelation.BHAKS_INJURIOUS_SENSE) &&
                !hasEstablishedActionMeaning(context, SemanticRelation.BHAKS_NONINJURIOUS_SENSE)
        }
        if (context.baseDhatu?.karmatva == dev.panini.shiksha.Karmatva.AKARMAKA) return true
        val classes = context.baseDhatu?.semanticRelations.orEmpty()
        if (classes.any { it in setOf(SemanticRelation.MOTION, SemanticRelation.COGNITION,
                SemanticRelation.CONSUMPTION, SemanticRelation.WORD_AS_OBJECT) }) return true
        return root.exact("गम्", "ज्ञा", "बुध्", "पठ्", "पठ", "अधी", "भुज्", "अश्", "श्रु")
    }

    /** Padamañjarī on 1.4.51: these causatives express the primary or secondary object as desired. */
    fun hasFreeCausativePromotion(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return root.exact("बुध्", "बुध", "ज्ञा", "विद्", "भुज्", "अश्", "पठ्", "पठ", "अधी") ||
            context.baseDhatu?.semanticRelations.orEmpty().any { it in setOf(SemanticRelation.COGNITION,
                SemanticRelation.CONSUMPTION, SemanticRelation.WORD_AS_OBJECT) }
    }

    fun isAbhivadSalutation(context: KarakaRuleContext): Boolean {
        val prefixes = actionPrefixes(context)?.map(dev.panini.core.Upasarga::fromUpadesha) ?: return false
        return actionRoot(context) in setOf("वद्", "वद") &&
            prefixes.none { it == null } && dev.panini.core.Upasarga.ABHI in prefixes &&
            hasEstablishedActionMeaning(context, SemanticRelation.ABHIVAD_SALUTATION_SENSE)
    }

    fun isAtmanepadaOptionalCauseeExtension(context: KarakaRuleContext): Boolean {
        if (context.governingKrt != null) return false
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return false
        // Passive endings alone do not establish the underlying causative's lexical pada.
        if (verb.vikarana == dev.panini.vyakaranam.ast.Vikarana.YAK) return false
        return verb.ting.affix?.pada == dev.panini.core.PadaType.ATMANEPADA &&
            verb.upasargaIdentities.none { it == null } &&
            (verb.dhatu.mulaDhatu in setOf("दृश्", "दृश") || isAbhivadSalutation(context))
    }
    fun isJugupsaViramaPramada(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("जुगुप्स्", "जुगुप्स", "जुगुप्सते", "विरम्", "विरम", "विराम", "प्रमद्", "प्रमद", "प्रमाद्यति")

    fun isJugupsaViramaPramada(context: KarakaRuleContext): Boolean {
        val verb = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada
            ?: return isJugupsaViramaPramada(context.dhatu)
        val prefixes = verb.upasargaIdentities
        if (prefixes.any { it == null }) return false
        val root = verb.dhatu.mulaDhatu
        return root in setOf("जुगुप्स्", "जुगुप्स") ||
            (root in setOf("रम्", "रम") && dev.panini.core.Upasarga.VI in prefixes) ||
            (root in setOf("मद्", "मद") && dev.panini.core.Upasarga.PRA in prefixes)
    }

    private val krudhDruhForms = arrayOf("क्रुध्", "क्रुध", "द्रुह्", "द्रुह", "क्रुध्य", "द्रुह्य")
    private fun isAngerRoot(dhatu: DhatuIdentity): Boolean = dhatu.exact(*krudhDruhForms) ||
        dhatu.exact("ईर्ष्या", "ईर्ष्य", "ईर्ष्य्", "असूया", "असूय", "असूय्")
    private fun isComposedKrudhDruh(dhatu: DhatuIdentity): Boolean =
        dev.panini.core.Upasarga.entries.any { prefix -> krudhDruhForms.any { root ->
            dhatu.varnas == form(prefix.upadesha) + form(root)
        } }

    fun isAnger(context: KarakaRuleContext): Boolean = hasEstablishedActionMeaning(context, SemanticRelation.ANGER_ACTION_SENSE) ||
        (actionRoot(context)?.let { isAngerRoot(DhatuIdentity(it)) }
            ?: (isAngerRoot(context.dhatu) || isComposedKrudhDruh(context.dhatu)))

    /** A supplied AST is authoritative; unknown prefixes must not fall back to spelling guesses. */
    fun hasAngerUpasarga(context: KarakaRuleContext): Boolean? {
        val prefixes = actionPrefixes(context)?.map(dev.panini.core.Upasarga::fromUpadesha)
            ?: return isComposedKrudhDruh(context.dhatu)
        if (prefixes.any { it == null }) return null
        return prefixes.isNotEmpty()
    }

    fun isPrefixedKrudhDruh(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: return isComposedKrudhDruh(context.dhatu)
        return hasAngerUpasarga(context) == true &&
            root.exact(*krudhDruhForms)
    }

    fun isRuc(context: KarakaRuleContext): Boolean = hasEstablishedActionMeaning(context, SemanticRelation.RUCI_ACTION_SENSE) ||
        (actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu).exact("रुच्", "रुच", "रोच", "स्वद्", "स्वद")
    fun isDestinyInquiry(context: KarakaRuleContext): Boolean {
        if (actionPrefixes(context)?.any { dev.panini.core.Upasarga.fromUpadesha(it) == null } == true) return false
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu
        return root.exact("राध्", "राध", "ईक्ष्", "ईक्ष")
    }

    /** The written action, including a linked kṛt, owns both root and prefixes. */
    private fun hasPriorRecipientPrefix(context: KarakaRuleContext, vararg prefixes: dev.panini.core.Upasarga): Boolean {
        val actual = actionPrefixes(context)?.map(dev.panini.core.Upasarga::fromUpadesha) ?: return false
        return actual.none { it == null } && actual.any { it in prefixes }
    }

    fun isPromiseShru(context: KarakaRuleContext): Boolean =
        actionRoot(context)?.let { DhatuIdentity(it).exact("श्रु", "श्रुँ") } == true &&
            hasPriorRecipientPrefix(context, dev.panini.core.Upasarga.PRATI, dev.panini.core.Upasarga.ANG) &&
            (context.baseDhatu == null || DhatuIdentity(context.baseDhatu.sourceSurface).exact("श्रु", "श्रुँ"))

    fun isEncouragementGri(context: KarakaRuleContext): Boolean =
        actionRoot(context)?.let { DhatuIdentity(it).exact("गृ", "गॄ") } == true &&
            hasPriorRecipientPrefix(context, dev.panini.core.Upasarga.ANU, dev.panini.core.Upasarga.PRATI) &&
            context.baseDhatu?.gana == dev.panini.core.DhatuGana.KRYADI &&
            DhatuIdentity(context.baseDhatu.sourceSurface).exact("गृ", "गॄ")

    /** An unproved prior-action reading cannot be replaced by generic recipient intent. */
    fun isPriorActionRecipientDomain(context: KarakaRuleContext): Boolean {
        val root = actionRoot(context)?.let(::DhatuIdentity) ?: return false
        return (root.exact("श्रु", "श्रुँ") &&
            hasPriorRecipientPrefix(context, dev.panini.core.Upasarga.PRATI, dev.panini.core.Upasarga.ANG)) ||
            (root.exact("गृ", "गॄ") &&
                hasPriorRecipientPrefix(context, dev.panini.core.Upasarga.ANU, dev.panini.core.Upasarga.PRATI))
    }
    fun isSprha(context: KarakaRuleContext): Boolean =
        (actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu).exact("स्पृह", "स्पृह्", "स्पृहय्", "स्पृहय")
    fun isShlaghHnuSthaShap(context: KarakaRuleContext): Boolean =
        (actionRoot(context)?.let(::DhatuIdentity) ?: context.dhatu)
            .exact("श्लाघ", "श्लाघ्", "ह्नु", "स्था", "तिष्ठ", "शप", "शप्")

}
