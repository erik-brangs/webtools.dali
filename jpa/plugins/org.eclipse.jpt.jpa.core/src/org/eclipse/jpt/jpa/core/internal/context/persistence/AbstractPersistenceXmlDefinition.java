/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.persistence;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlDefinition;

/**
 * All the state in the definition should be "static"
 * (i.e. unchanging once it is initialized).
 */
public abstract class AbstractPersistenceXmlDefinition
	implements PersistenceXmlDefinition
{
	private final PersistenceXmlContextNodeFactory factory;


	/**
	 * zero-argument constructor
	 */
	protected AbstractPersistenceXmlDefinition() {
		super();
		this.factory = this.buildContextNodeFactory();
	}

	protected abstract PersistenceXmlContextNodeFactory buildContextNodeFactory();

	public PersistenceXmlContextNodeFactory getContextNodeFactory() {
		return this.factory;
	}


	// ********** misc **********

	protected JptResourceType getResourceType(IContentType contentType, String version) {
		return PlatformTools.getResourceType(contentType, version);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
