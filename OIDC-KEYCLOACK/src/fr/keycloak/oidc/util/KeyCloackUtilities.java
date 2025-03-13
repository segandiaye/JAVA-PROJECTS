package fr.keycloak.oidc.util;

public final class KeyCloackUtilities {
	private final static KeyCloackUtilities instance = new KeyCloackUtilities();

	public static KeyCloackUtilities getInstance() {
		return instance;
	}
	
	/**
	 * Get auth endpoint
	 * 
	 * @param issuer
	 * @param realm
	 * @return
	 */
	public static String getAuthEndPoint(String issuer, String realm) {
		return issuer + "/realms/" + realm + "/protocol/openid-connect/auth";
	}
	
	/**
	 * Get token endpoint
	 * 
	 * @param issuer
	 * @param realm
	 * @return
	 */
	public static String getTokenEndPoint(String issuer, String realm) {
		return issuer + "/realms/" + realm + "/protocol/openid-connect/token";
	}
	
	/**
	 * Get User infos endpoint
	 * 
	 * @param issuer
	 * @param realm
	 * @return
	 */
	public static String getUserInfoEndPoint(String issuer, String realm) {
		return issuer + "/realms/" + realm + "/protocol/openid-connect/userinfo";
	}
}
