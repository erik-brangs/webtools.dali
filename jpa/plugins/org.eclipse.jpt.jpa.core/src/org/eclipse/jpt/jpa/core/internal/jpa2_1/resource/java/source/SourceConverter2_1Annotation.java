/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2_1.resource.java.source;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.jpa.core.jpa2_1.resource.java.Converter2_1Annotation;
import org.eclipse.jpt.jpa.core.jpa2_1.resource.java.JPA2_1;

/**
 * <code>javax.persistence.Converter</code>
 */
public final class SourceConverter2_1Annotation
	extends SourceAnnotation
	implements Converter2_1Annotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private DeclarationAnnotationElementAdapter<Boolean> autoApplyDeclarationAdapter;
	private AnnotationElementAdapter<Boolean> autoApplyAdapter;
	private Boolean autoApply;
	private TextRange autoApplyTextRange;


	public SourceConverter2_1Annotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
		this.autoApplyDeclarationAdapter = this.buildAutoApplyDeclarationAdapter();
		this.autoApplyAdapter = this.buildAutoApplyAdapter();
	}

	@Override
	public void initialize(Annotation astAnnotation) {
		super.initialize(astAnnotation);

		this.autoApply = this.buildAutoApply(astAnnotation);
		this.autoApplyTextRange = this.buildAutoApplyTextRange(astAnnotation);
	}

	@Override
	public void synchronizeWith(Annotation astAnnotation) {
		super.synchronizeWith(astAnnotation);

		this.syncAutoApply(this.buildAutoApply(astAnnotation));
		this.autoApplyTextRange = this.buildAutoApplyTextRange(astAnnotation);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}


	//************* Converter2_1Annotation implementation *************

	// ***** autoApply
	public Boolean getAutoApply() {
		return this.autoApply;
	}

	public void setAutoApply(Boolean autoApply) {
		if (this.attributeValueHasChanged(this.autoApply, autoApply)) {
			this.autoApply = autoApply;
			this.autoApplyAdapter.setValue(autoApply);
		}
	}

	private void syncAutoApply(Boolean astAutoApply) {
		Boolean old = this.autoApply;
		this.autoApply = astAutoApply;
		this.firePropertyChanged(AUTO_APPLY_PROPERTY, old, astAutoApply);
	}

	private Boolean buildAutoApply(Annotation astAnnotation) {
		return this.autoApplyAdapter.getValue(astAnnotation);
	}

	public TextRange getAutoApplyTextRange() {
		return this.autoApplyTextRange;
	}

	private TextRange buildAutoApplyTextRange(Annotation astAnnotation) {
		return this.getElementTextRange(this.autoApplyDeclarationAdapter, astAnnotation);
	}

	private DeclarationAnnotationElementAdapter<Boolean> buildAutoApplyDeclarationAdapter() {
		return this.buildBooleanElementAdapter(this.getAutoApplyElementName());
	}

	private AnnotationElementAdapter<Boolean> buildAutoApplyAdapter() {
		return this.buildBooleanElementAdapter(this.autoApplyDeclarationAdapter);
	}

	String getAutoApplyElementName() {
		return JPA2_1.CONVERTER__AUTO_APPLY;
	}


	// ********** misc **********

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				(this.autoApply == null);
	}
}
