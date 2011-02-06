/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.resource.java.EnumType;
import org.eclipse.jpt.jpa.core.resource.java.EnumeratedAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentAttribute;

/**
 * <code>javax.persistence.Enumerated</code>
 */
public final class NullEnumeratedAnnotation
	extends NullAnnotation<EnumeratedAnnotation>
	implements EnumeratedAnnotation
{
	protected NullEnumeratedAnnotation(JavaResourcePersistentAttribute parent) {
		super(parent);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	// ***** value
	public EnumType getValue() {
		return null;
	}

	public void setValue(EnumType value) {
		if (value != null) {
			this.addAnnotation().setValue(value);
		}		
	}

	public TextRange getValueTextRange(CompilationUnit astRoot) {
		return null;
	}
}
