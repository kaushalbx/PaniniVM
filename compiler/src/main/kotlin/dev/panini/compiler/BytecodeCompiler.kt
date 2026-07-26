package dev.panini.compiler

import dev.panini.execution.*
import java.io.File

object BytecodeCompiler {

    /**
     * Compiles a PaniniVM script content into JVM bytecode.
     */
    fun compile(scriptContent: String, className: String): ByteArray {
        var conversation = SambhashanaContext(speaker = "प्रयोक्ता", listener = "यन्त्रम्")
        val statementsPlans = mutableListOf<List<ExecutionPlan>>()
        val turnResultIds = mutableListOf<List<String>>()

        scriptContent.lines().forEachIndexed { zeroIdx, lineRaw ->
            val lineIdx = zeroIdx + 1
            val line = lineRaw.trim()
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                return@forEachIndexed
            }

            val input = SanskritUktiInput(text = line, speaker = conversation.speaker, listener = conversation.listener)
            val binding = try {
                VyakaranamExecutionAdapter.bind(input, conversation)
            } catch (e: Exception) {
                throw PaniniCompilationException(lineIdx, line, CompilerErrorKind.MORPHOLOGY_ERROR, e.message ?: "Parsing error")
            }

            val ukti = when (binding) {
                is ExecutionBindingResult.Bound -> binding.ukti
                is ExecutionBindingResult.NeedsInput -> throw PaniniCompilationException(
                    lineIdx, line, CompilerErrorKind.MISSING_INPUT, binding.message
                )
                is ExecutionBindingResult.Invalid -> throw PaniniCompilationException(
                    lineIdx, line, CompilerErrorKind.MORPHOLOGY_ERROR, binding.message
                )
            }

            val program = ExecutionProgram(ukti, ukti.dependencies)
            val historicalValues = conversation.resultHistory.associate { result ->
                result.id to (result.typedValue ?: SanskritValue.of(result.value, result.samjnas))
            }
            val conversationEnvironment = ValueEnvironment.from(
                displayValues = conversation.mentionedEntities + conversation.previousResults,
                samjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas,
                typedValues = historicalValues + conversation.previousTypedResults,
            )

            val planning = ExecutionPlanner.plan(program, conversationEnvironment)
            val plans = when (planning) {
                is PlanningResult.Planned -> planning.plans
                is PlanningResult.Failed -> {
                    val res = planning.result
                    val (kind, msg) = when (res) {
                        is ExecutionResult.NeedsInput -> CompilerErrorKind.MISSING_INPUT to res.message
                        is ExecutionResult.Ambiguous -> CompilerErrorKind.AMBIGUOUS_ACTION to res.message
                        is ExecutionResult.Failure -> {
                            val kind = if (res.error == ExecutionError.DHATU_NOT_EXECUTABLE || res.error == ExecutionError.OPERATION_NOT_FOUND) {
                                CompilerErrorKind.OPERATION_NOT_FOUND
                            } else {
                                CompilerErrorKind.DEPENDENCY_ERROR
                            }
                            kind to res.message
                        }
                        else -> CompilerErrorKind.DEPENDENCY_ERROR to "Compilation error"
                    }
                    throw PaniniCompilationException(lineIdx, line, kind, msg)
                }
            }

            statementsPlans += plans

            // Simulate turn progression to build turn result IDs and resolve references for the next turn
            val nextTurn = conversation.turnNumber + 1
            val stmtResultIds = mutableListOf<String>()
            val remembered = plans.map { plan ->
                val turnId = "उक्ति-${DevanagariDigits.render(nextTurn)}/${plan.invocationId}"
                stmtResultIds += turnId
                
                val mockVal = SanskritValue.Shabda("<${plan.invocationId}>", plan.resolved.operation.resultSamjnas)
                SmrtaPhala(
                    id = turnId,
                    turnNumber = nextTurn,
                    invocationId = plan.invocationId,
                    value = "<${plan.invocationId}>",
                    samjnas = plan.resolved.operation.resultSamjnas,
                    typedValue = mockVal,
                )
            }
            turnResultIds += stmtResultIds

            conversation = conversation.copy(
                previousResults = conversation.previousResults + plans.associate { it.invocationId to "<${it.invocationId}>" },
                previousResultSamjnas = conversation.previousResultSamjnas + plans.associate { it.invocationId to it.resolved.operation.resultSamjnas },
                previousTypedResults = conversation.previousTypedResults + plans.associate { it.invocationId to SanskritValue.Shabda("<${it.invocationId}>", it.resolved.operation.resultSamjnas) },
                resultHistory = conversation.resultHistory + remembered,
                turnNumber = nextTurn,
            )
        }

        return ClassGenerator.generateClass(className, statementsPlans, turnResultIds)
    }

    class PaniniClassLoader(parent: ClassLoader) : ClassLoader(parent) {
        fun loadFromBytes(name: String, bytes: ByteArray): Class<*> {
            return defineClass(name, bytes, 0, bytes.size)
        }
    }

    /**
     * Compiles a PaniniVM script content and returns the loaded JVM class.
     */
    @JvmStatic
    fun compileAndLoad(scriptContent: String, className: String, parentLoader: ClassLoader = this.javaClass.classLoader): Class<*> {
        val bytecode = compile(scriptContent, className)
        val loader = PaniniClassLoader(parentLoader)
        return loader.loadFromBytes(className, bytecode)
    }

    /**
     * Compiles a .pvm file and writes the generated .class file to the specified output folder.
     */
    fun compileFile(file: File, className: String, outputDir: File) {
        require(file.exists()) { "Script file not found: ${file.absolutePath}" }
        val bytecode = compile(file.readText(), className)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val classFile = File(outputDir, "$className.class")
        classFile.writeBytes(bytecode)
        println("Successfully compiled '${file.name}' to ${classFile.absolutePath}")
    }
}
