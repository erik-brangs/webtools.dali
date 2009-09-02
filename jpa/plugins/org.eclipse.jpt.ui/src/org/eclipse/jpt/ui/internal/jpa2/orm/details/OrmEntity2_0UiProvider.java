/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.orm.details;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.ui.JpaUiFactory;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.details.TypeMappingUiProvider;
import org.eclipse.jpt.ui.internal.details.AbstractEntityUiProvider;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

public class OrmEntity2_0UiProvider 
	extends AbstractEntityUiProvider<OrmEntity>
{
	// singleton
	private static final OrmEntity2_0UiProvider INSTANCE = 
		new OrmEntity2_0UiProvider();
	
	/**
	 * Return the singleton.
	 */
	public static TypeMappingUiProvider<OrmEntity> instance() {
		return INSTANCE;
	}
	
	public IContentType getContentType() {
		return JptCorePlugin.ORM2_0_XML_CONTENT_TYPE;
	}
	
	/**
	 * Ensure single instance.
	 */
	private OrmEntity2_0UiProvider() {
		super();
	}
	
	public JpaComposite buildPersistentTypeMappingComposite(
			JpaUiFactory factory, 
			PropertyValueModel<OrmEntity> subjectHolder, 
			Composite parent, 
			WidgetFactory widgetFactory) {

		return factory.createOrmEntityComposite(subjectHolder, parent, widgetFactory);
	}
}
