package org.eclipse.jpt.jaxb.eclipselink.core.internal.context.java;

import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.utility.Filter;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.jaxb.core.context.JaxbClass;
import org.eclipse.jpt.jaxb.core.internal.context.java.GenericJavaClassMapping;
import org.eclipse.jpt.jaxb.eclipselink.core.context.java.ELClassMapping;
import org.eclipse.jpt.jaxb.eclipselink.core.context.java.ELXmlDiscriminatorNode;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.java.ELJaxb;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.java.XmlDiscriminatorNodeAnnotation;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;


public class ELJavaClassMapping
		extends GenericJavaClassMapping
		implements ELClassMapping {
	
	protected ELJavaXmlDiscriminatorNode xmlDiscriminatorNode;
	
	
	public ELJavaClassMapping(JaxbClass parent) {
		super(parent);
		initXmlDiscriminatorNode();
	}
	
	
	// ***** sync/update *****
	
	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		syncXmlDiscriminatorNode();
	}
	
	@Override
	public void update() {
		super.update();
		updateXmlDiscriminatorNode();
	}
	
	
	// ***** xmlDiscriminatorNode *****
	
	public ELXmlDiscriminatorNode getXmlDiscriminatorNode() {
		return this.xmlDiscriminatorNode;
	}
	
	protected void setXmlDiscriminatorNode_(ELJavaXmlDiscriminatorNode xmlDiscriminatorNode) {
		ELXmlDiscriminatorNode old = this.xmlDiscriminatorNode;
		this.xmlDiscriminatorNode = xmlDiscriminatorNode;
		firePropertyChanged(XML_DISCRIMINATOR_NODE_PROPERTY, old, xmlDiscriminatorNode);
	}
	
	public ELXmlDiscriminatorNode addXmlDiscriminatorNode() {
		if (this.xmlDiscriminatorNode != null) {
			throw new IllegalStateException();
		}
		getJavaResourceType().addAnnotation(ELJaxb.XML_DISCRIMINATOR_NODE);
		ELJavaXmlDiscriminatorNode xmlDiscriminatorNode = buildXmlDiscriminatorNode();
		setXmlDiscriminatorNode_(xmlDiscriminatorNode);
		return xmlDiscriminatorNode;
	}
	
	public void removeXmlDiscriminatorNode() {
		if (this.xmlDiscriminatorNode == null) {
			throw new IllegalStateException();
		}
		getJavaResourceType().removeAnnotation(ELJaxb.XML_DISCRIMINATOR_NODE);
		setXmlDiscriminatorNode_(null);
	}
	
	public XmlDiscriminatorNodeAnnotation getXmlDiscriminatorNodeAnnotation() {
		return (XmlDiscriminatorNodeAnnotation) getJavaResourceType().getAnnotation(ELJaxb.XML_DISCRIMINATOR_NODE);
	}
	
	protected ELJavaXmlDiscriminatorNode buildXmlDiscriminatorNode() {
		return new ELJavaXmlDiscriminatorNode(this);
	}
	
	protected void initXmlDiscriminatorNode() {
		XmlDiscriminatorNodeAnnotation annotation = getXmlDiscriminatorNodeAnnotation();
		this.xmlDiscriminatorNode = (annotation == null) ? null : buildXmlDiscriminatorNode();
	}
	
	protected void syncXmlDiscriminatorNode() {
		XmlDiscriminatorNodeAnnotation annotation = getXmlDiscriminatorNodeAnnotation();
		if (annotation != null) {
			if (this.xmlDiscriminatorNode != null) {
				this.xmlDiscriminatorNode.synchronizeWithResourceModel();
			}
			else {
				setXmlDiscriminatorNode_(buildXmlDiscriminatorNode());
			}
		}
		else {
			setXmlDiscriminatorNode_(null);
		}
	}
	
	protected void updateXmlDiscriminatorNode() {
		if (this.xmlDiscriminatorNode != null) {
			this.xmlDiscriminatorNode.update();
		}
	}
	
	
	// ***** content assist *****
	
	@Override
	public Iterable<String> getJavaCompletionProposals(
			int pos, Filter<String> filter, CompilationUnit astRoot) {
		
		Iterable<String> result = super.getJavaCompletionProposals(pos, filter, astRoot);
		if (! CollectionTools.isEmpty(result)) {
			return result;
		}
		
		if (this.xmlDiscriminatorNode != null) {
			result = this.xmlDiscriminatorNode.getJavaCompletionProposals(pos, filter, astRoot);
			if (! CollectionTools.isEmpty(result)) {
				return result;
			}
		}
		
		return EmptyIterable.instance();
	}
	
	
	// ***** validation *****
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		
		if (this.xmlDiscriminatorNode != null) {
			this.xmlDiscriminatorNode.validate(messages, reporter, astRoot);
		}
	}
}
