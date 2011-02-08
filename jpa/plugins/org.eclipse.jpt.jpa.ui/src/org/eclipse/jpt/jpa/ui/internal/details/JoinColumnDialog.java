/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.widgets.DialogPane;
import org.eclipse.jpt.jpa.core.context.JoinColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyJoinColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * The abstract definition of the dialog used to edit an <code>IJoinColumn</code>.
 *
 * @see JoinColumn
 * @see JoinColumnStateObject
 * @see JoinColumnDialogPane
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class JoinColumnDialog<T extends JoinColumnStateObject>
	extends BaseJoinColumnDialog<T>
{

	/**
	 * Creates a new <code>AbstractJoinColumnDialog</code>.
	 *
	 * @param parent The parent shell
	 * @param owner The owner of the join column to create or where it is located
	 * @param joinColumn Either the join column to edit or <code>null</code> if
	 * this state object is used to create a new one
	 */
	public JoinColumnDialog(Shell parent, Object owner, ReadOnlyJoinColumn joinColumn) {
		super(parent, owner, joinColumn);
	}

	@Override
	protected DialogPane<?> buildLayout(Composite container) {
		return new JoinColumnDialogPane<T>(getSubjectHolder(), container);
	}

	@Override
	public ReadOnlyJoinColumn getJoinColumn() {
		return (ReadOnlyJoinColumn) super.getJoinColumn();
	}
}