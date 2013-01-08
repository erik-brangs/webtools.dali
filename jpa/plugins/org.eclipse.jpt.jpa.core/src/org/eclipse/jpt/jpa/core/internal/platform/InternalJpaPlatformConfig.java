/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.platform;

import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.jpa.core.JpaPlatform;
import org.eclipse.jpt.jpa.core.JpaPlatformFactory;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformConfig;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformGroupConfig;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformManager;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

class InternalJpaPlatformConfig
	implements JpaPlatformConfig
{
	private final InternalJpaPlatformManager jpaPlatformManager;
	private final String id;
	private final String label;
	private final String factoryClassName;
	private /* final */ IProjectFacetVersion jpaFacetVersion;
	private /* final */ boolean default_ = false;
	private /* final */ InternalJpaPlatformGroupConfig group;
	private /* final */ String pluginId;

	// lazily initialized
	private JpaPlatform jpaPlatform;


	InternalJpaPlatformConfig(InternalJpaPlatformManager jpaPlatformManager, String id, String label, String factoryClassName) {
		super();
		this.jpaPlatformManager = jpaPlatformManager;
		this.id = id;
		this.label = label;
		this.factoryClassName = factoryClassName;
	}

	public JpaPlatformManager getJpaPlatformManager() {
		return this.jpaPlatformManager;
	}

	public String getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	public String getFactoryClassName() {
		return this.factoryClassName;
	}

	void setJpaFacetVersion(IProjectFacetVersion jpaFacetVersion) {
		this.jpaFacetVersion = jpaFacetVersion;
	}

	public boolean supportsJpaFacetVersion(IProjectFacetVersion version) {
		if ( ! version.getProjectFacet().equals(JpaProject.FACET)) {
			throw new IllegalArgumentException(version.toString());
		}
		return (this.jpaFacetVersion == null) || this.jpaFacetVersion.equals(version);
	}

	public boolean isDefault() {
		return this.default_;
	}

	void setDefault(boolean default_) {
		this.default_ = default_;
	}

	public JpaPlatformGroupConfig getGroupConfig() {
		return this.group;
	}

	void setGroup(InternalJpaPlatformGroupConfig group) {
		this.group = group;
	}

	public String getPluginId() {
		return this.pluginId;
	}

	void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public synchronized JpaPlatform getJpaPlatform() {
		if (this.jpaPlatform == null) {
			this.jpaPlatform = this.buildJpaPlatform();
		}
		return this.jpaPlatform;
	}

	private JpaPlatform buildJpaPlatform() {
		JpaPlatformFactory factory = this.buildJpaPlatformFactory();
		return (factory == null) ? null : factory.buildJpaPlatform(this);
	}

	private JpaPlatformFactory buildJpaPlatformFactory() {
		return PlatformTools.instantiate(this.pluginId, this.jpaPlatformManager.getExtensionPointName(), this.factoryClassName, JpaPlatformFactory.class);
	}

	@Override
	public String toString() {
		return ObjectTools.toString(this, this.label);
	}
}
