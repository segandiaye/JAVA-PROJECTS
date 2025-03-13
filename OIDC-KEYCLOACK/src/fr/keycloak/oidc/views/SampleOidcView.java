package fr.keycloak.oidc.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fr.keycloak.oidc.auth.OIDCAuthenticator;

public class SampleOidcView extends ViewPart {
	public static final String ID = "fr.keycloak.oidc.views";

	@Override
	public void createPartControl(Composite parent) {
		// Create UI elements, e.g., a button to start authentication
		Button loginButton = new Button(parent, SWT.PUSH);
		loginButton.setText("Login with OIDC keycloak");
		loginButton.addListener(SWT.Selection, event -> {
			OIDCAuthenticator authenticator = new OIDCAuthenticator();
			authenticator.authenticate();
		});
	}

	@Override
	public void setFocus() {
		// Set focus to the UI component if needed
	}
}
