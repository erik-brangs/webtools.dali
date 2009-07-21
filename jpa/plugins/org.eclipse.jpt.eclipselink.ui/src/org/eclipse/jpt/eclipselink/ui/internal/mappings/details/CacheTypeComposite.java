/*******************************************************************************
 *  Copyright (c) 2008 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.mappings.details;

import java.util.Collection;
import org.eclipse.jpt.eclipselink.core.context.CacheType;
import org.eclipse.jpt.eclipselink.core.context.Caching;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.eclipselink.ui.internal.mappings.EclipseLinkUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.swt.widgets.Composite;

/**
 * Here is the layout of this pane:
 * <pre>
 * ----------------------------------------------------------------------------
 * |       ------------------------------------------------------------------ |
 * | Type: |                                                              |v| |
 * |       ------------------------------------------------------------------ |
 * ----------------------------------------------------------------------------</pre>
 *
 * @see Caching
 * @see CachingComposite - A container of this widget
 *
 * @version 2.1
 * @since 2.1
 */
public class CacheTypeComposite extends FormPane<Caching> {

	/**
	 * Creates a new <code>CacheTypeComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public CacheTypeComposite(FormPane<? extends Caching> parentPane,
	                          Composite parent) {

		super(parentPane, parent);
	}

	private EnumFormComboViewer<Caching, CacheType> addCacheTypeCombo(Composite container) {

		return new EnumFormComboViewer<Caching, CacheType>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(Caching.DEFAULT_TYPE_PROPERTY);
				propertyNames.add(Caching.SPECIFIED_TYPE_PROPERTY);
			}

			@Override
			protected CacheType[] getChoices() {
				return CacheType.values();
			}

			@Override
			protected CacheType getDefaultValue() {
				return getSubject().getDefaultType();
			}

			@Override
			protected String displayString(CacheType value) {
				return buildDisplayString(
					EclipseLinkUiMappingsMessages.class,
					CacheTypeComposite.this,
					value
				);
			}

			@Override
			protected CacheType getValue() {
				return getSubject().getSpecifiedType();
			}

			@Override
			protected void setValue(CacheType value) {
				getSubject().setSpecifiedType(value);
			}
			
			@Override
			protected boolean sortChoices() {
				return false;
			}
		};
	}

	@Override
	protected void initializeLayout(Composite container) {

		addLabeledComposite(
			container,
			EclipseLinkUiMappingsMessages.CacheTypeComposite_label,
			addCacheTypeCombo(container),
			EclipseLinkHelpContextIds.CACHING_CACHE_TYPE
		);
	}
}
