/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.java;

import org.eclipse.jpt.jpa.core.context.java.DefaultJavaAttributeMappingDefinition;

public class JavaEmbeddedMappingDefinition
	extends AbstractJavaEmbeddedMappingDefinition
{
	// singleton
	private static final DefaultJavaAttributeMappingDefinition INSTANCE = new JavaEmbeddedMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static DefaultJavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private JavaEmbeddedMappingDefinition() {
		super();
	}
}
