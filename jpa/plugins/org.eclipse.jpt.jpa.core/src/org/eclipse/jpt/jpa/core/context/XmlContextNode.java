/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context;

import java.util.List;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * XML JPA context node
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.2
 * @since 2.1
 */
// TODO bjv rename to XmlJpaContextNode
public interface XmlContextNode
	extends JpaContextNode
{
	/**
	 * Return the content assist proposals for the specified position
	 * in the XML file.
	 */
	Iterable<String> getXmlCompletionProposals(int pos);

	/**
	 * Add to the list of current validation messages
	 */
	void validate(List<IMessage> messages, IReporter reporter);

	TextRange getValidationTextRange();
}
