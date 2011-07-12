/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.v1_2.context.orm;

import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.jpa.core.context.orm.OrmXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.context.orm.OrmXmlDefinition;
import org.eclipse.jpt.jpa.eclipselink.core.JptJpaEclipseLinkCorePlugin;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm.AbstractEclipseLinkOrmXmlDefinition;

public class EclipseLinkOrmXml1_2Definition
	extends AbstractEclipseLinkOrmXmlDefinition
{
	// singleton
	private static final OrmXmlDefinition INSTANCE = new EclipseLinkOrmXml1_2Definition();

	/**
	 * Return the singleton.
	 */
	public static OrmXmlDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkOrmXml1_2Definition() {
		super();
	}

	public JptResourceType getResourceType() {
		return JptJpaEclipseLinkCorePlugin.ECLIPSELINK_ORM_XML_1_2_RESOURCE_TYPE;
	}

}
