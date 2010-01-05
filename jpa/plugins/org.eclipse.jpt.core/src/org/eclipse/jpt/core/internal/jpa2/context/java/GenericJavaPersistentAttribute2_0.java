/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.java;

import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.MultiRelationshipMapping;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaPersistentAttribute;
import org.eclipse.jpt.core.jpa2.context.MetamodelField;
import org.eclipse.jpt.core.jpa2.context.java.JavaPersistentAttribute2_0;
import org.eclipse.jpt.core.jpa2.resource.java.Access2_0Annotation;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.utility.internal.ClassName;

/**
 * JPA 2.0 Java persistent attribute
 */
public class GenericJavaPersistentAttribute2_0
	extends AbstractJavaPersistentAttribute
	implements JavaPersistentAttribute2_0
{
	protected AccessType specifiedAccess;


	public GenericJavaPersistentAttribute2_0(PersistentType parent, JavaResourcePersistentAttribute resourcePersistentAttribute) {
		super(parent, resourcePersistentAttribute);
		this.specifiedAccess = this.buildSpecifiedAccess();
	}


	// ********** AccessHolder implementation **********

	public AccessType getSpecifiedAccess() {
		return this.specifiedAccess;
	}

	/**
	 * Don't support changing to specified access on a java persistent attribute, this
	 * involves a larger process of moving the annotations to the corresponding field/property
	 * which may or may not exist or might need to be created.
	 */
	public void setSpecifiedAccess(AccessType specifiedAccess) {
		throw new UnsupportedOperationException();
	}

	protected void setSpecifiedAccess_(AccessType specifiedAccess) {
		AccessType old = this.specifiedAccess;
		this.specifiedAccess = specifiedAccess;
		this.firePropertyChanged(SPECIFIED_ACCESS_PROPERTY, old, specifiedAccess);
	}

	protected AccessType buildSpecifiedAccess() {
		Access2_0Annotation accessAnnotation = 
				(Access2_0Annotation) this.resourcePersistentAttribute.
					getAnnotation(Access2_0Annotation.ANNOTATION_NAME);
		return accessAnnotation == null ? null : AccessType.fromJavaResourceModel(accessAnnotation.getValue());
	}

	@Override
	public void update() {
		super.update();
		this.setSpecifiedAccess_(this.buildSpecifiedAccess());
	}


	// ********** metamodel **********

	public String getMetamodelContainerFieldTypeName() {
		return this.getJpaContainer(this.resourcePersistentAttribute.getTypeName()).getMetamodelContainerFieldTypeName();
	}

	public String getMetamodelContainerFieldMapKeyTypeName() {
		return this.getJpaContainer(this.resourcePersistentAttribute.getTypeName()).getMetamodelContainerFieldMapKeyTypeName((MultiRelationshipMapping) this.getMapping());
	}

	public String getMetamodelTypeName() {
		String typeName = this.resourcePersistentAttribute.getTypeName();
		if (typeName == null) {
			return MetamodelField.DEFAULT_TYPE_NAME;
		}
		if (ClassName.isPrimitive(typeName)) {
			return ClassName.getWrapperClassName(typeName);  // ???
		}
		return typeName;
	}

}
