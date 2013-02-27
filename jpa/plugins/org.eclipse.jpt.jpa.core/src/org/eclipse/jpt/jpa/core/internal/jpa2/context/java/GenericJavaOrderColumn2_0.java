/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2.context.java;

import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.jpa.core.context.NamedColumn;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaNamedColumn;
import org.eclipse.jpt.jpa.core.internal.jpa2.resource.java.OrderColumn2_0AnnotationDefinition;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaSpecifiedOrderColumn2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.java.JavaOrderable2_0;
import org.eclipse.jpt.jpa.core.jpa2.resource.java.OrderColumn2_0Annotation;

/**
 * Java order column
 */
public class GenericJavaOrderColumn2_0
	extends AbstractJavaNamedColumn<JavaOrderable2_0, OrderColumn2_0Annotation, NamedColumn.Owner>
	implements JavaSpecifiedOrderColumn2_0
{
	protected Boolean specifiedNullable;
	protected boolean defaultNullable;

	protected Boolean specifiedInsertable;
	protected boolean defaultInsertable;

	protected Boolean specifiedUpdatable;
	protected boolean defaultUpdatable;

	// JPA 1.0
	protected OrderColumn2_0Annotation nullColumnAnnotation;


	public GenericJavaOrderColumn2_0(JavaOrderable2_0 parent, NamedColumn.Owner owner) {
		super(parent, owner);
		//build defaults during construction for performance
		this.defaultNullable = this.buildDefaultNullable();
		this.defaultInsertable = this.buildDefaultInsertable();
		this.defaultUpdatable = this.buildDefaultUpdatable();
	}

	@Override
	protected void initialize(OrderColumn2_0Annotation columnAnnotation) {
		super.initialize(columnAnnotation);
		this.specifiedNullable = this.buildSpecifiedNullable(columnAnnotation);
		this.specifiedInsertable = this.buildSpecifiedInsertable(columnAnnotation);
		this.specifiedUpdatable = this.buildSpecifiedUpdatable(columnAnnotation);
	}

	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel(OrderColumn2_0Annotation columnAnnotation) {
		super.synchronizeWithResourceModel(columnAnnotation);
		this.setSpecifiedNullable_(this.buildSpecifiedNullable(columnAnnotation));
		this.setSpecifiedInsertable_(this.buildSpecifiedInsertable(columnAnnotation));
		this.setSpecifiedUpdatable_(this.buildSpecifiedUpdatable(columnAnnotation));
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultNullable(this.buildDefaultNullable());
		this.setDefaultInsertable(this.buildDefaultInsertable());
		this.setDefaultUpdatable(this.buildDefaultUpdatable());
	}


	// ********** column annotation **********

	/**
	 * If we are in a JPA 1.0 project, return a null annotation.
	 */
	@Override
	public OrderColumn2_0Annotation getColumnAnnotation() {
		// hmmmm...
		return this.isJpa2_0Compatible() ?
				(OrderColumn2_0Annotation) this.getResourceAttribute().getNonNullAnnotation(OrderColumn2_0Annotation.ANNOTATION_NAME) :
				this.getNullColumnAnnotation();
	}

	protected OrderColumn2_0Annotation getNullColumnAnnotation() {
		if (this.nullColumnAnnotation == null) {
			this.nullColumnAnnotation = this.buildNullColumnAnnotation();
		}
		return this.nullColumnAnnotation;
	}

	protected OrderColumn2_0Annotation buildNullColumnAnnotation() {
		// hmmmm...
		return (OrderColumn2_0Annotation) OrderColumn2_0AnnotationDefinition.instance().buildNullAnnotation(this.getResourceAttribute());
	}

	@Override
	protected void removeColumnAnnotation() {
		if (this.isJpa2_0Compatible()) {
			this.getResourceAttribute().removeAnnotation(OrderColumn2_0Annotation.ANNOTATION_NAME);
		} else {
			throw new IllegalStateException();
		}
	}


	// ********** nullable **********

	public boolean isNullable() {
		return (this.specifiedNullable != null) ? this.specifiedNullable.booleanValue() : this.isDefaultNullable();
	}

	public Boolean getSpecifiedNullable() {
		return this.specifiedNullable;
	}

	public void setSpecifiedNullable(Boolean nullable) {
		if (this.valuesAreDifferent(this.specifiedNullable, nullable)) {
			this.getColumnAnnotation().setNullable(nullable);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedNullable_(nullable);
		}
	}

	protected void setSpecifiedNullable_(Boolean nullable) {
		Boolean old = this.specifiedNullable;
		this.specifiedNullable = nullable;
		this.firePropertyChanged(SPECIFIED_NULLABLE_PROPERTY, old, nullable);
	}

	protected Boolean buildSpecifiedNullable(OrderColumn2_0Annotation columnAnnotation) {
		return columnAnnotation.getNullable();
	}

	public boolean isDefaultNullable() {
		return this.defaultNullable;
	}

	protected void setDefaultNullable(boolean nullable) {
		boolean old = this.defaultNullable;
		this.defaultNullable = nullable;
		this.firePropertyChanged(DEFAULT_NULLABLE_PROPERTY, old, nullable);
	}

	protected boolean buildDefaultNullable() {
		return DEFAULT_NULLABLE;
	}


	// ********** insertable **********

	public boolean isInsertable() {
		return (this.specifiedInsertable != null) ? this.specifiedInsertable.booleanValue() : this.isDefaultInsertable();
	}

	public Boolean getSpecifiedInsertable() {
		return this.specifiedInsertable;
	}

	public void setSpecifiedInsertable(Boolean insertable) {
		if (this.valuesAreDifferent(this.specifiedInsertable, insertable)) {
			this.getColumnAnnotation().setInsertable(insertable);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedInsertable_(insertable);
		}
	}

	protected void setSpecifiedInsertable_(Boolean insertable) {
		Boolean old = this.specifiedInsertable;
		this.specifiedInsertable = insertable;
		this.firePropertyChanged(SPECIFIED_INSERTABLE_PROPERTY, old, insertable);
	}

	protected Boolean buildSpecifiedInsertable(OrderColumn2_0Annotation columnAnnotation) {
		return columnAnnotation.getInsertable();
	}

	public boolean isDefaultInsertable() {
		return this.defaultInsertable;
	}

	protected void setDefaultInsertable(boolean insertable) {
		boolean old = this.defaultInsertable;
		this.defaultInsertable = insertable;
		this.firePropertyChanged(DEFAULT_INSERTABLE_PROPERTY, old, insertable);
	}

	protected boolean buildDefaultInsertable() {
		return DEFAULT_INSERTABLE;
	}


	// ********** updatable **********

	public boolean isUpdatable() {
		return (this.specifiedUpdatable != null) ? this.specifiedUpdatable.booleanValue() : this.isDefaultUpdatable();
	}

	public Boolean getSpecifiedUpdatable() {
		return this.specifiedUpdatable;
	}

	public void setSpecifiedUpdatable(Boolean updatable) {
		if (this.valuesAreDifferent(this.specifiedUpdatable, updatable)) {
			this.getColumnAnnotation().setUpdatable(updatable);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedUpdatable_(updatable);
		}
	}

	protected void setSpecifiedUpdatable_(Boolean updatable) {
		Boolean old = this.specifiedUpdatable;
		this.specifiedUpdatable = updatable;
		this.firePropertyChanged(SPECIFIED_UPDATABLE_PROPERTY, old, updatable);
	}

	protected Boolean buildSpecifiedUpdatable(OrderColumn2_0Annotation columnAnnotation) {
		return columnAnnotation.getUpdatable();
	}

	public boolean isDefaultUpdatable() {
		return this.defaultUpdatable;
	}

	protected void setDefaultUpdatable(boolean updatable) {
		boolean old = this.defaultUpdatable;
		this.defaultUpdatable = updatable;
		this.firePropertyChanged(DEFAULT_UPDATABLE_PROPERTY, old, updatable);
	}

	protected boolean buildDefaultUpdatable() {
		return DEFAULT_UPDATABLE;
	}


	// ********** misc **********

	protected JavaOrderable2_0 getOrderable() {
		return this.parent;
	}

	protected JavaResourceAttribute getResourceAttribute() {
		return this.getOrderable().getResourceAttribute();
	}

	@Override
	public String getTableName() {
		return this.getOrderable().getDefaultTableName();
	}
}
