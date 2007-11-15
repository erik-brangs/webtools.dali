/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value;

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.jpt.utility.internal.model.Model;

/**
 * Interface used to abstract list accessing and
 * change notification and make it more pluggable.
 */
public interface ListValueModel
	extends Model//, Iterable<E>
{
	/**
	 * Return the list's values.
	 */
	Iterator iterator();
		String LIST_VALUES = "list values";

	/**
	 * Return the list's values.
	 */
	ListIterator listIterator();

	/**
	 * Return the size of the list.
	 */
	int size();

	/**
	 * Return the item at the specified index of the list.
	 */
	Object get(int index);

}
