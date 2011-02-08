/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.validation;

import org.eclipse.osgi.util.NLS;

public class JpaValidationDescriptionMessages {
	
	public static String DOES_NOT_MATCH_JOIN_TABLE;
	public static String DOES_NOT_MATCH_COLLECTION_TABLE;
	public static String NOT_VALID_FOR_THIS_ENTITY;
	
	public static String ATTRIBUTE_DESC;
	public static String VIRTUAL_ATTRIBUTE_DESC;

	public static String ON_MAPPED_SUPERCLASS;
	public static String ON_EMBEDDABLE;


	private static final String BUNDLE_NAME = "jpa_validation_description"; //$NON-NLS-1$
	private static final Class<?> BUNDLE_CLASS = JpaValidationDescriptionMessages.class;
	static {
		NLS.initializeMessages(BUNDLE_NAME, BUNDLE_CLASS);
	}

	private JpaValidationDescriptionMessages() {
		throw new UnsupportedOperationException();
	}
}