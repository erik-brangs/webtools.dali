/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.java;

import java.util.ListIterator;
import org.eclipse.jpt.jpa.core.context.QueryContainer;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceAnnotatedElement;

/**
 * Java query container
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 *
 * @version 2.3
 * @since 2.3
 */
public interface JavaQueryContainer
	extends QueryContainer, JavaJpaContextNode
{
	// ********** named queries **********

	@SuppressWarnings("unchecked")
	ListIterator<JavaNamedQuery> namedQueries();

	JavaNamedQuery addNamedQuery();

	JavaNamedQuery addNamedQuery(int index);


	// ********** named native queries **********

	@SuppressWarnings("unchecked")
	ListIterator<JavaNamedNativeQuery> namedNativeQueries();

	JavaNamedNativeQuery addNamedNativeQuery();

	JavaNamedNativeQuery addNamedNativeQuery(int index);

	interface Owner
	{
		JavaResourceAnnotatedElement getResourceAnnotatedElement();
	}
}
