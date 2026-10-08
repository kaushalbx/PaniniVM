package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Lakara
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser

/** Semantic projection of a genitive membership declaration, not a new verb syntax. */
object ListDeclarationLowering {
    private val collect by lazy {
        PaniniParser().parse("सम् + ग्रहँ + श्ना + लोट् + सिप् ।").body as Invocation
    }
    private val place by lazy {
        PaniniParser().parse("फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।").body as Invocation
    }

    /** Only a quoted nominal naming a declared list is a binding, never a quoted command. */
    fun expand(node: Quotation): Sequence? {
        val quoted = node.quoted.vakya as? NamaVakya ?: return null
        val name = quoted.padas.singleOrNull() as? SubantaPada ?: return null
        val reporting = node.reporting as? Invocation ?: return null
        val declaration = expand(reporting) ?: return null
        require(reporting.vakya.padas.none { (it as? AvyayaPada)?.function == AvyayaFunction.NAMING }) {
            "A declaration cannot supply both इति and नाम names."
        }
        return withName(declaration, name, node.sourceText)
    }

    private fun withName(declaration: Sequence, name: SubantaPada, sourceText: String): Sequence {
        require(name.sup.text == SupAffix.SU.upadesha) {
            "A list name must be declared as one nominative singular referent."
        }
        val destination = name.copy(sourceText = "${name.pratipadika.sourceText} + ङि",
            sup = SupPratyaya("ङि", "ङि"))
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

    fun expand(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS) return null
        val target = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SUCHI &&
                it.sup.text == SupAffix.SU.upadesha
        } ?: return null
        require((target.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "A derived form of सूची is not the plain list declaration noun."
        }
        val naming = sentence.padas.filterIsInstance<AvyayaPada>().filter { it.function == AvyayaFunction.NAMING }
        require(naming.size <= 1) { "A list declaration requires one unambiguous name." }
        val name = naming.singleOrNull()?.let { marker ->
            val index = sentence.padas.indexOf(marker)
            require(index > 0) { "नाम requires a preceding nominal name." }
            (sentence.padas[index - 1] as? SubantaPada)
                ?: throw IllegalArgumentException("नाम requires a preceding nominal name.")
        }
        require(name == null || name.sup.text == SupAffix.SU.upadesha) {
            "नाम requires one nominative singular name."
        }
        require(sentence.tinganta.lakara == Lakara.LAT && sentence.tinganta.ting.text == "तिप्" &&
            sentence.tinganta.upasargas.isEmpty() && sentence.tinganta.dhatu.sanadiPratyayas.isEmpty()) {
            "A singular list declaration requires the present third-person singular अस्ति."
        }
        val qualifiers = sentence.padas.filterIsInstance<SubantaPada>().filter {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity in MEMBER_TYPES.keys &&
                it.sup.text == SupAffix.AM_6.upadesha
        }
        require(qualifiers.size <= 1) { "A list declaration requires one unambiguous member-type qualifier." }
        val qualifier = qualifiers.singleOrNull()
        require(qualifier == null || (qualifier.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Derived member qualifiers require their own semantics, not the base noun's member type."
        }
        val members = sentence.padas.filter { it !== target && it !== sentence.tinganta && it !== qualifier &&
            it !== name && it !in naming }
        if (members.isEmpty()) {
            require(name == null) { "A named list declaration must supply its members explicitly." }
            return null
        }
        fun accusative(sup: SupPratyaya): SupPratyaya {
            val genitive = SupAffix.candidates(sup.text).singleOrNull { it.vibhakti == Vibhakti.SASTHI }
                ?: throw IllegalArgumentException("List declaration members require genitive case.")
            val text = SupAffix.select(Vibhakti.DVITIYA, genitive.vacana).upadesha
            return SupPratyaya(text, text)
        }
        fun project(pada: Pada): Pada = when (pada) {
            is SubantaPada -> pada.copy(sup = accusative(pada.sup))
            is SankhyaPada -> pada.copy(sup = accusative(pada.sup))
            is SamuccitaSubanta -> pada.copy(members = pada.members.map { project(it) as SubantaPada })
            is AvyayaPada -> {
                require(pada.form == "च") { "Only member coordination is supported in a list declaration." }
                pada
            }
            else -> throw IllegalArgumentException("Unsupported list declaration member.")
        }
        val head = (collect.vakya as AkhyataVakya).tinganta.copy(
            listMemberType = qualifier?.let { MEMBER_TYPES[(it.pratipadika as MulaPratipadika).lexicalIdentity] })
        val gathering = Invocation(AkhyataVakya(node.sourceText, members.map(::project) + head, head))
        val declaration = Sequence(node.sourceText, listOf(gathering, place), listOf("ततः"))
        return if (name == null) declaration else withName(declaration, name, node.sourceText)
    }

    private val MEMBER_TYPES = mapOf(
        MulaPratipadikaIdentity.SANKHYA to ListMemberType.NUMBER,
        MulaPratipadikaIdentity.SHABDA to ListMemberType.TEXT,
    )
}
