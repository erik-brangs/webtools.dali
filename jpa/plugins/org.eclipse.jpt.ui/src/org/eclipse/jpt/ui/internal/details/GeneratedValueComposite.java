/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.details;

import java.util.Collection;

import org.eclipse.jpt.core.context.GeneratedValue;
import org.eclipse.jpt.core.context.GenerationType;
import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.ui.internal.JpaHelpContextIds;
import org.eclipse.jpt.ui.internal.listeners.SWTListChangeListenerWrapper;
import org.eclipse.jpt.ui.internal.listeners.SWTPropertyChangeListenerWrapper;
import org.eclipse.jpt.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.internal.ArrayTools;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.model.event.ListChangeEvent;
import org.eclipse.jpt.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.model.listener.ListChangeAdapter;
import org.eclipse.jpt.utility.model.listener.ListChangeListener;
import org.eclipse.jpt.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |                 --------------------------------------------------------- |
 * | Strategy:       | I                                                   |v| |
 * |                 --------------------------------------------------------- |
 * |                 --------------------------------------------------------- |
 * | Generator Name: | I                                                   |v| |
 * |                 --------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see IdMapping
 * @see GeneratedValue
 * @see IdMappingGenerationComposite - The parent container
 *
 * @version 2.2
 * @since 1.0
 */
@SuppressWarnings("nls")
public class GeneratedValueComposite extends Pane<IdMapping>
{
	private PropertyChangeListener generatedValuePropertyChangeListener;
	private Combo generatorNameCombo;
	private PropertyChangeListener generatorNamePropertyChangeListener;
	private ListChangeListener generatorsListChangeListener;

	/**
	 * Creates a new <code>GeneratedValueComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public GeneratedValueComposite(Pane<? extends IdMapping> parentPane,
	 	                            Composite parent) {

		super(parentPane, parent);
	}

	private PropertyChangeListener buildGeneratedValuePropertyChangeListener() {
		return new SWTPropertyChangeListenerWrapper(
			buildGeneratedValuePropertyChangeListener_()
		);
	}

	private PropertyChangeListener buildGeneratedValuePropertyChangeListener_() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				disengageListeners((GeneratedValue) e.getOldValue());
				engageListeners((GeneratedValue) e.getNewValue());

				if (!isPopulating()) {
					setPopulating(true);

					try {
						populateGeneratorNameCombo();
					}
					finally {
						setPopulating(false);
					}
				}
			}
		};
	}

	private ModifyListener buildGeneratorNameModifyListener() {
		return new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isPopulating()) {
					return;
				}

				String generatorName = ((Combo) e.getSource()).getText();
				GeneratedValue generatedValue = getSubject().getGeneratedValue();

				if (StringTools.stringIsEmpty(generatorName)) {

					if ((generatedValue == null) ||
					    StringTools.stringIsEmpty(generatedValue.getGenerator()))
					{
						return;
					}

					generatorName = null;
				}

				retrieveGeneratedValue().setSpecifiedGenerator(generatorName);
			}
		};
	}

	private PropertyChangeListener buildGeneratorNamePropertyChangeListener() {
		return new SWTPropertyChangeListenerWrapper(
			buildGeneratorNamePropertyChangeListener_()
		);
	}

	private PropertyChangeListener buildGeneratorNamePropertyChangeListener_() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				if (!isPopulating()) {
					setPopulating(true);

					try {
						populateGeneratorName();
					}
					finally {
						setPopulating(false);
					}
				}
			}
		};
	}

	private ListChangeListener buildGeneratorsListChangeListener() {
		return new SWTListChangeListenerWrapper(
			buildGeneratorsListChangeListener_());
	}

	private ListChangeListener buildGeneratorsListChangeListener_() {
		return new ListChangeAdapter() {
			@Override
			// should only have to listen to this event - others aren't created
			public void listChanged(ListChangeEvent event) {
				if (! isPopulating()) {
					setPopulating(true);

					try {
						populateGeneratorChoices();
					}
					finally {
						setPopulating(false);
					}
				}

			}
		};
	}

	private PropertyValueModel<GeneratedValue> buildGeneratorValueHolder() {
		return new PropertyAspectAdapter<IdMapping, GeneratedValue>(getSubjectHolder(), IdMapping.GENERATED_VALUE_PROPERTY) {
			@Override
			protected GeneratedValue buildValue_() {
				return getSubject().getGeneratedValue();
			}
		};
	}

	private EnumFormComboViewer<GeneratedValue, GenerationType> addStrategyComboViewer(Composite parent) {

		return new EnumFormComboViewer<GeneratedValue, GenerationType>(this, buildGeneratorValueHolder(), parent) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(GeneratedValue.DEFAULT_STRATEGY_PROPERTY);
				propertyNames.add(GeneratedValue.SPECIFIED_STRATEGY_PROPERTY);
			}

			@Override
			protected GenerationType[] getChoices() {
				return GenerationType.values();
			}

			@Override
			protected GenerationType getDefaultValue() {
				return getSubject().getDefaultStrategy();
			}

			@Override
			protected String displayString(GenerationType value) {
				return buildDisplayString(
					JptUiDetailsMessages.class,
					GeneratedValueComposite.this,
					value
				);
			}

			@Override
			protected GenerationType getValue() {
				return getSubject().getSpecifiedStrategy();
			}

			@Override
			protected void setValue(GenerationType value) {
				retrieveGeneratedValue().setSpecifiedStrategy(value);
			}
		};
	}

	@Override
	protected void doPopulate() {
		super.doPopulate();
		populateGeneratorNameCombo();
	}

	@Override
	protected void engageListeners_(IdMapping subject) {
		super.engageListeners_(subject);
		subject.addPropertyChangeListener(IdMapping.GENERATED_VALUE_PROPERTY, this.generatedValuePropertyChangeListener);
		this.engageListeners(subject.getGeneratedValue());
	}

	private void engageListeners(GeneratedValue generatedValue) {
		if (generatedValue != null) {
			this.engageListeners_(generatedValue);
		}
	}

	private void engageListeners_(GeneratedValue generatedValue) {
		generatedValue.getPersistenceUnit().addListChangeListener(PersistenceUnit.GENERATORS_LIST, this.generatorsListChangeListener);
		generatedValue.addPropertyChangeListener(GeneratedValue.DEFAULT_GENERATOR_PROPERTY, this.generatorNamePropertyChangeListener);
		generatedValue.addPropertyChangeListener(GeneratedValue.SPECIFIED_GENERATOR_PROPERTY, this.generatorNamePropertyChangeListener);
	}

	@Override
	protected void disengageListeners_(IdMapping subject) {
		this.disengageListeners(subject.getGeneratedValue());
		subject.removePropertyChangeListener(IdMapping.GENERATED_VALUE_PROPERTY, this.generatedValuePropertyChangeListener);
		super.disengageListeners_(subject);
	}

	private void disengageListeners(GeneratedValue generatedValue) {
		if (generatedValue != null) {
			this.disengageListeners_(generatedValue);
		}
	}

	private void disengageListeners_(GeneratedValue generatedValue) {
		generatedValue.removePropertyChangeListener(GeneratedValue.SPECIFIED_GENERATOR_PROPERTY, this.generatorNamePropertyChangeListener);
		generatedValue.removePropertyChangeListener(GeneratedValue.DEFAULT_GENERATOR_PROPERTY, this.generatorNamePropertyChangeListener);
		generatedValue.getPersistenceUnit().removeListChangeListener(PersistenceUnit.GENERATORS_LIST, this.generatorsListChangeListener);
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.generatedValuePropertyChangeListener = buildGeneratedValuePropertyChangeListener();
		this.generatorNamePropertyChangeListener  = buildGeneratorNamePropertyChangeListener();
		this.generatorsListChangeListener = buildGeneratorsListChangeListener();
	}

	@Override
	protected void initializeLayout(Composite container) {

		// Strategy widgets
		addLabeledComposite(
			container,
			JptUiDetailsMessages.GeneratedValueComposite_strategy,
			addStrategyComboViewer(container),
			JpaHelpContextIds.MAPPING_GENERATED_VALUE_STRATEGY
		);

		// Generator Name widgets
		this.generatorNameCombo = addLabeledEditableCombo(
			container,
			JptUiDetailsMessages.GeneratedValueComposite_generatorName,
			buildGeneratorNameModifyListener(),
			JpaHelpContextIds.MAPPING_GENERATED_VALUE_STRATEGY
		);

		this.generatorNameCombo.add(JptUiDetailsMessages.DefaultEmpty);
	}

	private void populateGeneratorChoices() {
		if (this.generatorNameCombo.isDisposed()) {
			return;
		}
		if (getSubject() == null) {
			this.generatorNameCombo.setItems(new String[0]);
		}
		else {
			this.generatorNameCombo.setItems(this.sortedUniqueGeneratorNames());
		}
	}

	private void populateGeneratorName() {
		if (this.generatorNameCombo.isDisposed()) {
			return;
		}
		if (getSubject() == null) {
			this.generatorNameCombo.setText("");
		}
		else {
			GeneratedValue generatedValue = getSubject().getGeneratedValue();

			if (generatedValue == null) {
				this.generatorNameCombo.setText("");
			}
			else {
				String generatorName = generatedValue.getGenerator();

				if (StringTools.stringIsEmpty(generatorName)) {
					this.generatorNameCombo.setText("");
				}
				else if (!this.generatorNameCombo.getText().equals(generatorName)) {
					this.generatorNameCombo.setText(generatorName);
				}
			}
		}
	}

	private void populateGeneratorNameCombo() {
		populateGeneratorName();
		populateGeneratorChoices();
	}

	private GeneratedValue retrieveGeneratedValue() {
		GeneratedValue generatedValue = getSubject().getGeneratedValue();

		if (generatedValue == null) {
			setPopulating(true);

			try {
				generatedValue = getSubject().addGeneratedValue();
			}
			finally {
				setPopulating(false);
			}
		}

		return generatedValue;
	}

	private String[] sortedUniqueGeneratorNames() {
		return ArrayTools.sort(this.getSubject().getPersistenceUnit().uniqueGeneratorNames());
	}

}
