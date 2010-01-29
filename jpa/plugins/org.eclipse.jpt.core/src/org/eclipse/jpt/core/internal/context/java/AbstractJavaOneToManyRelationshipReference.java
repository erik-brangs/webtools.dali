/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.java.JavaJoinTableJoiningStrategy;
import org.eclipse.jpt.core.context.java.JavaMappedByJoiningStrategy;
import org.eclipse.jpt.core.context.java.JavaOneToManyMapping;
import org.eclipse.jpt.core.context.java.JavaOneToManyRelationshipReference;
import org.eclipse.jpt.core.resource.java.OneToManyAnnotation;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public abstract class AbstractJavaOneToManyRelationshipReference
	extends AbstractJavaRelationshipReference
	implements JavaOneToManyRelationshipReference
{
	protected final JavaMappedByJoiningStrategy mappedByJoiningStrategy;
	
	protected final JavaJoinTableJoiningStrategy joinTableJoiningStrategy;
	
	
	protected AbstractJavaOneToManyRelationshipReference(JavaOneToManyMapping parent) {
		super(parent);
		this.mappedByJoiningStrategy = buildMappedByJoiningStrategy();
		this.joinTableJoiningStrategy = buildJoinTableJoiningStrategy();
	}
	
	protected JavaMappedByJoiningStrategy buildMappedByJoiningStrategy() {
		return new GenericJavaMappedByJoiningStrategy(this);
	}
	
	protected JavaJoinTableJoiningStrategy buildJoinTableJoiningStrategy() {
		return new GenericJavaJoinTableJoiningStrategy(this);
	}
	
	@Override
	public JavaOneToManyMapping getRelationshipMapping() {
		return (JavaOneToManyMapping) getParent();
	}
	
	public OneToManyAnnotation getMappingAnnotation() {
		return getRelationshipMapping().getMappingAnnotation();
	}
	
	public boolean isRelationshipOwner() {
		return this.getMappedByJoiningStrategy().getMappedByAttribute() == null;
	}
	
	public boolean isOwnedBy(RelationshipMapping mapping) {
		return this.mappedByJoiningStrategy.relationshipIsOwnedBy(mapping);
	}
	
	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result == null) {
			result = this.mappedByJoiningStrategy.javaCompletionProposals(pos, filter, astRoot);
		}
		if (result == null) {
			result = this.joinTableJoiningStrategy.javaCompletionProposals(pos, filter, astRoot);
		}
		return result;
	}
	
	
	// **************** mapped by **********************************************
	
	public JavaMappedByJoiningStrategy getMappedByJoiningStrategy() {
		return this.mappedByJoiningStrategy;
	}
	
	public final void setMappedByJoiningStrategy() {
		setMappedByJoiningStrategy_();
		setPredominantJoiningStrategy();
	}
	
	protected void setMappedByJoiningStrategy_() {
		this.mappedByJoiningStrategy.addStrategy();
		this.joinTableJoiningStrategy.removeStrategy();
	}
	
	public final void unsetMappedByJoiningStrategy() {
		unsetMappedByJoiningStrategy_();
		setPredominantJoiningStrategy();
	}
	
	protected void unsetMappedByJoiningStrategy_() {
		this.mappedByJoiningStrategy.removeStrategy();
	}
	
	public boolean usesMappedByJoiningStrategy() {
		return this.getPredominantJoiningStrategy() == this.mappedByJoiningStrategy;
	}
	
	public boolean mayBeMappedBy(AttributeMapping mappedByMapping) {
		return mappedByMapping.getKey() == MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}
	
	
	// **************** join table *********************************************
	
	public JavaJoinTableJoiningStrategy getJoinTableJoiningStrategy() {
		return this.joinTableJoiningStrategy;
	}
	
	public boolean usesJoinTableJoiningStrategy() {
		return getPredominantJoiningStrategy() == this.joinTableJoiningStrategy;
	}
	
	public final void setJoinTableJoiningStrategy() {
		setJoinTableJoiningStrategy_();
		setPredominantJoiningStrategy();
	}
	
	protected void setJoinTableJoiningStrategy_() {
		// join table is default, so no need to add annotation
		this.mappedByJoiningStrategy.removeStrategy();
	}
	
	public final void unsetJoinTableJoiningStrategy() {
		unsetJoinTableJoiningStrategy_();
		setPredominantJoiningStrategy();
	}
	
	protected void unsetJoinTableJoiningStrategy_() {
		this.joinTableJoiningStrategy.removeStrategy();
	}
	
	public boolean mayHaveDefaultJoinTable() {
		return this.mappedByJoiningStrategy.getMappedByAttribute() == null;
	}
	
	
	// **************** resource => context ************************************
	
	@Override
	protected void initializeJoiningStrategies() {
		this.mappedByJoiningStrategy.initialize();
		this.joinTableJoiningStrategy.initialize();
	}
	
	@Override
	protected void updateJoiningStrategies() {
		this.mappedByJoiningStrategy.update();
		this.joinTableJoiningStrategy.update();
	}
	
	
	// **************** Validation *********************************************
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		this.mappedByJoiningStrategy.validate(messages, reporter, astRoot);
		this.joinTableJoiningStrategy.validate(messages, reporter, astRoot);
	}
}
