/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.structure;

import org.eclipse.jpt.core.internal.PersistenceJpaFileProvider;
import org.eclipse.jpt.ui.jface.ItemLabelProviderFactory;
import org.eclipse.jpt.ui.jface.TreeItemContentProviderFactory;
import org.eclipse.jpt.ui.structure.JpaStructureProvider;

public class PersistenceResourceModelStructureProvider
	implements JpaStructureProvider
{
	// singleton
	private static final JpaStructureProvider INSTANCE = new PersistenceResourceModelStructureProvider();

	/**
	 * Return the singleton.
	 */
	public static JpaStructureProvider instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private PersistenceResourceModelStructureProvider() {
		super();
	}
	
	public String getResourceType() {
		return PersistenceJpaFileProvider.RESOURCE_TYPE;
	}

	public TreeItemContentProviderFactory getTreeItemContentProviderFactory() {
		return new PersistenceItemContentProviderFactory();
	}
	
	public ItemLabelProviderFactory getItemLabelProviderFactory() {
		return new PersistenceItemLabelProviderFactory();
	}

}
