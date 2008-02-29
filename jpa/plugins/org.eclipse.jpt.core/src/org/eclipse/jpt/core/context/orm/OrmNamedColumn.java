/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import org.eclipse.jpt.core.TextRange;
import org.eclipse.jpt.core.context.NamedColumn;


/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmNamedColumn extends NamedColumn, OrmJpaContextNode
{

	Owner owner();
	
	/**
	 * Return the (best guess) text location of the column's name.
	 */
	TextRange nameTextRange();

	/**
	 * interface allowing columns to be used in multiple places
	 * (e.g. basic mappings and attribute overrides)
	 */
	interface Owner extends NamedColumn.Owner
	{
		/**
		 * Return the column owner's text range. This can be returned by the
		 * column when its annotation is not present.
		 */
		TextRange validationTextRange();

	}
}