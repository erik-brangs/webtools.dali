/*******************************************************************************
 *  Copyright (c) 2008  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.orm.details;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.eclipselink.core.internal.JptEclipseLinkCorePlugin;
import org.eclipse.jpt.eclipselink.core.internal.context.orm.EclipseLinkOrmEntity;
import org.eclipse.jpt.ui.JpaUiFactory;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.details.TypeMappingUiProvider;
import org.eclipse.jpt.ui.internal.details.AbstractEntityUiProvider;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

public class EclipseLinkOrmEntityUiProvider 
	extends AbstractEntityUiProvider<EclipseLinkOrmEntity>
{
	// singleton
	private static final EclipseLinkOrmEntityUiProvider INSTANCE = 
		new EclipseLinkOrmEntityUiProvider();
	
	/**
	 * Return the singleton.
	 */
	public static TypeMappingUiProvider<EclipseLinkOrmEntity> instance() {
		return INSTANCE;
	}
	
	public IContentType getContentType() {
		return JptEclipseLinkCorePlugin.ECLIPSELINK_ORM_XML_CONTENT_TYPE;
	}
	
	/**
	 * Ensure single instance.
	 */
	private EclipseLinkOrmEntityUiProvider() {
		super();
	}
	
	
	public JpaComposite buildPersistentTypeMappingComposite(
			JpaUiFactory factory, 
			PropertyValueModel<EclipseLinkOrmEntity> subjectHolder, 
			Composite parent, 
			WidgetFactory widgetFactory) {
		return new EclipseLinkOrmEntityComposite(subjectHolder, parent, widgetFactory);
	}
}
