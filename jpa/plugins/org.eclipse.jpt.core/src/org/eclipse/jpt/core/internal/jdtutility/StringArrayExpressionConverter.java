/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jdtutility;

import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * Convert an array initializer to/from an array of strings (e.g. {"text0", "text1"}).
 * E is the type of the expressions to be found in the array initializer.
 */
public class StringArrayExpressionConverter<E extends Expression>
	extends AbstractExpressionConverter<ArrayInitializer, String[]>
{
	private final ExpressionConverter<E, String> elementConverter;

	public StringArrayExpressionConverter(ExpressionConverter<E, String> elementConverter) {
		super();
		this.elementConverter = elementConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ArrayInitializer convert_(String[] strings, AST ast) {
		ArrayInitializer arrayInitializer = ast.newArrayInitializer();
		List<Expression> expressions = arrayInitializer.expressions();
		for (String string : strings) {
			expressions.add(this.elementConverter.convert(string, ast));
		}
		return arrayInitializer;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected String[] convert_(ArrayInitializer arrayInitializer) {
		List<E> expressions = arrayInitializer.expressions();
		int len = expressions.size();
		String[] strings = new String[len];
		for (int i = len; i-- > 0; ) {
			strings[i] = this.elementConverter.convert(expressions.get(i));
		}
		return strings;
	}

	public static StringArrayExpressionConverter<StringLiteral> forStringLiterals() {
		return new StringArrayExpressionConverter<StringLiteral>(StringExpressionConverter.instance());
	}

}
