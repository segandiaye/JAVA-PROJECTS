package fr.keycloak.oidc.auth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.keycloak.oidc.util.KeyCloackUtilities;
import fr.keycloak.oidc.util.PropertiesUtilities;

public class OIDCAuthenticator {

	private static final String CLIENT_ID;
	private static final String CLIENT_SECRET;
	private static final String REALM;
	private static final String GRANT_TYPE;
	private static final String ISSUER;
	private static final String REDIRECT_URI;
	private static final String AUTHORIZATION_ENDPOINT;
	private static final String TOKEN_ENDPOINT;
	private static final String USERINFO_ENDPOINT;
	private static final String SCOPES;
	private static final String RESPONSE_TYPE;

	private boolean isShellClosed = false;

	static {
		// Read properties
		PropertiesUtilities app = new PropertiesUtilities();
		Properties props = app.loadPropertiesFile("resources/application.properties");

		CLIENT_ID = props.getProperty("client_id");
		CLIENT_SECRET = props.getProperty("client_secret");
		REALM = props.getProperty("realm");
		GRANT_TYPE = props.getProperty("grant_type");
		ISSUER = props.getProperty("issuer");
		REDIRECT_URI = props.getProperty("redirect_uri");
		SCOPES = props.getProperty("scopes");
		RESPONSE_TYPE = props.getProperty("response_type");

		// Keycloack
		AUTHORIZATION_ENDPOINT = KeyCloackUtilities.getAuthEndPoint(ISSUER, REALM);
		TOKEN_ENDPOINT = KeyCloackUtilities.getTokenEndPoint(ISSUER, REALM);
		USERINFO_ENDPOINT = KeyCloackUtilities.getUserInfoEndPoint(ISSUER, REALM);
	}

	/**
	 * Authentication UI
	 */
	public void authenticate() {
		Display display = PlatformUI.getWorkbench().getDisplay();
		if (display != null) {
			final String authUrl = AUTHORIZATION_ENDPOINT + "?response_type=" + RESPONSE_TYPE + "&client_id="
					+ CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=" + SCOPES;
			
			// TODO: remove it not needed
//			final String token = getTokenWithPassword("first_user", "first_user");
//			decodeToken(token);
//			getGrant();

			Shell shell = new Shell(display);
			shell.setText("SNCF keycloack OIDC");
			shell.setSize(600, 600);
			centerShell(display, shell);

			shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent e) {
					isShellClosed = true;
				}
			});

			Browser browser = new Browser(shell, SWT.NONE);
			browser.setSize(600, 600);
			browser.setUrl(authUrl);

			browser.addProgressListener(new ProgressAdapter() {
				@Override
				public void completed(ProgressEvent event) {
					String url = browser.getUrl();
					if (url.startsWith(REDIRECT_URI)) {
						String code = extractAuthorizationCode(url);
						if (code != null) {
							// Using a thread to handle network operations to avoid blocking the UI thread
							exchangeCodeForToken(code, shell);
//							shell.close();
//							new Thread(() -> {
//								exchangeCodeForToken(code, shell);
//								shell.close();
//							}).start();
						}
					}
				}
			});

			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

//			display.dispose();
		}
	}

	/**
	 * 
	 * @param display
	 * @param shell
	 */
	private void centerShell(Display display, Shell shell) {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private String extractAuthorizationCode(String url) {
		String code = null;
		try {
			java.net.URL aURL = new java.net.URL(url);
			String query = aURL.getQuery();
			String[] params = query.split("&");
			for (String param : params) {
				if (param.startsWith("code=")) {
					code = param.substring(5);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * Get token with code
	 * 
	 * @param code
	 * @param shell
	 */
	private void exchangeCodeForToken(String code, Shell shell) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(TOKEN_ENDPOINT);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			// Set the parameters for the request
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("client_id", CLIENT_ID));
			params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			params.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
			params.add(new BasicNameValuePair("code", code));
			params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));

			// Set the entity for the request
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonObject tokenResponse = JsonParser.parseString(jsonResponse).getAsJsonObject();
				String accessToken = tokenResponse.get("access_token").getAsString();
				
				// TODO
//				final String token = getTokenWithPassword("first_user", "first_user");
//				decodeToken(token);

				// Ensure UI updates are performed on the UI thread
				shell.getDisplay().asyncExec(() -> {
					if (!isShellClosed && !shell.isDisposed()) {
						// Use the access token to fetch user info
						getUserInfo(accessToken, shell);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get token with username and password
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private String getTokenWithPassword(String username, String password) {
		String accessToken = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(TOKEN_ENDPOINT);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			// Set the parameters for the request
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("grant_type", "password"));
			params.add(new BasicNameValuePair("client_id", CLIENT_ID));
			params.add(new BasicNameValuePair("scope", SCOPES));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));

			// Set the entity for the request
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonObject tokenResponse = JsonParser.parseString(jsonResponse).getAsJsonObject();
				accessToken = tokenResponse.get("access_token").getAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	/**
	 * GEt user infos
	 * 
	 * @param accessToken
	 * @param shell
	 */
	private void getUserInfo(String accessToken, Shell parentShell) {
		if (parentShell.isDisposed()) {
			return; // Exit if the parent shell is disposed
		}

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet httpGet = new HttpGet(USERINFO_ENDPOINT);
			httpGet.setHeader("Authorization", "Bearer " + accessToken);

			try (CloseableHttpResponse response = client.execute(httpGet)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonObject userInfo = JsonParser.parseString(jsonResponse).getAsJsonObject();

				// Display user info
				parentShell.getDisplay().asyncExec(() -> {
					if (!parentShell.isDisposed()) {
						Shell infoShell = new Shell(parentShell, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
						infoShell.setText("User Informations");
						infoShell.setSize(400, 300);
						centerShell(parentShell.getDisplay(), infoShell);

						String userInfoText = parseUserInfo(userInfo);
						org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(infoShell,
								SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
						text.setText(userInfoText);
						text.setBounds(10, 10, 380, 250);

						infoShell.open();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parsing user Infos
	 * 
	 * @param userInfo
	 * @return
	 */
	private String parseUserInfo(JsonObject userInfo) {
		StringBuilder userInfoText = new StringBuilder();
		for (String key : userInfo.keySet()) {
			JsonElement value = userInfo.get(key);
			userInfoText.append(key).append(": ").append(value.toString()).append("\n");
		}
		return userInfoText.toString();
	}

	/**
	 * 
	 * @return
	 */
	private void getGrant() {
		String authorizationUrl = String.format("%s?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s",
				AUTHORIZATION_ENDPOINT, RESPONSE_TYPE, CLIENT_ID, REDIRECT_URI, "openid");

		CookieStore cookieStore = new BasicCookieStore();

		try (CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();) {
			HttpClientContext context = HttpClientContext.create();
			context.setCookieStore(cookieStore);

			HttpGet httpGet = new HttpGet(authorizationUrl);
			httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

			try (CloseableHttpResponse response = client.execute(httpGet, context)) {
				if (response.getStatusLine().getStatusCode() == 302) { // Handle redirect
					String location = response.getFirstHeader("Location").getValue();
					System.out.println("Redirect Location: " + location);

					// Extract the authorization code from the location URL
					String authorizationCode = extractAuthorizationCode(location);
					System.out.println("Authorization Code: " + authorizationCode);

				} else {
					System.out.println("Unexpected response status: " + response.getStatusLine().getStatusCode());
					String responseBody = EntityUtils.toString(response.getEntity());
					final String authUrlToPost = getAuthUrlToPost(responseBody);
					// TODO
					autoConnexion(authUrlToPost, cookieStore);
					// System.out.println("Response body: " + responseBody);
				}
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param htmlText
	 * @return
	 */
	private String getAuthUrlToPost(String htmlText) {
		// Parse the HTML string
		Document document = Jsoup.parse(htmlText);

		// Extract elements with the <form> tag
		Elements forms = document.select("form");

//        // Iterate over the elements and print their attributes
//        for (Element form : forms) {
//        	String id = form.attr("id");
//            String action = form.attr("action");
//            System.out.println("Form id : " + id);
//            System.out.println("Form action : " + action);
//            System.out.println();
//        }

		if (forms != null && !forms.isEmpty()) {
			return forms.get(0).attr("action");
		}
		return null;
	}

	/**
	 * To connect without UI
	 * 
	 * @param cookies
	 * @param authUrlToPost
	 */
	private void autoConnexion(String authUrlToPost, CookieStore cookieStore) {
		try (CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build()) {
			HttpClientContext context = HttpClientContext.create();

			HttpPost httpPost = new HttpPost(authUrlToPost);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			// Adding cookies to the CookieStore
//			cookieStore = getCookies(listCookies, cookieStore);
			context.setCookieStore(cookieStore);

			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("grant_type", "password"));
//            params.add(new BasicNameValuePair("client_id", CLIENT_ID));
//            params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			params.add(new BasicNameValuePair("credentialId", "aa2e3c0f-62ac-4424-8ba8-41668b555bd0"));
			params.add(new BasicNameValuePair("username", "first_user"));
			params.add(new BasicNameValuePair("password", "first_user"));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			try (CloseableHttpResponse response = client.execute(httpPost, context)) {
				String responseBody = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get cookies from the response
	 * 
	 * @param cookieStore
	 * @return
	 */
	private static String toStringallCookies(CookieStore cookieStore) {
		return cookieStore.getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue())
				.collect(Collectors.joining(";"));
	}

	/**
	 * 
	 * @param cookieStore
	 * @return
	 */
	private CookieStore getCookies(List<Cookie> listCookies, CookieStore cookieStore) {
		listCookies.forEach(cookie -> {
			final BasicClientCookie basicCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
			basicCookie.setDomain(cookie.getDomain());
			basicCookie.setPath(cookie.getPath());
			cookieStore.addCookie(basicCookie);
		});
		return cookieStore;
	}
	
	/**
	 * 
	 * @param token
	 */
	private static void decodeToken(String token) {
        String[] parts = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

        System.out.println("Decoded token payload: " + jsonObject);

        // Extract roles and groups
        if (jsonObject.has("realm_access")) {
            JsonObject realmAccess = jsonObject.getAsJsonObject("realm_access");
            System.out.println("Realm Roles: " + realmAccess.get("roles"));
        }

        if (jsonObject.has("resource_access")) {
            JsonObject resourceAccess = jsonObject.getAsJsonObject("resource_access");
            System.out.println("Client Roles: " + resourceAccess.getAsJsonObject(CLIENT_ID).get("roles"));
        }

        if (jsonObject.has("groups")) {
            System.out.println("Groups: " + jsonObject.get("groups"));
        }
    }

}
