package io.github.ipl.tfc.docetressabores.ejbs;

import java.io.*;
import java.security.*;

import javax.ejb.Stateless;
import javax.json.*;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

@Stateless(name = "JwtEJB")
public class JwtBean {
	static {
		FileInputStream fis = null;
		char[] password = "pudimdecafe".toCharArray();
		String alias = "alias";
		PrivateKey pk = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			String configDir = System.getProperty("jboss.server.config.dir");
			String keystorePath = configDir + File.separator + "jwt.keystore";

			fis = new FileInputStream(keystorePath);
			ks.load(fis, password);
			Key key = ks.getKey(alias, password);

			if (key instanceof PrivateKey) pk = (PrivateKey) key;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		privateKey = pk;
	}

	private static final PrivateKey privateKey;
	private static final int TOKEN_VALIDITY = 14400;
	private static final String CLAIM_ROLES = "groups";
	private static final String ISSUER = "quickstart-jwt-issuer";
	private static final String AUDIENCE = "jwt-audience";

	public String createJwt(final String subject, final String[] roles) throws JOSEException {
		JWSSigner signer = new RSASSASigner(privateKey);
		JsonArrayBuilder rolesBuilder = Json.createArrayBuilder();

		for (String role : roles) rolesBuilder.add(role);

		JsonObjectBuilder claimsBuilder = Json.createObjectBuilder()
			.add("sub", subject)
			.add("iss", ISSUER)
			.add("aud", AUDIENCE)
			.add(CLAIM_ROLES, rolesBuilder.build())
			.add("exp", ((System.currentTimeMillis() / 1000) + TOKEN_VALIDITY));

		JWSObject jwsObject = new JWSObject(
			new JWSHeader.Builder(JWSAlgorithm.RS256).type(new JOSEObjectType("jwt")).build(),
			new Payload(claimsBuilder.build().toString())
		);

		jwsObject.sign(signer);
		return jwsObject.serialize();
	}
}
