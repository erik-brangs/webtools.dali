/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.resource.java.source;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.resource.java.source.SourceColumnAnnotation;
import org.eclipse.jpt.core.internal.utility.jdt.MemberAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.NestedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.resource.java.ColumnAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.Member;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.NullEclipseLinkWriteTransformerColumnAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkWriteTransformerAnnotation;

/**
 * org.eclipse.persistence.annotations.WriteTransformer
 */
public final class SourceEclipseLinkWriteTransformerAnnotation
	extends SourceEclipseLinkTransformerAnnotation
	implements EclipseLinkWriteTransformerAnnotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final MemberAnnotationAdapter columnAdapter;
	private ColumnAnnotation column;


	public SourceEclipseLinkWriteTransformerAnnotation(JavaResourcePersistentAttribute parent, Attribute attribute) {
		super(parent, attribute, DECLARATION_ANNOTATION_ADAPTER);
		this.columnAdapter = new MemberAnnotationAdapter(this.member, buildColumnAnnotationAdapter(this.daa));
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	public void initialize(CompilationUnit astRoot) {
		super.initialize(astRoot);
		if (this.columnAdapter.getAnnotation(astRoot) != null) {
			this.column = createColumn(this, this.member, this.daa);
			this.column.initialize(astRoot);
		}
	}

	@Override
	public void update(CompilationUnit astRoot) {
		super.update(astRoot);
		if (this.columnAdapter.getAnnotation(astRoot) == null) {
			this.setColumn(null);
		} else {
			if (this.column == null) {
				ColumnAnnotation col = createColumn(this, this.member, this.daa);
				col.initialize(astRoot);
				this.setColumn(col);
			} else {
				this.column.update(astRoot);
			}
		}
	}


	// ********** SourceTransformerAnnotation implementation **********

	@Override
	String getTransformerClassElementName() {
		return EclipseLink.WRITE_TRANSFORMER__TRANSFORMER_CLASS;
	}

	@Override
	String getMethodElementName() {
		return EclipseLink.WRITE_TRANSFORMER__METHOD;
	}


	// ********** WriteTransformerAnnotation implementation **********

	// ***** column
	public ColumnAnnotation getColumn() {
		return this.column;
	}

	public ColumnAnnotation getNonNullColumn() {
		return (this.column != null) ? this.column : new NullEclipseLinkWriteTransformerColumnAnnotation(this);
	}

	public ColumnAnnotation addColumn() {
		ColumnAnnotation col = createColumn(this, this.member, this.daa);
		col.newAnnotation();
		this.setColumn(col);
		return col;
	}

	public void removeColumn() {
		this.column.removeAnnotation();
		this.setColumn(null);
	}

	private void setColumn(ColumnAnnotation newColumn) {
		ColumnAnnotation old = this.column;
		this.column = newColumn;
		this.firePropertyChanged(COLUMN_PROPERTY, old, newColumn);
	}

	public TextRange getColumnTextRange(CompilationUnit astRoot) {
		if (this.column != null) {
			return this.column.getTextRange(astRoot);
		}
		return getTextRange(astRoot);
	}


	// ********** static methods **********

	private static DeclarationAnnotationAdapter buildColumnAnnotationAdapter(DeclarationAnnotationAdapter writeTransformerAnnotationAdapter) {
		return new NestedDeclarationAnnotationAdapter(writeTransformerAnnotationAdapter, EclipseLink.WRITE_TRANSFORMER__COLUMN, JPA.COLUMN, false);
	}

	private static ColumnAnnotation createColumn(JavaResourceNode parent, Member member, DeclarationAnnotationAdapter writeTransformerAnnotationAdapter) {
		return new SourceColumnAnnotation(parent, member, buildColumnAnnotationAdapter(writeTransformerAnnotationAdapter));
	}

}
