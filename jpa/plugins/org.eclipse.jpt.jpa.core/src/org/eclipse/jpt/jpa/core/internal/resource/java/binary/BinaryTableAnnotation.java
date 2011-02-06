/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.binary;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.jpa.core.resource.java.TableAnnotation;

/**
 * javax.persistence.Table
 */
public final class BinaryTableAnnotation
	extends BinaryBaseTableAnnotation
	implements TableAnnotation
{
	public BinaryTableAnnotation(JavaResourcePersistentType parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}


	// ********** BinaryBaseTableAnnotation implementation **********

	@Override
	protected String getNameElementName() {
		return JPA.TABLE__NAME;
	}

	@Override
	protected String getSchemaElementName() {
		return JPA.TABLE__SCHEMA;
	}

	@Override
	protected String getCatalogElementName() {
		return JPA.TABLE__CATALOG;
	}

	@Override
	protected String getUniqueConstraintElementName() {
		return JPA.TABLE__UNIQUE_CONSTRAINTS;
	}

}
