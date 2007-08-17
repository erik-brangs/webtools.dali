/*******************************************************************************
 * Copyright (c) 2005, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jdtutility;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jpt.core.internal.JptCorePlugin;

public class JDTTools {

	// TODO get rid of the "lightweight" methods after reworking how
	// ValidationMessages determine line numbers
	/**
	 * Build an AST for the specified member's compilation unit or
	 * (source-attached) class file. Build the AST without its bindings
	 * resolved.
	 */
	public static CompilationUnit buildLightweightASTRoot(IMember member) {
		return buildASTRoot(member, false);
	}

	/**
	 * Build an AST for the specified member's compilation unit or
	 * (source-attached) class file. Build the AST with its bindings
	 * resolved (and the resultant performance hit).
	 */
	public static CompilationUnit buildASTRoot(IMember member) {
		return buildASTRoot(member, true);
	}

	/**
	 * Build an AST for the specified member's compilation unit or
	 * (source-attached) class file.
	 */
	private static CompilationUnit buildASTRoot(IMember member, boolean resolveBindings) {
		return (member.isBinary()) ?
			buildASTRoot(member.getClassFile(), resolveBindings)  // the class file must have a source attachment
		:
			buildASTRoot(member.getCompilationUnit(), resolveBindings);
	}
	
	public static CompilationUnit buildASTRoot(IClassFile classFile) {
		return buildASTRoot(classFile, true);
	}
	
	private static CompilationUnit buildASTRoot(IClassFile classFile, boolean resolveBindings) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(classFile);
		parser.setResolveBindings(resolveBindings);
		return (CompilationUnit) parser.createAST(null);
	}
	
	public static CompilationUnit buildASTRoot(ICompilationUnit compilationUnit) {
		return buildASTRoot(compilationUnit, true);
	}
	
	private static CompilationUnit buildASTRoot(ICompilationUnit compilationUnit, boolean resolveBindings) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);
		parser.setResolveBindings(resolveBindings);
		return (CompilationUnit) parser.createAST(null);
	}
	
	public static IType findType(String packageName, String qualifiedTypeName, IJavaProject javaProject) {
		try {
			return javaProject.findType(packageName, qualifiedTypeName.replace('$', '.'));
		} catch (JavaModelException ex) {
			JptCorePlugin.log(ex);
			return null;
		}
	}

	public static String resolveEnum(Expression expression) {
		if (expression == null) {
			return null;
		}
		switch (expression.getNodeType()) {
			case ASTNode.QUALIFIED_NAME:
			case ASTNode.SIMPLE_NAME:
				return resolveEnum((Name) expression);
			default:
				return null;
		}
	}

	public static String resolveEnum(Name enumExpression) {
		IBinding binding = enumExpression.resolveBinding();
		if (binding == null) {
			return null;  // TODO figure why this is null sometimes
		}
		if (binding.getKind() != IBinding.VARIABLE) {
			return null;
		}
		IVariableBinding variableBinding = (IVariableBinding) binding;
		return variableBinding.getType().getQualifiedName() + "." + variableBinding.getName();
	}

}
