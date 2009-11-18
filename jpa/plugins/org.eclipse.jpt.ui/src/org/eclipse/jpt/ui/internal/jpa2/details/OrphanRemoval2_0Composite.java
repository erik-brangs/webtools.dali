/*******************************************************************************
* Copyright (c) 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.details;

import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.jpa2.context.OrphanRemovable2_0;
import org.eclipse.jpt.ui.internal.details.JptUiDetailsMessages;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaOneToOneMapping2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.orm.OrmOneToOneMapping2_0Composite;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | [X]  Orphan removal (true/false)                                    | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see {@link OrphanRemovable2_0}
 * @see {@link JavaOneToOneMapping2_0Composite} - A container of this widget
 * @see {@link OrmOneToOneMapping2_0Composite} - A container of this widget
 */
public class OrphanRemoval2_0Composite extends FormPane<OrphanRemovable2_0>
{
	/**
	 * Creates a new <code>OrphanRemoval2_0Composite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public OrphanRemoval2_0Composite(
							FormPane<? extends RelationshipMapping> parentPane,
							PropertyValueModel<? extends OrphanRemovable2_0> subjectHolder,
							Composite parent) {
		super(parentPane, subjectHolder, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		this.addTriStateCheckBoxWithDefault(
			container,
			JptUiDetailsMessages2_0.OrphanRemoval2_0Composite_orphanRemovalLabel,
			this.buildOrphanRemovalHolder(),
			this.buildOrphanRemovalStringHolder(),
			null		// TODO
		);
	}
	private WritablePropertyValueModel<Boolean> buildOrphanRemovalHolder() {
		return new PropertyAspectAdapter<OrphanRemovable2_0, Boolean>(
				this.getSubjectHolder(), 
				OrphanRemovable2_0.DEFAULT_ORPHAN_REMOVAL_PROPERTY,
				OrphanRemovable2_0.SPECIFIED_ORPHAN_REMOVAL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSpecifiedOrphanRemoval();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSpecifiedOrphanRemoval(value);
			}
		};
	}

	private PropertyValueModel<String> buildOrphanRemovalStringHolder() {
		
		return new TransformationPropertyValueModel<Boolean, String>(this.buildDefaultOrphanRemovalHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptUiDetailsMessages.Boolean_True : JptUiDetailsMessages.Boolean_False;
					return NLS.bind(JptUiDetailsMessages2_0.OrphanRemoval2_0Composite_orphanRemovalLabelDefault, defaultStringValue);
				}
				return JptUiDetailsMessages2_0.OrphanRemoval2_0Composite_orphanRemovalLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultOrphanRemovalHolder() {
		return new PropertyAspectAdapter<OrphanRemovable2_0, Boolean>(
			this.getSubjectHolder(),
			OrphanRemovable2_0.SPECIFIED_ORPHAN_REMOVAL_PROPERTY,
			OrphanRemovable2_0.DEFAULT_ORPHAN_REMOVAL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSpecifiedOrphanRemoval() != null) {
					return null;
				}
				return Boolean.valueOf(this.subject.isDefaultOrphanRemoval());
			}
		};
	}

}