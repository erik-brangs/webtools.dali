/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.ui.internal.navigator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jpt.common.ui.internal.jface.NavigatorContentProvider;
import org.eclipse.jpt.common.ui.jface.ItemExtendedLabelProviderFactory;
import org.eclipse.jpt.common.ui.jface.ItemTreeContentProviderFactory;
import org.eclipse.jpt.common.utility.model.event.CollectionAddEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionChangeEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionClearEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionRemoveEvent;
import org.eclipse.jpt.common.utility.model.listener.CollectionChangeListener;
import org.eclipse.jpt.jaxb.core.JaxbProject;
import org.eclipse.jpt.jaxb.core.JaxbProjectManager;
import org.eclipse.jpt.jaxb.core.JptJaxbCorePlugin;
import org.eclipse.jpt.jaxb.core.platform.JaxbPlatformDescription;
import org.eclipse.jpt.jaxb.ui.internal.plugin.JptJaxbUiPlugin;
import org.eclipse.jpt.jaxb.ui.platform.JaxbPlatformUi;

/**
 * This extension of navigator content provider delegates to the platform UI
 * (see the org.eclipse.jpt.jaxb.ui.jaxbPlatformUis extension point) for navigator content.
 * 
 * If there is a platform UI for the given project, this content provider will
 * provide a root "JAXB Content" node (child of the project), otherwise there
 * will be no content.  For children of the "JAXB Content" node (or for any other
 * sub-node), this provider will delegate to the content provider returned by the 
 * platform UI implementation.
 */
public class JaxbNavigatorContentProvider
	extends NavigatorContentProvider
{
	private final CollectionChangeListener jaxbProjectListener;
	
	private StructuredViewer viewer;
	
	
	public JaxbNavigatorContentProvider() {
		super();
		this.jaxbProjectListener = this.buildJaxbProjectListener();
		JptJaxbCorePlugin.instance().getProjectManager().addCollectionChangeListener(JaxbProjectManager.JAXB_PROJECTS_COLLECTION, this.jaxbProjectListener);
	}
	
	protected CollectionChangeListener buildJaxbProjectListener() {
		return new JaxbProjectListener();
	}

	@Override
	protected ItemTreeContentProviderFactory buildItemContentProviderFactory() {
		return new JaxbNavigatorTreeItemContentProviderFactory();
	}

	@Override
	protected ItemExtendedLabelProviderFactory buildItemLabelProviderFactory() {
		return new JaxbNavigatorItemLabelProviderFactory();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		this.viewer = (StructuredViewer) viewer;
	}
	
	@Override
	protected boolean hasChildren_(Object element) {
		if (element instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
			
			if (project != null) {
				JaxbProject jaxbProject = JptJaxbCorePlugin.instance().getProjectManager().getJaxbProject(project);
				if (jaxbProject != null) {
					JaxbPlatformDescription desc = jaxbProject.getPlatform().getDescription();
					JaxbPlatformUi platformUi = 
							JptJaxbUiPlugin.getJaxbPlatformUiManager().getJaxbPlatformUi(desc);
					
					return platformUi != null;
				}	
			}
		}
		return false;
	}

	@Override
	protected Object[] getChildren_(Object parentElement) {
		if (parentElement instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) parentElement).getAdapter(IProject.class);
			
			if (project != null) {
				JaxbProject jaxbProject = JptJaxbCorePlugin.instance().getProjectManager().getJaxbProject(project);
				if (jaxbProject != null) {
					JaxbPlatformDescription desc = jaxbProject.getPlatform().getDescription();
					JaxbPlatformUi platformUi = 
							JptJaxbUiPlugin.getJaxbPlatformUiManager().getJaxbPlatformUi(desc);
					
					if (platformUi != null) {
						return new Object[] {jaxbProject.getContextRoot()};
					}
				}	
			}
		}
		return null;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		JptJaxbCorePlugin.instance().getProjectManager().removeCollectionChangeListener(JaxbProjectManager.JAXB_PROJECTS_COLLECTION, this.jaxbProjectListener);
	}
	
	
	// **************** member classes *****************************************
	
	/* CU private */ class JaxbProjectListener
		implements CollectionChangeListener
	{
		public void collectionChanged(CollectionChangeEvent event) {
			this.refreshViewer(null);
		}
		public void collectionCleared(CollectionClearEvent event) {
			this.refreshViewer(null);
		}
		public void itemsAdded(CollectionAddEvent event) {
			for (Object item : event.getItems()) {
				this.refreshViewer(((JaxbProject) item).getProject());
			}
		}
		public void itemsRemoved(CollectionRemoveEvent event) {
			for (Object item : event.getItems()) {
				this.refreshViewer(((JaxbProject) item).getProject());
			}
		}
		
		private void refreshViewer(final IProject project) {
			if (viewer != null 
					&& viewer.getControl() != null 
					&& !viewer.getControl().isDisposed()) {
				// Using job here so that project model update (which also uses
				//  a job) will complete first
				Job refreshJob = new Job("Refresh viewer") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						// Using runnable here so that refresh will go on correct thread
						viewer.getControl().getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (project != null) {
									viewer.refresh(project);
								}
								else {
									viewer.refresh();
								}
							}
						});
						return Status.OK_STATUS;
					}
				};
				refreshJob.setRule(project);
				refreshJob.schedule();
			}
		}
	}
}
