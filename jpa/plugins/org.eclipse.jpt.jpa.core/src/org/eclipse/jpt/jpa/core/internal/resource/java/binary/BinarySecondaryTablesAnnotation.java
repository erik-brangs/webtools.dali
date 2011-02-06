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
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.jpa.core.resource.java.NestableSecondaryTableAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.SecondaryTablesAnnotation;

/**
 * javax.persistence.SecondaryTables
 */
public final class BinarySecondaryTablesAnnotation
	extends BinaryContainerAnnotation<NestableSecondaryTableAnnotation>
	implements SecondaryTablesAnnotation
{
	private final Vector<NestableSecondaryTableAnnotation> secondaryTables;


	public BinarySecondaryTablesAnnotation(JavaResourcePersistentType parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
		this.secondaryTables = this.buildSecondaryTables();
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public Iterable<NestableSecondaryTableAnnotation> getNestedAnnotations() {
		return new LiveCloneIterable<NestableSecondaryTableAnnotation>(this.secondaryTables);
	}

	public int getNestedAnnotationsSize() {
		return this.secondaryTables.size();
	}

	private Vector<NestableSecondaryTableAnnotation> buildSecondaryTables() {
		Object[] jdtSecondaryTables = this.getJdtMemberValues(JPA.SECONDARY_TABLES__VALUE);
		Vector<NestableSecondaryTableAnnotation> result = new Vector<NestableSecondaryTableAnnotation>(jdtSecondaryTables.length);
		for (Object jdtSecondaryTable : jdtSecondaryTables) {
			result.add(new BinarySecondaryTableAnnotation(this, (IAnnotation) jdtSecondaryTable));
		}
		return result;
	}

	@Override
	public void update() {
		super.update();
		this.updateSecondaryTables();
	}

	// TODO
	private void updateSecondaryTables() {
		throw new UnsupportedOperationException();
	}

}
