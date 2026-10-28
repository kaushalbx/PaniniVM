package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser

/** Semantic projection of a genitive membership declaration, not a new verb syntax. */
object ListDeclarationLowering {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object NotDeclaration : Analysis
        data class Resolved(val sequence: Sequence) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidDeclaration(val code: String, message: String) : IllegalArgumentException(message)

    private fun requireDeclaration(value: Boolean, code: String, message: () -> String) {
        if (!value) throw InvalidDeclaration(code, message())
    }

    private val collect by lazy {
        PaniniParser().parse("सम् + ग्रहँ + श्ना + लोट् + सिप् ।").body as Invocation
    }
    private val place by lazy {
        PaniniParser().parse("फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।").body as Invocation
    }

    /** Only a quoted nominal naming a declared list is a binding, never a quoted command. */
    fun analyze(node: Quotation): Analysis = analyze(node, ::expandInternal)
    fun analyze(node: Invocation): Analysis = analyze(node, ::expandInternal)

    private fun <T : ProgramNode> analyze(node: T, lowering: (T) -> Sequence?): Analysis = try {
        lowering(node)?.let(Analysis::Resolved) ?: Analysis.NotDeclaration
    } catch (error: InvalidDeclaration) {
        val span = when (node) {
            is Invocation -> node.relationSpan()
            is Quotation -> (node.reporting as? Invocation)?.relationSpan() ?: node.quoted.relationSpan()
            else -> null
        }
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), span, node.sourceText))
    }

    fun expand(node: Quotation): Sequence? = when (val analysis = analyze(node)) {
        Analysis.NotDeclaration -> null
        is Analysis.Resolved -> analysis.sequence
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }

    fun expand(node: Invocation): Sequence? = when (val analysis = analyze(node)) {
        Analysis.NotDeclaration -> null
        is Analysis.Resolved -> analysis.sequence
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }

    private fun expandInternal(node: Quotation): Sequence? {
        val quoted = node.quoted.vakya as? NamaVakya ?: return null
        val name = quoted.padas.singleOrNull() as? SubantaPada ?: return null
        val reporting = node.reporting as? Invocation ?: return null
        val declaration = expand(reporting) ?: return null
        requireDeclaration(reporting.vakya.padas.none { (it as? AvyayaPada)?.function == AvyayaFunction.NAMING },
            "LIST_DECLARATION_DUPLICATE_NAME") {
            "A declaration cannot supply both इति and नाम names."
        }
        return withName(declaration, name, node.sourceText)
    }

    private fun withName(declaration: Sequence, name: SubantaPada, sourceText: String): Sequence {
        requireDeclaration(name.sup.affixCandidates == listOf(SupAffix.SU), "LIST_DECLARATION_NAME_AGREEMENT") {
            "A list name must be declared as one nominative singular referent."
        }
        val destination = name.copy(sourceText = "${name.pratipadika.sourceText} + ङि",
            sup = SupPratyaya("ङि", "ङि")).inheritLocation(name)
        val placement = place.vakya as AkhyataVakya
        val padas = placement.padas.map { pada ->
            if (pada is SubantaPada && (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                MulaPratipadikaIdentity.SUCHI) destination else pada
        }
        val namedPlace = Invocation(placement.copy(padas = padas))
        // The generic list noun retains the latest declared list as a discourse
        // referent, independently of intervening arithmetic/display results.
        return declaration.copy(sourceText = sourceText,
            statements = listOf(declaration.statements.first(), namedPlace, place),
            connectors = listOf("ततः", "ततः"))
    }

    private fun expandInternal(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS) return null
        val target = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SUCHI &&
                it.sup.affixCandidates == listOf(SupAffix.SU)
        } ?: return null
        requireDeclaration((target.pratipadika as MulaPratipadika).vikaras.isEmpty(),
            "LIST_DECLARATION_DERIVED_HEAD") {
            "A derived form of सूची is not the plain list declaration noun."
        }
        val naming = sentence.padas.filterIsInstance<AvyayaPada>().filter { it.function == AvyayaFunction.NAMING }
        requireDeclaration(naming.size <= 1, "LIST_DECLARATION_DUPLICATE_NAME") {
            "A list declaration requires one unambiguous name."
        }
        val name = naming.singleOrNull()?.let { marker ->
            val index = sentence.padas.indexOf(marker)
            requireDeclaration(index > 0, "LIST_DECLARATION_MISSING_NAME") {
                "नाम requires a preceding nominal name."
            }
            (sentence.padas[index - 1] as? SubantaPada)
                ?: throw InvalidDeclaration("LIST_DECLARATION_MISSING_NAME", "नाम requires a preceding nominal name.")
        }
        requireDeclaration(name == null || name.sup.affixCandidates == listOf(SupAffix.SU),
            "LIST_DECLARATION_NAME_AGREEMENT") {
            "नाम requires one nominative singular name."
        }
        requireDeclaration(sentence.tinganta.lakara == Lakara.LAT && sentence.tinganta.ting.affix == TingAffix.TIP &&
            sentence.tinganta.upasargas.isEmpty() && sentence.tinganta.dhatu.sanadiPratyayas.isEmpty(),
            "LIST_DECLARATION_COPULA_AGREEMENT") {
            "A singular list declaration requires the present third-person singular अस्ति."
        }
        val qualifiers = sentence.padas.filterIsInstance<SubantaPada>().filter {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity in MEMBER_TYPES.keys &&
                it.sup.affixCandidates == listOf(SupAffix.AM_6)
        }
        requireDeclaration(qualifiers.size <= 1, "LIST_DECLARATION_AMBIGUOUS_MEMBER_TYPE") {
            "A list declaration requires one unambiguous member-type qualifier."
        }
        val qualifier = qualifiers.singleOrNull()
        requireDeclaration(qualifier == null || (qualifier.pratipadika as MulaPratipadika).vikaras.isEmpty(),
            "LIST_DECLARATION_DERIVED_MEMBER_TYPE") {
            "Derived member qualifiers require their own semantics, not the base noun's member type."
        }
        val members = sentence.padas.filter { it !== target && it !== sentence.tinganta && it !== qualifier &&
            it !== name && it !in naming }
        if (members.isEmpty()) {
            requireDeclaration(qualifier != null, "LIST_DECLARATION_EMPTY_MEMBER_TYPE") {
                "An empty list declaration requires an explicit member-type qualifier."
            }
        }
        fun accusative(sup: SupPratyaya): SupPratyaya {
            val genitive = sup.affixCandidates.singleOrNull { it.vibhakti == Vibhakti.SASTHI }
                ?: throw InvalidDeclaration("LIST_DECLARATION_MEMBER_CASE",
                    "List declaration members require genitive case.")
            val text = SupAffix.select(Vibhakti.DVITIYA, genitive.vacana).upadesha
            return SupPratyaya(text, text)
        }
        fun project(pada: Pada): Pada = when (pada) {
            is SubantaPada -> pada.copy(sup = accusative(pada.sup)).inheritLocation(pada)
            is SankhyaPada -> pada.copy(sup = accusative(pada.sup))
            is SamuccitaSubanta -> pada.copy(members = pada.members.map { project(it) as SubantaPada })
            is AvyayaPada -> {
                requireDeclaration(pada.form == "च", "LIST_DECLARATION_MEMBER_COORDINATION") {
                    "Only member coordination is supported in a list declaration."
                }
                pada
            }
            else -> throw InvalidDeclaration("LIST_DECLARATION_UNSUPPORTED_MEMBER",
                "Unsupported list declaration member.")
        }
        val head = (collect.vakya as AkhyataVakya).tinganta.copy(
            listMemberType = qualifier?.let { MEMBER_TYPES[(it.pratipadika as MulaPratipadika).lexicalIdentity] })
            .inheritLocation((collect.vakya as AkhyataVakya).tinganta)
        val gathering = Invocation(AkhyataVakya(node.sourceText, members.map(::project) + head, head))
        val declaration = Sequence(node.sourceText, listOf(gathering, place), listOf("ततः"))
        return if (name == null) declaration else withName(declaration, name, node.sourceText)
    }

    private val MEMBER_TYPES = mapOf(
        MulaPratipadikaIdentity.SANKHYA to ListMemberType.NUMBER,
        MulaPratipadikaIdentity.SHABDA to ListMemberType.TEXT,
    )
}
