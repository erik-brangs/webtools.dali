/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.platform;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jpt.jpa.core.JpaPreferences;
import org.eclipse.jpt.jpa.core.JpaWorkspace;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformDescription;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformManager;

/**
 * Factory to build Dali adapters for an {@link IResource}:<ul>
 * <li>{@link JpaPlatformDescription}
 * </ul>
 * See <code>org.eclipse.jpt.jpa.core/plugin.xml:org.eclipse.core.runtime.adapters</code>.
 */
public class ResourceAdapterFactory
	implements IAdapterFactory
{
	private static final Class<?>[] ADAPTER_LIST = new Class[] {
			JpaPlatformDescription.class
		};

	public Class<?>[] getAdapterList() {
		return ADAPTER_LIST;
	}

	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adaptableObject instanceof IResource) {
			return this.getAdapter((IResource) adaptableObject, adapterType);
		}
		return null;
	}
	
	private Object getAdapter(IResource resource, Class<?> adapterType) {
		if (adapterType == JpaPlatformDescription.class) {
			return this.getJpaPlatformDescription(resource);
		}
		return null;
	}
	
	private JpaPlatformDescription getJpaPlatformDescription(IResource resource) {
		return this.getJpaPlatformManager().getJpaPlatformDescription(JpaPreferences.getJpaPlatformID(resource.getProject()));
	}

	private JpaPlatformManager getJpaPlatformManager() {
		return this.getJpaWorkspace().getJpaPlatformManager();
	}

	private JpaWorkspace getJpaWorkspace() {
		return (JpaWorkspace) ResourcesPlugin.getWorkspace().getAdapter(JpaWorkspace.class);
	}
}
