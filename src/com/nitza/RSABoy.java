package com.nitza;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.codec.binary.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RSABoy {


    public void generateKey(String filename) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);


        // menyimpan private key dan public key pada temporary
        RSABoy rsaObj = new RSABoy();
        rsaObj.saveKeys(filename + ".pub", rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
        rsaObj.saveKeys(filename + ".priv", rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());

    }


    private void saveKeys(String fileName, BigInteger mod, BigInteger exp) throws Exception {

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream("keytemp/" + fileName); // letakkan DER pada temporary
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mod);
            oos.writeObject(exp);

            System.out.println(fileName + " generated successfully");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(oos != null) {
                oos.close();
                if(fos != null) {
                    fos.close();
                }
            }

        }

        // file yang sudah di generate disimpan kedalam file pem
        String base64String = Base64.encodeBase64String(loadFileAsBytesArray("keytemp/" + fileName)); // file DER pada temporary dibaca byte-nya, dan di encode kepada base64 string
        PrintWriter out = new PrintWriter(fileName + ".pem"); // menyimpan file output DER kedalam .pem
        out.println(base64String);
        out.close();

    }

    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;
    }

    public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {

        File file = new File(fileName);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(content);
        writer.flush();
        writer.close();

    }

    public PrivateKey readPrivateKeyFromPemFile(String fileName) throws Exception {

        // baca pem file dan decode
        // pem file adalah base64 encode sehingga harus di decode
        byte[] normalByte = Base64.decodeBase64(loadFileAsBytesArray(fileName));

        ByteArrayInputStream bis = new ByteArrayInputStream(normalByte);
        ObjectInputStream in = new ObjectInputStream(bis);
        BigInteger modulus = (BigInteger) in.readObject();
        BigInteger exponent = (BigInteger) in.readObject();

        // Get private key
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);

        return privateKey;

    }

    public PublicKey readPublicKeyFromPemFile(String fileName) throws Exception {

        // baca pem file dan decode
        // pem file adalah base64 encode sehingga harus di decode
        byte[] normalByte = Base64.decodeBase64(loadFileAsBytesArray(fileName));

        ByteArrayInputStream bis = new ByteArrayInputStream(normalByte);
        ObjectInputStream in = new ObjectInputStream(bis);
        BigInteger modulus = (BigInteger) in.readObject();
        BigInteger exponent = (BigInteger) in.readObject();

        // Get public key
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);

        return publicKey;

    }

    public String encrypt(String message, String publicKeyFilename) throws Exception {

        byte[] dataToEncrypt = message.getBytes();

        PublicKey publicKey = readPublicKeyFromPemFile(publicKeyFilename);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(dataToEncrypt);

        String encrypted = Base64.encodeBase64String(encryptedData);

        return encrypted;
    }

    public String decrypt(String encryptedMessage, String privateKeyFilename) throws Exception {
        PrivateKey privateKey = readPrivateKeyFromPemFile(privateKeyFilename);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // encrypted message adalah dari base64 encode
        // sehingga disini harus di decode dulu
        byte[] decodeEncryptedMessage = Base64.decodeBase64(encryptedMessage);

        byte[] decryptedData = cipher.doFinal(decodeEncryptedMessage);

        String str = new String(decryptedData, StandardCharsets.UTF_8);

        return str;
    }

    public String sign(String plainText, String privateKeyFilename) throws Exception {

        PrivateKey privateKey = readPrivateKeyFromPemFile(privateKeyFilename);

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        String signed = Base64.encodeBase64String(signature);

        return signed;
    }

    public Boolean verify(String plainText, String signature, String publicKeyFilename) throws Exception {

        PublicKey publicKey = readPublicKeyFromPemFile(publicKeyFilename);

        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.decodeBase64(signature);

        return publicSignature.verify(signatureBytes);
    }

}
