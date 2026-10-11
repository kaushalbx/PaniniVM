package dev.panini.execution

import kotlin.test.*

class ProcedureLocalBindingsTest {
    private fun number(value: Long) = SanskritValue.Sankhya(value, value.toString())

    @Test fun `pronoun domain stops at a procedure boundary but includes lexical members`() {
        val caller = ProcedureLocalBindings.create(listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)),
            listOf(number(5)))
        val callee = ProcedureLocalBindings.create(listOf(PrakriyaParameter("मूल्य", PrakriyaValueType.SANKHYA)),
            listOf(number(2)), caller)
        assertEquals(2, callee.visibleReferents().size)
        assertEquals(listOf(callee.identity("मूल्य")), callee.anaphoricReferents().map { it.identity })
        val member = ProcedureLocalBindings.member(callee, "सदस्य", number(1))
        assertEquals(setOf(callee.identity("मूल्य"), member.identity("सदस्य")),
            member.anaphoricReferents().map { it.identity }.toSet())
        val emptyCall = ProcedureLocalBindings.create(emptyList(), emptyList(), member)
        assertTrue(emptyCall.anaphoricReferents().isEmpty())
    }

    @Test fun `invocation aliases share live cells without escaping their view`() {
        val outer = ProcedureLocalBindings.member(null, "मान", number(5))
        val identity = assertNotNull(outer.identity("मान"))
        val aliases = outer.withReferentAliases(mapOf("temporary" to identity))
        assertSame(identity, aliases.identity("temporary"))
        assertEquals(1, aliases.visibleReferents().size)
        aliases.accept(mapOf("temporary" to number(2)))
        assertEquals(number(2), outer.values["मान"])
        assertNull(outer.identity("temporary"))
        outer.accept(mapOf("मान" to number(3)))
        assertEquals(number(3), aliases.values["temporary"])
        assertFailsWith<IllegalArgumentException> { outer.withReferentAliases(mapOf("मान" to identity)) }
        val shadow = ProcedureLocalBindings.create(listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)),
            listOf(number(1)), outer)
        // The parent's named cell is fully hidden here (it has no positional alias).
        assertFailsWith<IllegalArgumentException> { shadow.withReferentAliases(mapOf("temporary" to identity)) }
    }

    @Test fun `visible referents deduplicate aliases and exclude fully shadowed frames`() {
        val parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA),
            PrakriyaParameter("अन्य", PrakriyaValueType.SANKHYA))
        val outer = ProcedureLocalBindings.create(parameters, listOf(number(5), number(5)))
        val original = outer.visibleReferents()
        assertEquals(2, original.size, "Equal payloads are not the same referent.")
        assertNotSame(original[0].identity, original[1].identity)
        val recursive = ProcedureLocalBindings.create(parameters, listOf(number(3), number(4)), outer)
        val visible = recursive.visibleReferents()
        assertEquals(2, visible.size)
        assertTrue(visible.all { it.owner === recursive.scopeIdentity })
        assertTrue(visible.none { candidate -> original.any { it.identity === candidate.identity } })
        val member = ProcedureLocalBindings.member(recursive, "सदस्य", number(1))
        assertEquals(3, member.visibleReferents().size)
        val memberIdentity = member.identity("सदस्य")
        member.accept(mapOf("सदस्य" to number(2)))
        assertEquals(number(2), member.visibleReferents().single { it.identity === memberIdentity }.value)
        assertEquals(listOf(number(5), number(5)), outer.visibleReferents().map { it.value })
    }

    @Test fun `referent metadata comes from declaration morphology not payload shape`() {
        val sentence = PvmScript.parse("मान + सुँ सूची + सुँ इति मान + सुँ ।")
            .filterIsInstance<PvmScriptStatement.Sentence>().single()
        val declaration = assertNotNull(PrakriyaSignatureDeclarationParser.parameterDeclaration(sentence))
        val parameter = assertNotNull(PrakriyaSignatureDeclarationParser.parameter(sentence))
        val collection = SanskritValue.Gana(listOf(number(1), number(2)))
        val locals = ProcedureLocalBindings.create(listOf(parameter), listOf(collection),
            declarations = mapOf(parameter.nameStem to declaration))
        val referent = assertNotNull(locals.referent("मान"))
        assertSame(declaration, referent.declaration)
        assertSame(locals.scopeIdentity, referent.owner)
        assertEquals(setOf(dev.panini.core.Vacana.EKAVACANA), referent.grammaticalNumbers)
        assertEquals(dev.panini.core.Linga.NAPUMSAKA, referent.gender)
        assertEquals(collection, referent.value)
        val positional = assertNotNull(locals.referent(PrakriyaAstArgumentBinder.referenceKey(0)))
        assertSame(referent.identity, positional.identity)
        locals.accept(mapOf("मान" to SanskritValue.Gana(emptyList())))
        val updated = assertNotNull(locals.referent("मान"))
        assertSame(referent.identity, updated.identity)
        assertEquals(referent.grammaticalNumbers, updated.grammaticalNumbers)
        assertEquals(SanskritValue.Gana(emptyList()), updated.value)
        val agreement = ReferentAgreement(setOf(dev.panini.core.Vacana.EKAVACANA), setOf(dev.panini.core.Linga.NAPUMSAKA))
        assertEquals(ReferentAgreementResolution.Selected(referent.identity), locals.resolveReferent(agreement))
        val inner = ProcedureLocalBindings.member(locals, "अन्य", number(3))
        val unknown = inner.referent("अन्य")!!
        assertTrue(unknown.grammaticalNumbers.isEmpty())
        assertNull(unknown.gender)
        assertNull(unknown.declaration)
        assertNotSame(referent.owner, unknown.owner)
        val ambiguous = assertIs<ReferentAgreementResolution.Ambiguous<LocalReferentIdentity>>(inner.resolveReferent(agreement))
        assertEquals(setOf(referent.identity, unknown.identity), ambiguous.candidates.toSet())
    }

    @Test fun `gender evidence preserves lexical alternatives and never leaks through derivation`() {
        fun declared(source: String): LocalReferent {
            val node = dev.panini.vyakaranam.parser.PaniniParser().parse(source).body as dev.panini.vyakaranam.ast.Invocation
            val pada = node.vakya.padas.single() as dev.panini.vyakaranam.ast.SubantaPada
            return ProcedureLocalBindings.member(null, "सदस्य", number(1), pada).referent("सदस्य")!!
        }
        val ambiguous = declared("प्रथम + सुँ ।")
        assertEquals(setOf(dev.panini.core.Linga.PUMS, dev.panini.core.Linga.NAPUMSAKA), ambiguous.grammaticalGenders)
        assertNull(ambiguous.gender)
        assertEquals(setOf(dev.panini.core.Linga.NAPUMSAKA), declared("मूल्य + सुँ ।").grammaticalGenders)
        assertTrue(declared("मूल्य + त्व + सुँ ।").grammaticalGenders.isEmpty())
        assertTrue(declared("अनुपस्थित + सुँ ।").grammaticalGenders.isEmpty())
    }

    @Test fun `referent identity survives assignment aliases and nested shadowing`() {
        val parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA))
        val outer = ProcedureLocalBindings.create(parameters, listOf(number(5)))
        val positional = PrakriyaAstArgumentBinder.referenceKey(0)
        val identity = assertNotNull(outer.identity("मान"))
        assertSame(identity, outer.identity(positional))
        outer.accept(mapOf(positional to number(7)))
        assertSame(identity, outer.identity("मान"))
        assertEquals(number(7), outer.values["मान"])
        val inner = ProcedureLocalBindings.member(outer, "मान", number(3))
        assertNotSame(identity, inner.identity("मान"))
        assertSame(identity, inner.identity(positional))
        inner.accept(mapOf("मान" to number(1)))
        assertEquals(number(7), outer.values["मान"])
        assertSame(identity, outer.identity("मान"))
        val recursiveCall = ProcedureLocalBindings.create(parameters, listOf(number(5)), outer)
        assertNotSame(identity, recursiveCall.identity("मान"))
        assertSame(recursiveCall.identity("मान"), recursiveCall.identity(positional))
        assertNull(inner.identity("अनुपस्थित"))
    }

    @Test fun `duplicate named parameters cannot merge independent positional slots`() {
        val parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA),
            PrakriyaParameter("मान", PrakriyaValueType.SHABDA))
        assertTrue(assertFailsWith<IllegalArgumentException> {
            PrakriyaSignature(parameters = parameters)
        }.message.orEmpty().contains("declared more than once"))
        val parent = ProcedureLocalBindings.member(null, "मान", number(7))
        assertFailsWith<IllegalArgumentException> {
            ProcedureLocalBindings.create(parameters, listOf(number(1), number(2)), parent)
        }
        assertEquals(number(7), parent.values["मान"])
    }

    @Test fun `live environments preserve aliases and inner shadowing`() {
        val local = ProcedureLocalBindings.create(listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)),
            listOf(number(5)))
        val environment = ValueEnvironment.live(local.values)
        val member = ProcedureLocalBindings.member(local, "मान", number(3))
        val inner = environment.mergedWith(ValueEnvironment.live(member.values))
        member.accept(mapOf("मान" to number(1)))
        assertEquals(number(1), inner.values["मान"])
        assertEquals(number(5), environment.values["मान"])
        val positional = PrakriyaAstArgumentBinder.referenceKey(0)
        local.accept(mapOf(positional to number(7)))
        assertEquals(number(7), environment.values["मान"])
        assertEquals(number(7), inner.values[positional])
        assertEquals(number(1), inner.values["मान"])
        assertEquals(number(1), inner.values.toMap()["मान"])
    }

    @Test fun `ordinary value environments retain snapshot merging`() {
        val source = mutableMapOf("मान" to number(1))
        val merged = ValueEnvironment(source).mergedWith(ValueEnvironment())
        source["मान"] = number(2)
        assertEquals(number(1), merged.values["मान"])
    }
}
