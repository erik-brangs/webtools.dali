/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.utility.jdt.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.MemberAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.ShortCircuitAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.core.resource.java.DiscriminatorColumnAnnotation;
import org.eclipse.jpt.core.resource.java.DiscriminatorType;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentMember;
import org.eclipse.jpt.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.Member;

public class DiscriminatorColumnImpl extends AbstractNamedColumn implements DiscriminatorColumnAnnotation
{
	public static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);
	
	private static final DeclarationAnnotationElementAdapter<String> DISCRIMINATOR_TYPE_ADAPTER = buildDiscriminatorTypeAdapter();

	// hold this so we can get the 'length' text range
	private final DeclarationAnnotationElementAdapter<Integer> lengthDeclarationAdapter;

	private final AnnotationElementAdapter<String> discriminatorTypeAdapter;

	private final AnnotationElementAdapter<Integer> lengthAdapter;

	private DiscriminatorType discriminatorType;

	private Integer length;	
	
	protected DiscriminatorColumnImpl(JavaResourceNode parent, Member member, DeclarationAnnotationAdapter daa) {
		super(parent, member, daa,  new MemberAnnotationAdapter(member, daa));
		this.discriminatorTypeAdapter = new ShortCircuitAnnotationElementAdapter<String>(member, DISCRIMINATOR_TYPE_ADAPTER);
		this.lengthDeclarationAdapter = this.buildIntegerElementAdapter(JPA.DISCRIMINATOR_COLUMN__LENGTH);
		this.lengthAdapter = this.buildShortCircuitIntegerElementAdapter(this.lengthDeclarationAdapter);
	}
	
	@Override
	public void initialize(CompilationUnit astRoot) {
		super.initialize(astRoot);
		this.discriminatorType = this.discriminatorType(astRoot);
		this.length = this.length(astRoot);
	}
	
	@Override
	protected String nameElementName() {
		return JPA.DISCRIMINATOR_COLUMN__NAME;
	}
	
	@Override
	protected String columnDefinitionElementName() {
		return JPA.DISCRIMINATOR_COLUMN__COLUMN_DEFINITION;
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}
	
	public void moveAnnotation(int newIndex) {
		//TODO move makes no sense for DiscriminatorColumn.  maybe NestableAnnotation
		//needs to be split up and we could have IndexableAnnotation
	}
	
	@Override
	public void initializeFrom(NestableAnnotation oldAnnotation) {
		super.initializeFrom(oldAnnotation);
		DiscriminatorColumnAnnotation oldColumn = (DiscriminatorColumnAnnotation) oldAnnotation;
		setLength(oldColumn.getLength());
		setDiscriminatorType(oldColumn.getDiscriminatorType());
	}
	
	public DiscriminatorType getDiscriminatorType() {
		return this.discriminatorType;
	}
	
	public void setDiscriminatorType(DiscriminatorType newDiscriminatorType) {
		DiscriminatorType oldDiscriminatorType = this.discriminatorType;
		this.discriminatorType = newDiscriminatorType;
		this.discriminatorTypeAdapter.setValue(DiscriminatorType.toJavaAnnotationValue(newDiscriminatorType));
		firePropertyChanged(DISCRIMINATOR_TYPE_PROPERTY, oldDiscriminatorType, newDiscriminatorType);
	}
	
	public Integer getLength() {
		return this.length;
	}

	public void setLength(Integer newLength) {
		Integer oldLength = this.length;
		this.length = newLength;
		this.lengthAdapter.setValue(newLength);
		firePropertyChanged(LENGTH_PROPERTY, oldLength, newLength);
	}
	
	@Override
	public void updateFromJava(CompilationUnit astRoot) {
		super.updateFromJava(astRoot);
		this.setLength(this.length(astRoot));
		this.setDiscriminatorType(this.discriminatorType(astRoot));
	}

	protected Integer length(CompilationUnit astRoot) {
		return this.lengthAdapter.value(astRoot);
	}
	
	protected DiscriminatorType discriminatorType(CompilationUnit astRoot) {
		return DiscriminatorType.fromJavaAnnotationValue(this.discriminatorTypeAdapter.value(astRoot));
	}
	
	// ********** static methods **********
	private static DeclarationAnnotationElementAdapter<String> buildDiscriminatorTypeAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, JPA.DISCRIMINATOR_COLUMN__DISCRIMINATOR_TYPE);
	}
	
	public static class DiscriminatorColumnAnnotationDefinition implements AnnotationDefinition
	{
		// singleton
		private static final DiscriminatorColumnAnnotationDefinition INSTANCE = new DiscriminatorColumnAnnotationDefinition();


		/**
		 * Return the singleton.
		 */
		public static AnnotationDefinition instance() {
			return INSTANCE;
		}

		/**
		 * Ensure non-instantiability.
		 */
		private DiscriminatorColumnAnnotationDefinition() {
			super();
		}

		public Annotation buildAnnotation(JavaResourcePersistentMember parent, Member member) {
			return new DiscriminatorColumnImpl(parent, member, DiscriminatorColumnImpl.DECLARATION_ANNOTATION_ADAPTER);
		}
		
		public Annotation buildNullAnnotation(JavaResourcePersistentMember parent, Member member) {
			return new NullDiscriminatorColumn(parent);
		}

		public String getAnnotationName() {
			return ANNOTATION_NAME;
		}
	}
}
