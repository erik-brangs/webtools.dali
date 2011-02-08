/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.commands;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.jpa.core.context.orm.OrmReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.ui.internal.dialogs.AddPersistentAttributeToXmlAndMapDialog;
import org.eclipse.jpt.jpa.ui.internal.selection.DefaultJpaSelection;
import org.eclipse.jpt.jpa.ui.internal.selection.JpaSelectionManager;
import org.eclipse.jpt.jpa.ui.internal.selection.SelectionManagerFactory;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddPersistentAttributeToXmlAndMapHandler extends AbstractHandler
{
	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent executionEvent) throws ExecutionException {
		final IWorkbenchWindow window = 
			HandlerUtil.getActiveWorkbenchWindowChecked(executionEvent);
		
		final List<OrmReadOnlyPersistentAttribute> newAttributes = new ArrayList<OrmReadOnlyPersistentAttribute>();
		
		IStructuredSelection selection 
			= (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(executionEvent);
		
		
		// only applies for multiply selected OrmReadOnlyPersistentAttribute objects in a tree
		for (OrmReadOnlyPersistentAttribute attribute : (Iterable<OrmReadOnlyPersistentAttribute>) CollectionTools.iterable(selection.iterator())) {
			OrmPersistentType type = attribute.getOwningPersistentType();
			String attributeName = attribute.getName();
			
			AddPersistentAttributeToXmlAndMapDialog dialog = new AddPersistentAttributeToXmlAndMapDialog(window.getShell(), attribute);
			dialog.create();
			dialog.setBlockOnOpen(true);
			dialog.open();
			
			OrmReadOnlyPersistentAttribute newAttribute = type.getAttributeNamed(attributeName);
			if (newAttribute != null) {
				newAttributes.add(newAttribute);
			}
		}
		
		if (newAttributes.size() == 1) {
			window.getShell().getDisplay().asyncExec(
				new Runnable() {
					public void run() {
						JpaSelectionManager selectionManager = SelectionManagerFactory.getSelectionManager(window);
						selectionManager.select(new DefaultJpaSelection(newAttributes.get(0)), null);
					}
				});
		}
		
		return null;
	}
}