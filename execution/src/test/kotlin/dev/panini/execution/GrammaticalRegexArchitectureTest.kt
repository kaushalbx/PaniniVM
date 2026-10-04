package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GrammaticalRegexArchitectureTest {
    @Test
    fun `shared semantic components do not dispatch on serialized Sanskrit`() {
        val workingDirectory = File(System.getProperty("user.dir"))
        val repository = if (workingDirectory.name == "execution") workingDirectory.parentFile else workingDirectory
        val semanticSources = listOf(
            "execution/src/main/kotlin/dev/panini/execution/NaturalSemanticNormalizer.kt",
            "execution/src/main/kotlin/dev/panini/execution/DirectResultAssignment.kt",
            "execution/src/main/kotlin/dev/panini/execution/TaddhitaStructEngine.kt",
            "execution/src/main/kotlin/dev/panini/execution/binding/KarakaExtractor.kt",
            "compiler/src/main/kotlin/dev/panini/compiler/CompilerFrontend.kt",
        ).map { File(repository, it) }
        val forbiddenDispatch = listOf(
            Regex("""mulaDhatu\s*(?:==|!=|!in\b|in\b)"""),
            Regex("""when\s*\([^)]*mulaDhatu"""),
            Regex("""sourceText\.(?:contains|startsWith|endsWith|substringBefore|substringAfter|split)\("""),
        )

        semanticSources.forEach { file ->
            val source = file.readText()
            forbiddenDispatch.forEach { pattern ->
                assertFalse(pattern.containsMatchIn(source), "${file.path}: ${pattern.pattern}")
            }
        }
    }

    @Test
    fun `grammatical interpretation remains AST based`() {
        val workingDirectory = File(System.getProperty("user.dir"))
        val repository = if (workingDirectory.name == "execution") workingDirectory.parentFile else workingDirectory
        val module = File(repository, "execution")
        val grammarSensitiveFiles = listOf(
            "PrakriyaSignature.kt",
            "DirectResultAssignment.kt",
            "PrakriyaScriptValidator.kt",
            "ItiDeclaration.kt",
        ).map { File(module, "src/main/kotlin/dev/panini/execution/$it") } +
            File(repository, "compiler/src/main/kotlin/dev/panini/compiler/CompilerFrontend.kt")

        grammarSensitiveFiles.forEach { file ->
            val source = file.readText()
            assertFalse("Regex(" in source || ".toRegex(" in source, file.path)
        }
    }

    @Test
    fun `compiler routes lexical semantics through typed predicates`() {
        val workingDirectory = File(System.getProperty("user.dir"))
        val repository = if (workingDirectory.name == "execution") workingDirectory.parentFile else workingDirectory
        val frontend = File(
            repository,
            "compiler/src/main/kotlin/dev/panini/compiler/CompilerFrontend.kt",
        ).readText()

        assertFalse("mulaDhatu.startsWith" in frontend)
        assertFalse(Regex("""pratipadika\.sourceText[^\n]+== "फल"""").containsMatchIn(frontend))
        assertFalse(Regex("""pratipadika\.sourceText[^\n]+== "विजय"""").containsMatchIn(frontend))
        assertTrue("NaturalSemanticNormalizer.isPriorResult" in frontend)
        assertFalse("PaniniParser" in frontend, "Compiler leaves must consume the existing AST.")
        assertFalse("parseOrNull" in frontend, "Compiler leaves must not reparse rendered Sanskrit.")
        assertFalse("PuranaPratyayaResolver" in frontend, "Ordinal procedure semantics belong in shared normalization.")
        assertFalse("MulaPratipadikaIdentity.SAMAVAYA" in frontend, "Collection-parameter meaning belongs in shared normalization.")
        assertFalse(
            "argument.substringBefore('+').trim() == \"फल\"" in frontend,
            "Prior-result arguments must use resolved AST semantics.",
        )
        assertFalse(
            "argument.substringBefore('+').trim()" in frontend,
            "Pipeline arguments must retain parsed nominal identity.",
        )
        assertFalse("CompilerSymbols.stem" in frontend)
        assertTrue("pratipadika.semanticKey()" in frontend)
        assertFalse("source.split('+')" in frontend, "Structured values must consume parsed value padas.")
        assertTrue("TaddhitaStructEngine.assertionValue" in frontend)
        assertFalse("name == \"फल\"" in frontend)
        assertTrue("argument.referenceName" in frontend)
        assertTrue(".isPriorResult" in frontend)
        assertTrue("PvmDiscourseContext.from" in frontend)
        assertFalse("filterIsInstance<PvmScriptStatement.RangeDefinition>()" in frontend)

        val irLowering = File(
            repository,
            "compiler/src/main/kotlin/dev/panini/compiler/CompilerIr.kt",
        ).readText()
        assertTrue("NaturalOperationResolver.resolve" in irLowering)
        assertFalse("bindings[Karaka.APADANA] != null" in irLowering)

        val signatures = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaSignature.kt",
        ).readText()
        assertFalse("text::contains" in signatures)
        assertTrue("TaddhitaVikara" in signatures)
        assertFalse("when (stem.text)" in signatures)
        assertFalse("singleStem() != \"मान\"" in signatures)
        assertFalse("singleStem() != \"परिणाम\"" in signatures)
        assertTrue("MulaPratipadikaIdentity.MANA" in signatures)
        assertTrue("MulaPratipadikaIdentity.PARINAMA" in signatures)

        val karakaExtractor = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/binding/KarakaExtractor.kt",
        ).readText()
        assertFalse("sourceText.contains" in karakaExtractor)
        assertFalse("participant.pada.sourceText" in karakaExtractor)
        assertFalse("?.text in setOf" in karakaExtractor)

        val ordinalResolver = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PuranaPratyayaResolver.kt",
        ).readText()
        assertFalse("sourceText" in ordinalResolver, "Ordinal meaning must come from the typed AST.")
        assertFalse(".split(" in ordinalResolver, "Ordinal morphology must be parsed by the grammar.")

        val normalizer = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/NaturalSemanticNormalizer.kt",
        ).readText()
        assertFalse("referenceName() == \"फल\"" in normalizer)
        assertFalse("pratipadika.sourceText.substringBefore" in normalizer)
        assertTrue("pratipadika.referenceKey()" in normalizer)
        assertFalse("it == \"विजय\"" in normalizer)
        assertTrue("MulaPratipadikaIdentity.PHALA" in normalizer)
        assertTrue("MulaPratipadikaIdentity.VIJAYA" in normalizer)
        assertFalse("tinganta.dhatu.mulaDhatu" in normalizer)
        assertTrue("canonicalDhatuIdentity()" in normalizer)
        assertFalse("sankhyaEvaluator" in normalizer)
        assertFalse("Compatibility form: a non-numeric ablative" in normalizer)

        val signatureCompiler = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaSignature.kt",
        ).readText()
        assertFalse("it.pratyaya == \"त्व\"" in signatureCompiler)
        assertTrue("TaddhitaPratyayaClass.BHAVA" in signatureCompiler)

        val parameterBinder = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaAstArgumentBinder.kt",
        ).readText()
        assertFalse("pada.pratipadika.sourceText" in parameterBinder)
        assertTrue("pada.pratipadika.semanticKey()" in parameterBinder)
        assertFalse("normalizeIdentity(argument)" in parameterBinder)

        val headerIdentity = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaHeaderIdentity.kt",
        ).readText()
        assertFalse("sourceText" in headerIdentity)
        assertTrue("morphologicalKey()" in headerIdentity)

        val projectLoader = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmProjectLoader.kt",
        ).readText()
        assertTrue("prakriya.nameIdentity" in projectLoader)
        assertTrue("prakriya.domainIdentity" in projectLoader)
        assertFalse("derivePrakriyaStem" in projectLoader)

        val resolvedCallFrameSource = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaCallFrame.kt",
        ).readText()
        assertFalse("normalizeIdentity" in resolvedCallFrameSource)
        assertFalse("orderedTerms" in resolvedCallFrameSource)

        val moduleAnalyzer = File(
            repository,
            "compiler/src/main/kotlin/dev/panini/compiler/CompilerModule.kt",
        ).readText()
        assertTrue("definition.prakriya.nameIdentity" in moduleAnalyzer)
        assertTrue("definition.prakriya.domainIdentity" in moduleAnalyzer)
        assertFalse("substringAfter(\"ङस्\"" in moduleAnalyzer)
        assertFalse("internal object CompilerSymbols" in moduleAnalyzer)

        val namedArgumentResolverSource = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/NamedPrakriyaArgumentResolver.kt",
        ).readText()
        assertFalse("normalizeIdentity(pratipadika.sourceText)" in namedArgumentResolverSource)
        assertTrue("pratipadika.semanticKey()" in namedArgumentResolverSource)
        assertTrue("Success.fromPadas(positionalPadas)" in namedArgumentResolverSource)
        assertFalse("PaniniParser" in namedArgumentResolverSource)
        assertFalse("fun resolve(karmaText:" in namedArgumentResolverSource)
        assertFalse("Success.fromTerms" in namedArgumentResolverSource)
        assertTrue("pada = value" in namedArgumentResolverSource)

        val invocationArgumentResolver = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaInvocationArgumentResolver.kt",
        ).readText()
        assertTrue("source.pada === grammaticalPada" in invocationArgumentResolver)

        val invocationMatcher = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaInvocationMatcher.kt",
        ).readText()
        assertFalse("removeSuffix(\" + ल्युट्\")" in invocationMatcher)
        assertFalse("cognateBase" in invocationMatcher)

        val pvmScript = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmScript.kt",
        ).readText()
        assertFalse("?.text != \"सीमा\"" in pvmScript)
        assertTrue("MulaPratipadikaIdentity.SIMA" in pvmScript)
        assertTrue("AdhikaraHeaderParser.parse(stripped)" in pvmScript)
        assertFalse("AdhikaraHeaderParser.domainIdentity(stripped)" in pvmScript)
        assertTrue("parsed?.declarationPadas" in pvmScript)
        assertTrue("PrakriyaHeaderIdentityParser::parse" in pvmScript)

        val definitionMarker = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaDefinitionMarkerParser.kt",
        ).readText()
        assertTrue("val declarationPadas: List<Pada>" in definitionMarker)

        val scriptExecutor = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmScriptExecutor.kt",
        ).readText()
        assertTrue("PvmDiscourseContext.from" in scriptExecutor)
        assertFalse("filterIsInstance<PvmScriptStatement.RangeDefinition>()" in scriptExecutor)

        val directAssignment = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/DirectResultAssignment.kt",
        ).readText()
        assertFalse("it.stem() == \"फल\"" in directAssignment)
        assertTrue("MulaPratipadikaIdentity.PHALA" in directAssignment)

        val expressionBuilder = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/binding/ExpressionBuilder.kt",
        ).readText()
        assertFalse("when (baseText)" in expressionBuilder)
        assertTrue("MulaPratipadikaIdentity.SATYA" in expressionBuilder)
        assertTrue("MulaPratipadikaIdentity.ASATYA" in expressionBuilder)
        assertTrue("SvamRupamEngine.evaluate(normalized.pratipadika)" in expressionBuilder)

        val svamRupamEngine = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/SvamRupamEngine.kt",
        ).readText()
        assertFalse("stripSupSuffix" in svamRupamEngine)
        assertTrue("pratipadika.semanticKey()" in svamRupamEngine)

        val loopExecutor = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmLoopExecutor.kt",
        ).readText()
        assertFalse("baseText() == \"असत्य\"" in loopExecutor)

        val structEngine = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/TaddhitaStructEngine.kt",
        ).readText()
        assertFalse("endsWith(\"परिणाम\")" in structEngine)
        assertFalse("chunked(2)" in structEngine)
        assertFalse("detectStructConstruction" in structEngine)
        assertTrue("detectFieldAssertion" in structEngine)
        assertFalse("verb.dhatu.mulaDhatu" in structEngine)
        assertTrue("CanonicalDhatuIdentity.GRAH" in structEngine)
        assertTrue("MulaPratipadikaIdentity.KSHETRA" in structEngine)
        assertTrue("TingAffix.JHI" in structEngine)
        assertFalse("substringBefore(\" + \")" in structEngine)
        assertFalse("substringBeforeLast" in structEngine)
        assertTrue("pratipadika.semanticKey()" in structEngine)

        val uktiRenderer = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmUktiSadhaka.kt",
        ).readText()
        assertFalse("dhatu.mulaDhatu in setOf" in uktiRenderer)
        assertFalse("rawDhatu in setOf" in uktiRenderer)
        assertTrue("CanonicalDhatuIdentity.CHI" in uktiRenderer)
        assertTrue("CanonicalDhatuIdentity.VRJ" in uktiRenderer)
        assertTrue("KrtAffix.KTVA" in uktiRenderer)
        assertTrue("SanadiAffix.NIC" in uktiRenderer)

        val structuredValueExecutor = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/StructuredValueExecutor.kt",
        ).readText()
        assertFalse("inflectResult" in structuredValueExecutor)
        assertFalse("inflectAttributeValue" in structuredValueExecutor)

        val argumentBinder = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaAstArgumentBinder.kt",
        ).readText()
        assertFalse("sourceText.trim() == \"समवाय\"" in argumentBinder)
        assertTrue("MulaPratipadikaIdentity.SAMAVAYA" in argumentBinder)

        val callFrame = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaCallFrame.kt",
        ).readText()
        assertFalse("substringBefore('+')" in callFrame)
        assertTrue("createResolved" in callFrame)

        val leafPlanner = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/planning/ResolvedLeafPlanner.kt",
        ).readText()
        assertFalse("substringBefore('+')" in leafPlanner)
        assertTrue("pratipadika.semanticKey()" in leafPlanner)

        val guardEvaluator = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/NishedhaGuardEvaluator.kt",
        ).readText()
        assertFalse("substringBefore('+')" in guardEvaluator)
        assertFalse("normalizeIdentity(term)" in guardEvaluator)
        assertFalse("split(\" + \")" in guardEvaluator)
        assertTrue("resolved.argument.pada?.let(NumeralPadaBinder::resolveSemanticValue)" in guardEvaluator)

        val registry = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaRegistry.kt",
        ).readText()
        assertFalse("sourceText.substringBeforeLast" in registry)
        assertTrue("is BhutasamkhyaPada -> terms.joinToString" in registry)
        assertTrue("is SubantaPada -> pratipadika.semanticKey()" in registry)
        assertFalse("is SubantaPada -> pratipadika.sourceText" in registry)
        assertFalse("stripSupSuffix" in registry)
        assertTrue("arguments: List<PrakriyaArgument>" in registry)
        assertFalse("argumentTerms: List<String>" in registry)
        assertTrue("AntaratamaOverloadEngine.matchTypes" in registry)

        val overloadEngine = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/AntaratamaOverloadEngine.kt",
        ).readText()
        assertFalse("classifyTerm" in overloadEngine)
        assertTrue("argumentTypes" in overloadEngine)

        val validator = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PrakriyaScriptValidator.kt",
        ).readText()
        assertFalse("operationStem.substringBefore" in validator)
        assertFalse("nameStem.substringBefore" in validator)

        val renderer = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/PvmUktiSadhaka.kt",
        ).readText()
        assertFalse("pratipadika.sourceText.substringBefore" in renderer)
        assertTrue("(normalized.pratipadika as? MulaPratipadika)?.text" in renderer)

        val ifAction = File(
            repository,
            "actions/src/main/kotlin/dev/panini/actions/control/IfAction.kt",
        ).readText()
        val filterAction = File(
            repository,
            "actions/src/main/kotlin/dev/panini/actions/collection/ListFilterAction.kt",
        ).readText()
        assertFalse("condResult ==" in ifAction)
        assertTrue("condition as? SanskritValue.Satya" in ifAction)
        assertFalse("result.value == \"सत्यम्\"" in filterAction)
        assertTrue("resultTyped as? SanskritValue.Satya" in filterAction)

        assertFalse("pada.form == \"न\"" in frontend)
        assertTrue("AvyayaFunction.NISHEDHA" in frontend)
        assertFalse("it.form == \"न\"" in normalizer)
        assertTrue("AvyayaFunction.NISHEDHA" in normalizer)

        val copularEquality = File(
            repository,
            "actions/src/main/kotlin/dev/panini/actions/comparison/CopularEqualityAction.kt",
        ).readText()
        val copularOrder = File(
            repository,
            "actions/src/main/kotlin/dev/panini/actions/comparison/CopularOrderAction.kt",
        ).readText()
        assertFalse("predicateText" in copularEquality)
        assertFalse("predicateText" in copularOrder)

        val astBuilder = File(
            repository,
            "parser/src/main/kotlin/dev/panini/vyakaranam/parser/VyakaranamAstBuilder.kt",
        ).readText()
        assertFalse("?.text == \"प्रयत्न\"" in astBuilder)
        assertFalse("limitBase!!.text == \"अन्त\"" in astBuilder)
        assertTrue("MulaPratipadikaIdentity.PRAYATNA" in astBuilder)
        assertTrue("MulaPratipadikaIdentity.ANTA" in astBuilder)

        val compiledRuntime = File(
            repository,
            "compiler/src/main/kotlin/dev/panini/compiler/CompiledProgramRuntime.kt",
        ).readText()
        assertFalse("name == \"फल\"" in compiledRuntime)

        assertFalse("setOf(\"फल\", \"LastResult\")" in irLowering)
        assertFalse("InjectedKarmanBinding(\"फल\"" in frontend)

        val expressionBuilderSource = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/binding/ExpressionBuilder.kt",
        ).readText()
        assertTrue("resolvedId ?: PhalaReference.RUNTIME_KEY" in expressionBuilderSource)

        val namedArguments = File(
            repository,
            "execution/src/main/kotlin/dev/panini/execution/NamedPrakriyaArgumentResolver.kt",
        ).readText()
        assertTrue("argument.origin == PrakriyaArgumentOrigin.PIPE" in namedArguments)

        listOf(
            File(repository, "compiler/src/main/kotlin/dev/panini/compiler/CompilerFrontend.kt"),
            File(repository, "execution/src/main/kotlin/dev/panini/execution/PvmSentenceSemantics.kt"),
            File(repository, "execution/src/main/kotlin/dev/panini/execution/PvmSequenceExecutor.kt"),
            File(
                repository,
                "execution/src/main/kotlin/dev/panini/execution/binding/VyakaranamExecutionAdapter.kt",
            ),
        ).forEach { sequenceConsumer ->
            val source = sequenceConsumer.readText()
            assertFalse("connectors.any { it != \"ततः\" }" in source, sequenceConsumer.path)
            assertTrue("SequenceConnector.ANANTARYA" in source, sequenceConsumer.path)
        }

        assertFalse("setOf(\"सत्य\", \"असत्य\")" in leafPlanner)
        assertTrue("MulaPratipadikaIdentity.SATYA" in leafPlanner)
        assertTrue("MulaPratipadikaIdentity.ASATYA" in leafPlanner)
        assertFalse("it.sup.text in setOf" in leafPlanner)
        assertTrue("it.vibhakti() == Vibhakti.DVITIYA" in leafPlanner)
        assertTrue("it.vibhakti() == Vibhakti.TRTIYA" in leafPlanner)
    }
}
