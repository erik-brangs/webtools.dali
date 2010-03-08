/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.java;

/**
 * Corresponds to the JPA annotation
 * javax.persistence.DiscriminatorColumn
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.2
 * @since 2.0
 */
public interface DiscriminatorColumnAnnotation
	extends NamedColumnAnnotation
{
	String ANNOTATION_NAME = JPA.DISCRIMINATOR_COLUMN;

	/**
	 * Corresponds to the 'discriminatorType' element of the DiscriminatorColumn annotation.
	 * Return null if the element does not exist in Java.
	 */
	DiscriminatorType getDiscriminatorType();
		String DISCRIMINATOR_TYPE_PROPERTY = "discriminatorType"; //$NON-NLS-1$

	/**
	 * Corresponds to the 'discriminatorType' element of the DiscriminatorColumn annotation.
	 * Set the to null to remove the element.
	 */
	void setDiscriminatorType(DiscriminatorType discriminatorType);


	/**
	 * Corresponds to the 'length' element of the DiscriminatorColumn annotation.
	 * Return null if the element does not exist in Java.
	 */
	Integer getLength();
		String LENGTH_PROPERTY = "length"; //$NON-NLS-1$

	/**
	 * Corresponds to the 'length' element of the DiscriminatorColumn annotation.
	 * Set the to null to remove the element.
	 */
	void setLength(Integer length);

}
