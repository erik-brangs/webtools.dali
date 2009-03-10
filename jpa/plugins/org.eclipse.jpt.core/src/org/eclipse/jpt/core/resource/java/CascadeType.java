/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.java;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public enum CascadeType {
	
	ALL(JPA.CASCADE_TYPE__ALL),
	PERSIST(JPA.CASCADE_TYPE__PERSIST),
	MERGE(JPA.CASCADE_TYPE__MERGE),
	REMOVE(JPA.CASCADE_TYPE__REMOVE),
	REFRESH(JPA.CASCADE_TYPE__REFRESH);
	
	private String javaAnnotationValue;
	
	CascadeType(String javaAnnotationValue) {
		this.javaAnnotationValue = javaAnnotationValue;
	}
	
	public String getJavaAnnotationValue() {
		return this.javaAnnotationValue;
	}
	
	public static CascadeType[] fromJavaAnnotationValue(String[] javaAnnotationValues) {
		if (javaAnnotationValues == null) {
			return EMPTY_CASCADE_TYPE_ARRAY;
		}
		CascadeType[] cascadeTypes = new CascadeType[javaAnnotationValues.length];
		for (int i = 0; i < javaAnnotationValues.length; i++) {
			String javaAnnotationValue = javaAnnotationValues[i];
			if (javaAnnotationValue != null) {
				cascadeTypes[i] = cascadeType(javaAnnotationValue);
			}
		}
		return cascadeTypes;
	}
	private static final CascadeType[] EMPTY_CASCADE_TYPE_ARRAY = new CascadeType[0];

	private static CascadeType cascadeType(String javaAnnotationValue) {
		if (javaAnnotationValue.equals(ALL.getJavaAnnotationValue())) {
			return ALL;
		}
		if (javaAnnotationValue.equals(PERSIST.getJavaAnnotationValue())) {
			return PERSIST;
		}
		if (javaAnnotationValue.equals(MERGE.getJavaAnnotationValue())) {
			return MERGE;
		}
		if (javaAnnotationValue.equals(REMOVE.getJavaAnnotationValue())) {
			return REMOVE;
		}
		if (javaAnnotationValue.equals(REFRESH.getJavaAnnotationValue())) {
			return REFRESH;
		}
		throw new IllegalArgumentException("Unknown cascade type: " + javaAnnotationValue); //$NON-NLS-1$
	}
	
	public static String[] toJavaAnnotationValue(CascadeType[] cascadeTypes) {
		String[] javaAnnotationValues = new String[cascadeTypes.length];
		for (int i = 0; i < cascadeTypes.length; i++) {
			javaAnnotationValues[i] = cascadeTypes[i].javaAnnotationValue;
		}
		return javaAnnotationValues;
	}

}
