/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.binary;

import java.util.Vector;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.jpa.core.resource.java.AttributeOverridesAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.jpa.core.resource.java.NestableAttributeOverrideAnnotation;

/**
 * javax.persistence.AttributeOverrides
 */
public final class BinaryAttributeOverridesAnnotation
	extends BinaryContainerAnnotation<NestableAttributeOverrideAnnotation>
	implements AttributeOverridesAnnotation
{
	private final Vector<NestableAttributeOverrideAnnotation> attributeOverrides;


	public BinaryAttributeOverridesAnnotation(JavaResourceNode parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
		this.attributeOverrides = this.buildAttributeOverrides();
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public Iterable<NestableAttributeOverrideAnnotation> getNestedAnnotations() {
		return new LiveCloneIterable<NestableAttributeOverrideAnnotation>(this.attributeOverrides);
	}

	public int getNestedAnnotationsSize() {
		return this.attributeOverrides.size();
	}

	private Vector<NestableAttributeOverrideAnnotation> buildAttributeOverrides() {
		Object[] jdtAttributeOverrides = this.getJdtMemberValues(JPA.ATTRIBUTE_OVERRIDES__VALUE);
		Vector<NestableAttributeOverrideAnnotation> result = new Vector<NestableAttributeOverrideAnnotation>(jdtAttributeOverrides.length);
		for (Object jdtAttributeOverride : jdtAttributeOverrides) {
			result.add(new BinaryAttributeOverrideAnnotation(this, (IAnnotation) jdtAttributeOverride));
		}
		return result;
	}

	@Override
	public void update() {
		super.update();
		this.updateAttributeOverrides();
	}

	// TODO
	private void updateAttributeOverrides() {
		throw new UnsupportedOperationException();
	}

}
