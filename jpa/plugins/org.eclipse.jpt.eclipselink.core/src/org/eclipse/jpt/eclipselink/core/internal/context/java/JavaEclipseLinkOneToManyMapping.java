/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.java;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaRelationshipReference;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaOneToManyMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkOneToManyMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkJoinFetch;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkPrivateOwned;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkPrivateOwnedAnnotation;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.utility.internal.iterators.CompositeIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class JavaEclipseLinkOneToManyMapping
	extends AbstractJavaOneToManyMapping
	implements EclipseLinkOneToManyMapping
{
	protected final JavaEclipseLinkJoinFetch joinFetch;
	
	protected final JavaEclipseLinkPrivateOwned privateOwned;
	
	
	public JavaEclipseLinkOneToManyMapping(JavaPersistentAttribute parent) {
		super(parent);
		this.joinFetch = new JavaEclipseLinkJoinFetch(this);
		this.privateOwned = new JavaEclipseLinkPrivateOwned(this);
	}
	
	@Override
	protected JavaRelationshipReference buildRelationshipReference() {
		return new JavaEclipseLinkOneToManyRelationshipReference(this);
	}
	
	@Override
	public Iterator<String> supportingAnnotationNames() {
		return new CompositeIterator<String>(
			super.supportingAnnotationNames(),
			new ArrayIterator<String>(
				EclipseLink.JOIN_FETCH,
				EclipseLink.PRIVATE_OWNED));
	}
	
	@Override
	public JavaEclipseLinkOneToManyRelationshipReference getRelationshipReference() {
		return (JavaEclipseLinkOneToManyRelationshipReference) super.getRelationshipReference();
	}
	
	
	// **************** private owned ******************************************
	
	public EclipseLinkPrivateOwned getPrivateOwned() {
		return this.privateOwned;
	}
	
	protected String getPrivateOwnedAnnotationName() {
		return EclipseLinkPrivateOwnedAnnotation.ANNOTATION_NAME;
	}
	
	protected EclipseLinkPrivateOwnedAnnotation getResourcePrivateOwned() {
		return (EclipseLinkPrivateOwnedAnnotation) this.resourcePersistentAttribute.getAnnotation(getPrivateOwnedAnnotationName());
	}
	
	protected void addResourcePrivateOwned() {
		this.resourcePersistentAttribute.addAnnotation(getPrivateOwnedAnnotationName());
	}
	
	protected void removeResourcePrivateOwned() {
		this.resourcePersistentAttribute.removeAnnotation(getPrivateOwnedAnnotationName());
	}
	
	
	// **************** join fetch *********************************************
	
	public EclipseLinkJoinFetch getJoinFetch() {
		return this.joinFetch;
	}
	
	
	// **************** resource -> context ************************************
	
	@Override
	protected void initialize() {
		super.initialize();
		this.joinFetch.initialize(this.resourcePersistentAttribute);
		this.privateOwned.initialize(this.resourcePersistentAttribute);
	}
	
	@Override
	protected void update() {
		super.update();
		this.joinFetch.update(this.resourcePersistentAttribute);
		this.privateOwned.update(this.resourcePersistentAttribute);
	}
	
	
	// **************** validation *********************************************
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		this.joinFetch.validate(messages, reporter, astRoot);
		this.privateOwned.validate(messages, reporter, astRoot);
	}
}
