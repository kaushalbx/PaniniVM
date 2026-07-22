package dev.panini.execution

import dev.panini.core.Karaka

/** Built-in executable meanings, kept outside the grammatical Dhātupāṭha catalogue. */
internal object BuiltInOperationRegistrations {
    val all: Map<String, List<DhatuOperation>> = mapOf(
        "01.1153" to listOf(
                DhatuOperation(
                    id = "सङ्ख्याभागः",
                    description = "सङ्ख्यानां भागः त्रैराशिकं वा (Fraction/Proportion)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritFractionAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                )
            ),
        "01.1143" to listOf(
                DhatuOperation(
                    id = "मूल्यदर्शनम्",
                    description = "मूल्यस्य दर्शनम् / प्रेक्षणम् (Variable Inspection / Query)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    action = SanskritVariableInspectAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                )
            ),
        "01.1046" to listOf(
                DhatuOperation(
                    id = "सङ्ख्याहरणम्",
                    description = "सङ्ख्यानां हरणम् (विभाजनम्)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritDivisionAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                )
            ),
        "01.0607" to listOf(
                DhatuOperation(
                    id = "सङ्ख्यामूलम्",
                    description = "सङ्ख्यायाः वर्गमूलम् (Square Root)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritSquareRootAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                )
            ),
        "01.0601" to listOf(
                DhatuOperation(
                    id = "स्मृतिरक्षणम्",
                    description = "Saves active context state to persistent storage.",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    effects = setOf(ExecutionEffect.WRITE_RESOURCE),
                    action = SmritiSaveAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                ),
                DhatuOperation(
                    id = "स्मृतिपुनर्प्राप्तिः",
                    description = "Restores context state from persistent storage.",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    effects = setOf(ExecutionEffect.READ_RESOURCE),
                    action = SmritiLoadAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                ),
            ),
        "01.0863" to listOf(
                DhatuOperation(
                    id = "सङ्ख्याघातः",
                    description = "सङ्ख्यायाः घातवर्धनम् (Exponentiation)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritExponentiationAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                )
            ),
        "10.0391" to listOf(
                DhatuOperation(
                    id = "सङ्ख्यागुणनम्",
                    description = "सङ्ख्यानां गुणनम्",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritMultiplicationAction,
                    trigger = OperationTrigger(forbiddenUpasargas = setOf("सम्", "सम")),
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
                DhatuOperation(
                    id = "सङ्ख्यागणनम्",
                    description = "पदार्थानां / सङ्ख्यानां गणनम् (सङ्ख्यानम्)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                                shape = ExpressionShape.COORDINATION,
                            )
                        )
                    ),
                    action = SanskritCountingAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
                DhatuOperation(
                    id = "सङ्ख्यासाम्यम्",
                    description = "सङ्ख्यानां साम्यम् (माध्यमम् / Average)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritAverageAction,
                    trigger = OperationTrigger(requiredUpasargas = setOf("सम्")),
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
            ),
        "10.0509" to listOf(
                DhatuOperation(
                    id = "बाह्यप्रेषणम्",
                    description = "Dispatches command or message to external system capability.",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    effects = setOf(ExecutionEffect.NETWORK, ExecutionEffect.EXECUTE_PROCESS, ExecutionEffect.SEND_MESSAGE),
                    action = BahyaSendAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                )
            ),
        "03.0010" to listOf(
                DhatuOperation(
                    id = "मूल्यदानम्",
                    description = "मूल्यस्य दानम् / संविभाजनम् (Variable Assignment)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    action = SanskritVariableAssignAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                )
            ),
        "07.0014" to listOf(
                DhatuOperation(
                    id = "सङ्ख्याशेषः",
                    description = "सङ्ख्याविभाजनात् शेषः (Modulo)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritModuloAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                )
            ),
        "07.0013" to listOf(
                DhatuOperation(
                    id = "सङ्ख्यातुलना",
                    description = "सङ्ख्यानां विचारः तुलना च (Comparison / Max)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritComparisonAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
                DhatuOperation(
                    id = "सङ्ख्यान्यूनत्वम्",
                    description = "सङ्ख्यानां न्यूनत्वम् (Minimum)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                                shape = ExpressionShape.COORDINATION,
                                memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                            )
                        )
                    ),
                    action = SanskritMinAction,
                    resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
            ),
        "08.0010" to listOf(
                DhatuOperation(
                    id = "संहिताकरणम्",
                    description = "पदानां संहिताकरणम् (सन्धियोगः)",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 2,
                                shape = ExpressionShape.COORDINATION,
                            )
                        )
                    ),
                    action = SanskritSandhiAction,
                    trigger = OperationTrigger(requiredAvyayas = setOf("इति")),
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                ),
                DhatuOperation(
                    id = "पदनिष्पत्तिः",
                    description = "प्रातिपदिकस्य सुबन्तरूपसिद्धिः",
                    signature = OperationSignature(
                        requirements = listOf(
                            KarakaRequirement(
                                karaka = Karaka.KARMAN,
                                minimumMembers = 1,
                            )
                        )
                    ),
                    action = SanskritSubantaDerivationAction,
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                ),
            )
    )
}
