package it.baeyens.arduino.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.PropertyPage;



public class ArduinoPropertyPage extends PropertyPage {
	ArduinoPageLayout myPageLayout = new ArduinoPageLayout();
	String mOriginalMCUProject = "";

	private Listener completeListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			boolean valid = myPageLayout.isPageComplete();
			getApplyButton().setVisible(valid);
			setValid(valid);
		}
	};

	@Override
	protected void performDefaults() {
		myPageLayout.setToDefaults();
		updateApplyButton();
	}

	@Override
	public boolean performOk() {
		if (!isValid())
			return false;
		IResource resource = (IResource) getElement().getAdapter(org.eclipse.core.resources.IResource.class);
		IProject project = resource.getProject();

		if (!ArduinoHelpers.IsStaticLib(project)) {
			ArduinoHelpers.ChangeProjectReference(project, mOriginalMCUProject, ArduinoHelpers.getMCUProjectName(myPageLayout.getMCUName()),
					myPageLayout.getArduinoBoardName());
		}

		myPageLayout.save(project);
		return true;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		IResource resource = (IResource) getElement().getAdapter(org.eclipse.core.resources.IResource.class);
		IProject project = resource.getProject();
		myPageLayout.draw(composite, project);
		mOriginalMCUProject = ArduinoHelpers.getMCUProjectName(myPageLayout.getMCUName());
		setControl(parent);
		Dialog.applyDialogFont(parent);
		myPageLayout.feedbackControl.addListener(SWT.Selection, completeListener);
		Control c = parent;
		return c;
	}

}