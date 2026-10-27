package dev.panini.plugin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.PsiDocumentManager
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PvmGotoDeclarationFixtureTest : BasePlatformTestCase() {

    fun testRenameChangesOnlyLexicalRootAndKeepsCanonicalGrammar() {
        val library = myFixture.addFileToProject(
            "library.pvm",
            """
            गणित + ङस् युज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        ) as PvmFile
        val main = myFixture.configureByText(
            "main.pvm",
            "पञ्च + अम् द्वि + अम् च गणित + ङस् युज् + णिच् + ल्युट् + टा ततः " +
                "गणित + ङस् युज् + णिच् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।",
        ) as PvmFile
        val declarationOffset = requireNotNull(library.canonicalDocument())
            .prakriyaNameSpans.values.single().start
        val declaration = generateSequence(requireNotNull(library.findElementAt(declarationOffset))) { it.parent }
            .filterIsInstance<PvmPsiElement>()
            .single { it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER }

        myFixture.renameElement(declaration, "गम्")

        assertTrue(library.text.contains("गम् + णिच् + ल्युट् + सुँ"))
        assertTrue(!library.text.contains("युज् + णिच् + ल्युट् + सुँ"))
        assertEquals(2, Regex("गम् \\+ णिच् \\+ ल्युट् \\+ टा").findAll(main.text).count())
        assertNotNull(library.canonicalDocument())
        assertNotNull(main.canonicalDocument())

        val renamedOffset = library.text.indexOf("गम्")
        val renamed = generateSequence(requireNotNull(library.findElementAt(renamedOffset))) { it.parent }
            .filterIsInstance<PvmPsiElement>()
            .single { it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER }
        assertFailsWith<com.intellij.util.IncorrectOperationException> {
            WriteCommandAction.runWriteCommandAction(project) { renamed.setName("गम् + ल्युट्") }
        }
    }

    fun testFormatterNormalizesSeparatorsWithoutChangingGrammar() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            "पञ्च+अम् मुद्र्+णिच्+लोट्+सिप् । द्वि+अम् वद्+लोट्+सिप् ।",
        )
        val beforeKinds = requireNotNull((file as PvmFile).canonicalDocument()).items.map { it::class }

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }

        assertEquals(
            "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ।\nद्वि + अम् वद् + लोट् + सिप् ।",
            file.text,
        )
        val canonical = requireNotNull(file.canonicalDocument())
        assertEquals(beforeKinds, canonical.items.map { it::class })
    }

    fun testPersistentPrakriyaIndexStoresCanonicalIdentityAndDomain() {
        val library = myFixture.addFileToProject(
            "library.pvm",
            """
            गणित + सुँ इति अधिकार + सुँ ।
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val unrelated = myFixture.addFileToProject(
            "unrelated.pvm",
            "मुद्र् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
        )
        val plain = myFixture.addFileToProject(
            "plain.pvm",
            "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        val anchor = myFixture.configureByText(
            "main.pvm",
            "पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा कृ + लोट् + सिप् ।",
        ) as PvmFile

        val files = FileBasedIndex.getInstance().getContainingFiles(
            PvmPrakriyaIndex.NAME,
            PvmPrakriyaIndex.key("गण + ल्युट्", "गणित"),
            GlobalSearchScope.projectScope(project),
        )

        assertTrue(requireNotNull(library.virtualFile) in files)
        assertTrue(requireNotNull(unrelated.virtualFile) !in files)
        val declarationFiles = FileBasedIndex.getInstance().getContainingFiles(
            PvmPrakriyaIndex.NAME,
            PvmPrakriyaIndex.ALL_DECLARATIONS,
            GlobalSearchScope.projectScope(project),
        )
        assertTrue(requireNotNull(library.virtualFile) in declarationFiles)
        assertTrue(requireNotNull(unrelated.virtualFile) in declarationFiles)
        assertTrue(requireNotNull(plain.virtualFile) !in declarationFiles)
        assertTrue("गण + ल्युट्" in PvmProjectFileIndex.knownOperationStems(anchor))
        assertEquals(
            listOf("library.pvm"),
            PvmProjectFileIndex.documentsForIdentity(anchor, "गण + ल्युट्" to "गणित")
                .filter { (file, document) ->
                    document.items.filterIsInstance<dev.panini.vyakaranam.ast.Prakriya>().any {
                        it.nameIdentity == "गण + ल्युट्"
                    } && file != anchor
                }
                .map { it.first.name },
        )
        assertEquals(
            setOf("library.pvm", "unrelated.pvm"),
            PvmProjectFileIndex.declarationDocuments(anchor).mapTo(mutableSetOf()) { it.first.name },
        )
    }

    fun testIncompleteTailPreservesCanonicalPrefixPsi() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            राम +
            """.trimIndent(),
        ) as PvmFile

        assertNull(file.canonicalDocument())
        val topLevelTypes = file.children.map { it.node.elementType }
        assertTrue(PvmTokenTypes.PRAKRIYA_DECLARATION in topLevelTypes)
        assertTrue(PvmTokenTypes.UTTERANCE in topLevelTypes)

        val declaration = file.children.single {
            it.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION
        }
        val header = declaration.children.single {
            it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER
        }
        assertTrue(header.children.any { it.node.elementType == PvmTokenTypes.PRAKRIYA_NAME })

        val recovery = file.children.single { it.node.elementType == PvmTokenTypes.UTTERANCE }
        assertTrue(recovery.text.contains("राम +"))
        val breadcrumbs = PvmBreadcrumbsProvider()
        assertTrue(breadcrumbs.acceptElement(recovery))
        assertTrue(breadcrumbs.getElementInfo(recovery).startsWith("अपूर्णम् "))
    }

    fun testBreadcrumbsUseCanonicalDeclarationAndPadaStructure() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val provider = PvmBreadcrumbsProvider()
        val declaration = file.children.single {
            it.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION
        }
        val header = declaration.children.single {
            it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER
        }
        val name = header.children.single { it.node.elementType == PvmTokenTypes.PRAKRIYA_NAME }
        val statement = declaration.children.single {
            it.node.elementType == PvmTokenTypes.PRAKRIYA_STATEMENT
        }
        val subanta = statement.children.first { it.node.elementType == PvmTokenTypes.SUBANTA }
        val tinganta = statement.children.single { it.node.elementType == PvmTokenTypes.TINGANTA }

        assertTrue(provider.acceptElement(declaration))
        assertEquals("प्रक्रिया गण + ल्युट्", provider.getElementInfo(declaration))
        assertEquals("शीर्षकम् गण + ल्युट्", provider.getElementInfo(header))
        assertEquals("गण + ल्युट्", provider.getElementInfo(name))
        assertTrue(provider.getElementInfo(subanta).startsWith("सुबन्तम् पञ्च + अम्"))
        assertTrue(provider.getElementInfo(tinganta).startsWith("तिङन्तम् युज् + णिच्"))
    }

    fun testParameterInfoUsesCanonicalProjectPrakriyaSignature() {
        myFixture.addFileToProject(
            "library.pvm",
            """
            योजन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ शब्द + सुँ इति मान + सुँ ।
            सूची + सुँ इति परिणाम + सुँ ।
            वाम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val file = myFixture.configureByText(
            "main.pvm",
            "द्वि + अम् राम + अम् च <caret>योजन + टा कृ + लोट् + सिप् ।",
        ) as PvmFile
        val handler = PvmParameterInfoHandler()

        val signatures = handler.signaturesAt(file, myFixture.editor.caretModel.offset)

        assertEquals(1, signatures.size)
        assertEquals(
            "वाम: सङ्ख्या, दक्षिण: शब्द → सूची",
            handler.renderSignature(signatures.single()),
        )
        assertEquals(0, handler.activeParameterAt(file, file.text.indexOf("द्वि")))
        assertEquals(1, handler.activeParameterAt(file, file.text.indexOf("राम")))
        assertNull(handler.activeParameterAt(file, file.text.indexOf("योजन")))
        assertEquals(1, handler.signaturesAt(file, file.text.indexOf("राम")).size)
    }

    fun testPrakriyaNameOwnershipAndUseScopeFollowCanonicalVisibility() {
        val publicFile = myFixture.addFileToProject(
            "public.pvm",
            """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        ) as PvmFile
        val internalFile = myFixture.configureByText(
            "internal.pvm",
            """
            गणित + ङस् गम् + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        ) as PvmFile

        fun declarationHeader(file: PvmFile): PvmPsiElement {
            val declaration = file.children.single {
                it.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION
            }
            return declaration.children.single {
                it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER
            } as PvmPsiElement
        }

        val publicHeader = declarationHeader(publicFile)
        val internalHeader = declarationHeader(internalFile)
        val publicName = requireNotNull(publicHeader.nameIdentifier)
        val internalName = requireNotNull(internalHeader.nameIdentifier)

        assertEquals(PvmTokenTypes.PRAKRIYA_NAME, publicName.node.elementType)
        assertEquals("गण + ल्युट् + सुँ", publicName.text.trim())
        assertEquals(PvmTokenTypes.PRAKRIYA_NAME, internalName.node.elementType)
        assertEquals("गम् + ल्युट् + सुँ", internalName.text.trim())

        assertTrue(publicHeader.useScope is GlobalSearchScope)
        assertTrue(publicHeader.useScope.contains(requireNotNull(publicFile.virtualFile)))
        assertTrue(publicHeader.useScope.contains(requireNotNull(internalFile.virtualFile)))
        assertTrue(internalHeader.useScope is LocalSearchScope)
        assertTrue(internalHeader.useScope.contains(requireNotNull(internalFile.virtualFile)))
        assertTrue(!internalHeader.useScope.contains(requireNotNull(publicFile.virtualFile)))
    }

    fun testPsiProjectsCanonicalTopLevelAndPrakriyaBodyStructure() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            """
            गणित + सुँ इति अधिकार + सुँ ।
            युज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् द्वितीय + टा युज् + णिच् + लोट् + सिप् ॥
            एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            """.trimIndent(),
        )

        val topLevelTypes = file.children.map { it.node.elementType }
        assertTrue(PvmTokenTypes.SCOPE_DECLARATION in topLevelTypes)
        assertTrue(PvmTokenTypes.PRAKRIYA_DECLARATION in topLevelTypes)
        assertTrue(PvmTokenTypes.RANGE_DECLARATION in topLevelTypes)

        val prakriya = file.children.single { it.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION }
        val header = prakriya.children.single { it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER }
        val name = header.children.single { it.node.elementType == PvmTokenTypes.PRAKRIYA_NAME }
        assertTrue(name.text.contains("युज्"))

        val statement = prakriya.children.single { it.node.elementType == PvmTokenTypes.PRAKRIYA_STATEMENT }
        val grammaticalNodes = statement.children.filter {
            it.node.elementType == PvmTokenTypes.SUBANTA || it.node.elementType == PvmTokenTypes.TINGANTA
        }
        assertEquals(2, grammaticalNodes.count { it.node.elementType == PvmTokenTypes.SUBANTA })
        assertEquals(1, grammaticalNodes.count { it.node.elementType == PvmTokenTypes.TINGANTA })
        assertEquals("युज् + णिच् + लोट् + सिप्", grammaticalNodes.last().text.trim())

        val scope = file.children.single { it.node.elementType == PvmTokenTypes.SCOPE_DECLARATION }
        val domain = scope.children.single { it.node.elementType == PvmTokenTypes.SCOPE_DOMAIN }
        assertEquals("गणित + सुँ", domain.text.trim())
        assertNotNull((file as PvmFile).canonicalItemAt(prakriya.textRange.startOffset))
    }

    fun testCanonicalPrakriyaReferenceResolvesThroughPsiReferenceApi() {
        myFixture.addFileToProject(
            "library.pvm",
            """
            गणित + सुँ इति अधिकार + सुँ ।
            युज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् द्वितीय + टा युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        myFixture.configureByText(
            PvmFileType.INSTANCE,
            "पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>युज् + णिच् + ल्युट् + टा ततः गणित + ङस् युज् + णिच् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।",
        )

        val caretOffset = myFixture.editor.caretModel.offset
        val leaf = requireNotNull(myFixture.file.findElementAt(caretOffset))
        assertEquals(PvmTokenTypes.DHATU, leaf.node.elementType)
        assertEquals(PvmLanguage.INSTANCE, leaf.language)
        val current = myFixture.file as PvmFile
        val documents = PvmProjectFileIndex.documents(current)
        val knownNames = documents.flatMapTo(mutableSetOf()) { (_, document) ->
            document.items.filterIsInstance<dev.panini.vyakaranam.ast.Prakriya>()
                .map(dev.panini.vyakaranam.ast.Prakriya::nameIdentity)
        }
        val canonicalIdentity = requireNotNull(PvmSymbolResolver.prakriyaReferenceIdentityAt(
            requireNotNull(current.canonicalDocument()), caretOffset, knownNames,
        )) { "Canonical resolver did not recognize '$knownNames' at $caretOffset" }
        assertEquals("युज् + णिच् + ल्युट्" to "गणित", canonicalIdentity)
        val reference = myFixture.getReferenceAtCaretPositionWithAssertion()
        val targets = (reference as PsiPolyVariantReference).multiResolve(false).mapNotNull { it.element }
        val target = targets.singleOrNull { it.containingFile.name == "library.pvm" }
            ?: error("Resolved targets: ${targets.map { it.containingFile.name to it.text }}")
        assertEquals("library.pvm", target.containingFile.name)
        assertEquals("युज्", (target as PsiNamedElement).name)
    }

    fun testNonOperationTokenInPipelineIsNotAPrakriyaReference() {
        myFixture.addFileToProject(
            "library.pvm",
            "युज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\nप्रथम + अम् द्वितीय + टा युज् + णिच् + लोट् + सिप् ॥",
        )
        myFixture.configureByText(
            PvmFileType.INSTANCE,
            "पञ्च + अम् द्वि + अम् च युज् + <caret>ल्युट् + टा ततः युज् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।",
        )

        assertNull(myFixture.file.findReferenceAt(myFixture.editor.caretModel.offset))
    }

    fun testFindUsagesUsesCanonicalPrakriyaIdentityAndPsiReferences() {
        val library = myFixture.addFileToProject(
            "library.pvm",
            """
            गणित + सुँ इति अधिकार + सुँ ।
            युज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् द्वितीय + टा युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        ) as PvmFile
        val main = myFixture.addFileToProject(
            "main.pvm",
            "पञ्च + अम् द्वि + अम् च गणित + ङस् युज् + णिच् + ल्युट् + टा ततः गणित + ङस् युज् + णिच् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।",
        ) as PvmFile
        myFixture.configureFromExistingVirtualFile(requireNotNull(main.virtualFile))

        val declarationOffset = requireNotNull(library.canonicalDocument())
            .prakriyaNameSpans.values.single().start
        val declarationLeaf = requireNotNull(library.findElementAt(declarationOffset))
        val declaration = generateSequence(declarationLeaf) { it.parent }
            .filterIsInstance<PvmPsiElement>()
            .single { it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER }
        val provider = PvmFindUsagesProvider()
        assertTrue(provider.canFindUsagesFor(declaration))
        assertEquals("prakriyā declaration", provider.getType(declaration))
        assertEquals("गणित / युज् + णिच् + ल्युट्", provider.getDescriptiveName(declaration))

        val usages = ReferencesSearch.search(declaration, LocalSearchScope(main), false).findAll()
        assertEquals(2, usages.size)
        assertTrue(usages.all { it.element.containingFile == myFixture.file })
    }

    fun testCanonicalDiagnosticWarningRangeAndQuickFixReachEditor() {
        myFixture.configureByText(
            PvmFileType.INSTANCE,
            "<caret>पञ्च + शस् दश + अम् च मुद्र् + लोट् + सिप् ।",
        )

        val warnings = myFixture.doHighlighting().filter {
            it.severity == HighlightSeverity.WARNING &&
                it.description?.contains("canonical numeral stem") == true
        }
        assertEquals(2, warnings.size)
        assertEquals(setOf("पञ्च", "दश"), warnings.map {
            myFixture.file.text.substring(it.startOffset, it.endOffset)
        }.toSet())

        val fix = requireNotNull(myFixture.findSingleIntention("Replace with 'पञ्चन्'"))
        myFixture.launchAction(fix)
        assertTrue(myFixture.file.text.startsWith("पञ्चन् + शस्"))
        assertTrue(myFixture.doHighlighting().none {
            it.description?.contains("canonical numeral stem 'पञ्चन्'") == true
        })
    }

    fun testCanonicalSemanticErrorPreservesParserSpanInEditor() {
        val source = "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
            "तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + भिस् " +
            "युज् + णिच् + लोट् + सिप् ।"
        myFixture.configureByText(PvmFileType.INSTANCE, source)

        val error = myFixture.doHighlighting().single {
            it.severity == HighlightSeverity.ERROR &&
                it.description?.contains("instrumental", ignoreCase = true) == true
        }
        assertEquals(
            "सङ्ख्या+भिस्",
            source.substring(error.startOffset, error.endOffset).filterNot(Char::isWhitespace),
        )
    }

    fun testCompletionUsesCanonicalNumeralStemsFromCore() {
        myFixture.configureByText(PvmFileType.INSTANCE, "पञ्<caret>")

        myFixture.completeBasic()
        val panchanSuggestions = myFixture.lookupElementStrings.orEmpty()
        assertTrue("पञ्चन्" in panchanSuggestions)
        assertTrue("पञ्च" !in panchanSuggestions)

        myFixture.configureByText(PvmFileType.INSTANCE, "को<caret>")
        myFixture.completeBasic()
        assertTrue("कोटि" in myFixture.lookupElementStrings.orEmpty())
    }

    fun testCompletionKeepsValueCatalogsOutOfSuffixPosition() {
        myFixture.configureByText(PvmFileType.INSTANCE, "भू + त<caret>")

        myFixture.completeBasic()
        val suggestions = myFixture.lookupElementStrings.orEmpty()
        assertTrue("तिप्" in suggestions)
        assertTrue("त्रि" !in suggestions)
        assertTrue("तर्हि" !in suggestions)
        assertTrue("तृच्" in suggestions)
    }

    fun testCompletionUsesCanonicalVikaranaAndStriAffixCatalogs() {
        myFixture.configureByText(PvmFileType.INSTANCE, "भू + श्<caret>")

        myFixture.completeBasic()
        val verbalSuggestions = myFixture.lookupElementStrings.orEmpty()
        assertTrue("शप्" in verbalSuggestions)
        assertTrue("श्यन्" in verbalSuggestions)

        myFixture.configureByText(PvmFileType.INSTANCE, "संख्या + ट<caret>")
        myFixture.completeBasic()
        assertTrue("टाप्" in myFixture.lookupElementStrings.orEmpty())
    }

    fun testCompletionAfterUpasargaOffersDhatusInsteadOfAffixes() {
        myFixture.configureByText(PvmFileType.INSTANCE, "वि + भ<caret>")

        myFixture.completeBasic()
        val suggestions = myFixture.lookupElementStrings.orEmpty()
        assertTrue("भज्" in suggestions)
        assertTrue("भिस्" !in suggestions)
    }
    fun testPipelineStageNavigatesThroughEditorPsiToCanonicalDeclaration() {
        val source = """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val file = myFixture.configureByText(PvmFileType.INSTANCE, source)
        val offset = myFixture.editor.caretModel.offset
        val sourceElement = requireNotNull(file.findElementAt(offset))

        val targets = requireNotNull(
            PvmGotoDeclarationHandler().getGotoDeclarationTargets(sourceElement, offset, myFixture.editor),
        )

        assertEquals(1, targets.size)
        assertEquals(file.text.indexOf("गण + ल्युट्"), targets.single().textOffset)
    }

    fun testCanonicalSemanticCategoriesProduceDistinctEditorHighlights() {
        val source = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            राम + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        myFixture.configureByText(PvmFileType.INSTANCE, source)

        val semanticKeys = myFixture.doHighlighting().mapNotNull { it.forcedTextAttributesKey }.toSet()

        assertTrue(PvmSyntaxHighlighter.PRAKRIYA_DECLARATION in semanticKeys)
        assertTrue(PvmSyntaxHighlighter.PRAKRIYA_REFERENCE in semanticKeys)
        assertTrue(PvmSyntaxHighlighter.DOMAIN_DECLARATION in semanticKeys)
        assertTrue(PvmSyntaxHighlighter.DOMAIN_REFERENCE in semanticKeys)
        assertTrue(PvmSyntaxHighlighter.NOMINAL_STEM in semanticKeys, semanticKeys.toString())
    }

    fun testPipelineStageNavigatesToPublicDeclarationInProjectFile() {
        myFixture.addFileToProject(
            "library.pvm",
            """
                गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।"
        val file = myFixture.configureByText("main.pvm", source)
        val offset = myFixture.editor.caretModel.offset
        val sourceElement = requireNotNull(file.findElementAt(offset))

        val targets = requireNotNull(
            PvmGotoDeclarationHandler().getGotoDeclarationTargets(sourceElement, offset, myFixture.editor),
        )

        assertEquals(listOf("library.pvm"), targets.map { it.containingFile.name })
    }

    fun testProjectNavigationDoesNotExposeInternalDeclarationFromAnotherFile() {
        myFixture.addFileToProject(
            "private.pvm",
            """
                गणित + ङस् गण + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।"
        val file = myFixture.configureByText("main.pvm", source)
        val offset = myFixture.editor.caretModel.offset
        val sourceElement = requireNotNull(file.findElementAt(offset))

        val targets = PvmGotoDeclarationHandler().getGotoDeclarationTargets(sourceElement, offset, myFixture.editor)

        assertNull(targets)
    }

    fun testOrdinaryPrakriyaInvocationUsesRuntimeIdentityForProjectNavigation() {
        myFixture.addFileToProject(
            "library.pvm",
            """
                गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>गण + ल्युट् + टा कृ + लोट् + सिप् ।"
        val file = myFixture.configureByText("main.pvm", source)
        val offset = myFixture.editor.caretModel.offset
        val sourceElement = requireNotNull(file.findElementAt(offset))

        val targets = requireNotNull(
            PvmGotoDeclarationHandler().getGotoDeclarationTargets(sourceElement, offset, myFixture.editor),
        )

        assertEquals(listOf("library.pvm"), targets.map { it.containingFile.name })
    }

    fun testCompletionOffersPublicProjectPrakriyaAndHidesForeignInternalOne() {
        myFixture.addFileToProject(
            "library.pvm",
            """
                गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "private.pvm",
            """
                गणित + ङस् गम् + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        myFixture.configureByText("main.pvm", "ग<caret>")

        myFixture.completeBasic()
        val suggestions = myFixture.lookupElementStrings.orEmpty()

        assertTrue("गण + ल्युट्" in suggestions)
        assertTrue("गम् + ल्युट्" !in suggestions)
    }

    fun testQuickDocumentationUsesCanonicalProjectPrakriyaAndSignature() {
        myFixture.addFileToProject(
            "library.pvm",
            """
                गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
                पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् <caret>गण + ल्युट् + टा कृ + लोट् + सिप् ।"
        val file = myFixture.configureByText("main.pvm", source)
        val target = requireNotNull(file.findElementAt(myFixture.editor.caretModel.offset))

        val documentation = requireNotNull(PvmDocumentationProvider().generateDoc(target, target))

        assertTrue(documentation.contains("Project Prakriyā: गण + ल्युट्"))
        assertTrue(documentation.contains("library.pvm"))
        assertTrue(documentation.contains("Domain:</b> गणित"))
        assertTrue(documentation.contains("Visibility:</b> public"))
        assertTrue(documentation.contains("Body statements:</b> 1"))
        assertTrue(documentation.contains("Parameters:</b>"))
        assertTrue(documentation.contains("Result:</b>"))
    }

    fun testQuickDocumentationUsesCanonicalSubantaMorphologyAndLinga() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            "<caret>सूची + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        val target = requireNotNull(file.findElementAt(myFixture.editor.caretModel.offset))

        val documentation = requireNotNull(PvmDocumentationProvider().generateDoc(target, target))

        assertTrue(documentation.contains("Subanta:"))
        assertTrue(documentation.contains("Prātipadika:</b> <code>सूची</code>"))
        assertTrue(documentation.contains("dvitiya ekavacana"))
        assertTrue(documentation.contains("Liṅga:</b> stri"))
    }

    fun testQuickDocumentationUsesCanonicalTingantaAndDhatupatha() {
        val file = myFixture.configureByText(
            PvmFileType.INSTANCE,
            "सूची + अम् <caret>मुद्र् + णिच् + लोट् + सिप् ।",
        )
        val target = requireNotNull(file.findElementAt(myFixture.editor.caretModel.offset))

        val documentation = requireNotNull(PvmDocumentationProvider().generateDoc(target, target))

        assertTrue(documentation.contains("Tiṅanta:"))
        assertTrue(documentation.contains("Dhātupāṭha:"))
        assertTrue(documentation.contains("curadi gaṇa"))
        assertTrue(documentation.contains("Sanādi:</b> णिच्"))
        assertTrue(documentation.contains("Lakāra:</b> लोट्"))
        assertTrue(documentation.contains("Tiṅ:</b> सिप्"))
    }

    fun testProjectDocumentCacheInvalidatesAfterPsiEdit() {
        val library = myFixture.addFileToProject(
            "library.pvm",
            "युज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
        ) as PvmFile
        val anchor = myFixture.configureByText("main.pvm", "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ।") as PvmFile

        fun projectNames() = PvmProjectFileIndex.documents(anchor)
            .flatMap { (_, document) -> document.items.filterIsInstance<dev.panini.vyakaranam.ast.Prakriya>() }
            .mapTo(mutableSetOf(), dev.panini.vyakaranam.ast.Prakriya::nameIdentity)

        assertTrue("युज् + ल्युट्" in projectNames())

        val document = requireNotNull(library.viewProvider.document)
        WriteCommandAction.runWriteCommandAction(project) {
            document.setText(
                "गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                    "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
            )
        }
        PsiDocumentManager.getInstance(project).commitDocument(document)

        val updatedNames = projectNames()
        assertTrue("गण + ल्युट्" in updatedNames)
        assertTrue("युज् + ल्युट्" !in updatedNames)
    }

    fun testProjectSemanticDiagnosticTracksUnresolvedAndResolvedPrakriya() {
        val call = "पञ्चन् + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा " +
            "ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।"
        myFixture.configureByText("main.pvm", call)

        val unresolved = myFixture.doHighlighting().filter {
            it.severity == HighlightSeverity.ERROR &&
                it.description?.contains("Unresolved PaniniVM prakriyā") == true
        }
        assertEquals(2, unresolved.size, myFixture.doHighlighting().joinToString { "${it.severity}:${it.description}" })
        assertTrue(unresolved.all {
            call.substring(it.startOffset, it.endOffset) == "गण + ल्युट्"
        })

        myFixture.addFileToProject(
            "library.pvm",
            "गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "पञ्च + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
        )
        myFixture.configureByText("main.pvm", call)

        assertTrue(myFixture.doHighlighting().none {
            it.severity == HighlightSeverity.ERROR &&
                it.description?.contains("Unresolved PaniniVM prakriyā") == true
        })
    }
}
