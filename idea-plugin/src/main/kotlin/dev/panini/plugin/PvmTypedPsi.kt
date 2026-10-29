package dev.panini.plugin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.RangeDeclaration
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.SupBearingPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.execution.ResolvedPrakriyaArgument
import dev.panini.execution.PrakriyaParameter

/** Typed editable PSI façades; grammatical state remains solely in the canonical AST. */
class PvmPrakriyaDeclarationPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalDeclaration(): Prakriya? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? Prakriya
}

class PvmPrakriyaHeaderPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalDeclaration(): Prakriya? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? Prakriya
}

class PvmPrakriyaNamePsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalDeclaration(): Prakriya? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? Prakriya
}

class PvmScopeDeclarationPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalScope(): Scope? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? Scope
}

class PvmRangeDeclarationPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalDeclaration(): RangeDeclaration? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? RangeDeclaration
}

class PvmUktiPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalUkti(): Ukti? =
        (containingFile as? PvmFile)?.canonicalItemAt(textOffset) as? Ukti
}

class PvmInvocationPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalInvocation(): Invocation? =
        (containingFile as? PvmFile)?.canonicalInvocationAt(textOffset)
}

class PvmInvocationArgumentPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalArgument(): Pada? =
        (containingFile as? PvmFile)?.canonicalPadaAt(textOffset)

    fun resolvedPrakriyaArgument(): ResolvedPrakriyaArgument? =
        (containingFile as? PvmFile)?.let { PvmPrakriyaCallSemantics.resolvedArgumentAt(it, textOffset) }

    fun resolvedParameterReference(): ResolvedPrakriyaArgument? =
        (containingFile as? PvmFile)?.let {
            PvmPrakriyaCallSemantics.resolvedParameterReferenceAt(it, textOffset)
        }

    fun resolvedBodyParameter(): PrakriyaParameter? =
        (containingFile as? PvmFile)?.let {
            PvmPrakriyaCallSemantics.bodyParameterAt(it, textOffset)
        }

    fun parameterDeclaration(): PsiElement? {
        val file = containingFile as? PvmFile ?: return null
        val (owner, declaration) = PvmPrakriyaCallSemantics.parameterDeclarationAt(file, textOffset)
            ?: return null
        val span = (declaration as SourceLocatedNode).sourceSpan ?: return null
        val leaf = owner.findElementAt(span.start) ?: return null
        return PsiTreeUtil.getParentOfType(leaf, PvmPrakriyaParameterPsi::class.java, false)
            ?: PsiTreeUtil.getParentOfType(leaf, PvmSubantaPsi::class.java, false)
            ?: leaf
    }
}

class PvmPrakriyaParameterPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalParameter(): PrakriyaParameter? =
        (containingFile as? PvmFile)?.let { PvmPrakriyaCallSemantics.declaredParameterAt(it, textOffset) }

    override fun getName(): String? = canonicalParameter()?.nameStem

    override fun setName(name: String): PsiElement {
        val declaration = PsiTreeUtil.getParentOfType(
            this,
            PvmPrakriyaDeclarationPsi::class.java,
            false,
        )
        if (declaration != null && PsiTreeUtil.findChildrenOfType(
                declaration,
                PvmPrakriyaParameterPsi::class.java,
            ).any { it !== this && it.canonicalParameter()?.nameStem == name }
        ) {
            throw IncorrectOperationException("Prakriyā parameter '$name' is already declared")
        }
        return super.setName(name)
    }

    override fun getNameIdentifier(): PsiElement? = PsiTreeUtil.collectElements(this) {
        it.firstChild == null && it.node.elementType in PvmTokenTypes.SEMANTIC_SYMBOLS
    }.firstOrNull()
}

class PvmSubantaPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalPada(): SupBearingPada? =
        (containingFile as? PvmFile)?.canonicalPadaAt(textOffset) as? SupBearingPada
}

class PvmTingantaPsi(node: ASTNode) : PvmPsiElement(node) {
    fun canonicalPada(): TingantaPada? =
        (containingFile as? PvmFile)?.canonicalPadaAt(textOffset) as? TingantaPada
}
