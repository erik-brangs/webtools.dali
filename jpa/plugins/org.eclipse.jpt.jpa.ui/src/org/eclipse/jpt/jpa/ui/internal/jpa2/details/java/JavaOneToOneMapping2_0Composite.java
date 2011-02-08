/*******************************************************************************
 * Copyright (c) 2009, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.jpa2.details.java;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.java.JavaOneToOneMapping;
import org.eclipse.jpt.jpa.core.jpa2.context.OrphanRemovable2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.OrphanRemovalHolder2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaOneToOneRelationship2_0;
import org.eclipse.jpt.jpa.ui.internal.details.FetchTypeComposite;
import org.eclipse.jpt.jpa.ui.internal.details.OptionalComposite;
import org.eclipse.jpt.jpa.ui.internal.details.TargetEntityComposite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.AbstractOneToOneMapping2_0Composite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.CascadePane2_0;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.OneToOneJoiningStrategy2_0Pane;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.OrphanRemoval2_0Composite;
import org.eclipse.swt.widgets.Composite;

public class JavaOneToOneMapping2_0Composite
	extends AbstractOneToOneMapping2_0Composite<JavaOneToOneMapping, JavaOneToOneRelationship2_0>
{
	public JavaOneToOneMapping2_0Composite(
			PropertyValueModel<? extends JavaOneToOneMapping> subjectHolder,
			Composite parent,
			WidgetFactory widgetFactory) {
		
		super(subjectHolder, parent, widgetFactory);
	}
	
	
	@Override
	protected void initializeOneToOneSection(Composite container) {
		new TargetEntityComposite(this, container);
		new FetchTypeComposite(this, container);
		new OptionalComposite(this, container);
		new OrphanRemoval2_0Composite(this, buildOrphanRemovableHolder(), container);
		new CascadePane2_0(this, buildCascadeHolder(),  addSubPane(container, 5));
	}
	
	@Override
	protected void initializeJoiningStrategyCollapsibleSection(Composite container) {
		new OneToOneJoiningStrategy2_0Pane(this, buildJoiningHolder(), container);
	}
	
	protected PropertyValueModel<OrphanRemovable2_0> buildOrphanRemovableHolder() {
		return new PropertyAspectAdapter<JavaOneToOneMapping, OrphanRemovable2_0>(getSubjectHolder()) {
			@Override
			protected OrphanRemovable2_0 buildValue_() {
				return ((OrphanRemovalHolder2_0) this.subject).getOrphanRemoval();
			}
		};
	}
}