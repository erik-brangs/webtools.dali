/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.utility.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.utility.jdt.AnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.BooleanAnnotationAdapter;

/**
 * Wrap an annotation adapter and return true if the annotation is simply
 * present; return false if it is missing. The annotation can be of any type
 * (marker, single member, or normal).
 */
public class SimpleBooleanAnnotationAdapter implements BooleanAnnotationAdapter {
	private final AnnotationAdapter adapter;

	public SimpleBooleanAnnotationAdapter(AnnotationAdapter adapter) {
		super();
		this.adapter = adapter;
	}


	// ********** BooleanAnnotationAdapter implementation **********

	public ASTNode astNode() {
		return this.adapter.astNode();
	}

	public ASTNode astNode(CompilationUnit astRoot) {
		return this.adapter.astNode(astRoot);
	}

	public Annotation annotation() {
		return this.adapter.annotation();
	}

	public Annotation annotation(CompilationUnit astRoot) {
		return this.adapter.annotation(astRoot);
	}

	public boolean value() {
		return this.getValue(this.annotation());
	}

	public boolean value(CompilationUnit astRoot) {
		return this.getValue(this.annotation(astRoot));
	}

	public void setValue(boolean value) {
		this.setValue(this.value(), value);
	}


	// ********** internal methods **********

	protected boolean getValue(Annotation annotation) {
		return annotation != null;
	}

	/**
	 * set the adapter's value to the specified new value if it
	 * is different from the specified old value
	 */
	protected void setValue(boolean oldValue, boolean newValue) {
		if (newValue != oldValue) {
			this.setNewValue(newValue);
		}
	}

	protected void setNewValue(boolean value) {
		if (value) {
			this.adapter.newMarkerAnnotation();
		} else {
			this.adapter.removeAnnotation();
		}
	}

}
