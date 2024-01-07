package com.omate.liuqu.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.omate.liuqu.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTCreator.Builder;

public class JWTManager {
    private static final String PUBLIC_KEY_FILE = "publicKey.pem";
    private static final String PRIVATE_KEY_FILE = "privateKey.pem";

    private static RSAPublicKey rsaPublicKey;
    private static RSAPrivateKey rsaPrivateKey;
    private static Algorithm algorithm;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void init() {
        try {

            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);

            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);

            algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateAndStoreKeys() {
        // Generate RSA Key Pair
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Specify the key size
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            if (!Files.exists(Paths.get(PUBLIC_KEY_FILE))) {
                // Store the public key to a file
                PublicKey publicKey = keyPair.getPublic();
                byte[] publicKeyBytes = publicKey.getEncoded();
                Files.write(Paths.get(PUBLIC_KEY_FILE), publicKeyBytes);
            }

            if (!Files.exists(Paths.get(PRIVATE_KEY_FILE))) {
                // Store the private key to a file (keep it secure)
                PrivateKey privateKey = keyPair.getPrivate();
                byte[] privateKeyBytes = privateKey.getEncoded();
                Files.write(Paths.get(PRIVATE_KEY_FILE), privateKeyBytes);
            }

            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createToken(Date exp, Map<String, Object> data) {
        Builder builder = JWT.create();

        data.forEach((name, object) -> {
            try {
                builder.withClaim(name, objectMapper.writeValueAsString(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return builder.withExpiresAt(exp).sign(algorithm);
    }

    public static <T> T getDataFromToken(String token, String name, Class<T> type) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
            return objectMapper.readValue(decodedJWT.getClaim(name).asString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkToken(String token, Long id) {

        UserDTO userDTO = JWTManager.getDataFromToken(token, "user", UserDTO.class);

        if (userDTO.getUserId() != id) {
            return false;
        }
        return true;
    }
}
