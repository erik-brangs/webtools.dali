/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.base;

import java.util.ListIterator;


public interface ISingleRelationshipMapping extends IRelationshipMapping, INullable
{
	
	// **************** fetch type **************************************

	FetchType DEFAULT_FETCH_TYPE = FetchType.EAGER;

	// **************** join columns **************************************

	/**
	 * Return a list iterator of the join columns whether specified or default.
	 * This will not be null.
	 */
	<T extends IJoinColumn> ListIterator<T> joinColumns();

	/**
	 * Return the number of join columns, both specified and default.
	 */
	int joinColumnsSize();

	/**
	 * Return a list iterator of the specified join columns.
	 * This will not be null.
	 */
	<T extends IJoinColumn> ListIterator<T> specifiedJoinColumns();
		String SPECIFIED_JOIN_COLUMNS_LIST = "specifiedJoinColumnsList";

	/**
	 * Return the number of specified join columns.
	 */
	int specifiedJoinColumnsSize();

	/**
	 * Return the default join column or null.  A default join column
	 * only exists if there are no specified join columns.
	 */
	IJoinColumn getDefaultJoinColumn();
		String DEFAULT_JOIN_COLUMN = "defaultJoinColumn";

	/**
	 * Add a specified join column to the join table return the object 
	 * representing it.
	 */
	IJoinColumn addSpecifiedJoinColumn(int index);
	
	/**
	 * Remove the specified join column from the join table.
	 */
	void removeSpecifiedJoinColumn(int index);
	
	/**
	 * Remove the specified join column at the index from the join table.
	 */
	void removeSpecifiedJoinColumn(IJoinColumn joinColumn);
	
	/**
	 * Move the specified join column from the source index to the target index.
	 */
	void moveSpecifiedJoinColumn(int targetIndex, int sourceIndex);

	boolean containsSpecifiedJoinColumns();

}