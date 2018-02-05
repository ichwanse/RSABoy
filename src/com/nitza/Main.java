package com.nitza;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Version 0.1
 * Some commands that can be execute in this application;
 * - creating private key dan public key
 * - encrypt
 * - decrypt
 * - sign
 * - verify
 *
 *
 */

public class Main {

    public static void main(String[] args) throws Exception {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        System.out.println("Input?");
        String input = br.readLine();

        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(input).getAsJsonObject();

        String command = rootObj.get("command").getAsString();

        /**
         * Ex;
         * {"command": "generate_key", "filename": "OrangA"}
         *
         * {
         *     "command": "generate_key",
         *     "filename": "OrangA"
         * }
         */
        if (command.equals("generate_key")) {

            // filename
            String filename = rootObj.get("filename").getAsString();

            RSABoy RSABoy = new RSABoy();
            RSABoy.generateKey(filename);
        }

        /**
         * Ex;
         * {"command": "encrypt", "message": "encrypt ini", "pubkey_file": "OrangA.pub.pem"}
         *
         * {
         *     "command": "encrypt",
         *     "message": "encrypt ini",
         *     "pubkey_file": "OrangA.pub.pem"
         * }
         */
        else if (command.equals("encrypt") ) {
            String stringToEncrypt = rootObj.get("message").getAsString();
            String pubkeyFile = rootObj.get("pubkey_file").getAsString();

            // counting byte length
            int messageByteLength = stringToEncrypt.getBytes().length;
            if(messageByteLength < 245) {
                RSABoy RSABoy = new RSABoy();
                String encryptedString = RSABoy.encrypt(stringToEncrypt, pubkeyFile);
                System.out.println(encryptedString);
            }
            else {
                System.out.println("Failed. Your message is " + messageByteLength + " bytes. The message can't be longer than 245 bytes");
            }
        }

        /**
         * Ex;
         * {"command": "sign","message": "encrypt ini", "privkey_file": "OrangB.priv.pem"}
         *
         * {
         *     "command": "sign",
         *     "message": "encrypt ini",
         *     "privkey_file": "OrangB.priv"
         * }
         */
        else if (command.equals("sign") ) {
            String stringToSign = rootObj.get("message").getAsString();
            String privkeyFile = rootObj.get("privkey_file").getAsString();

            RSABoy RSABoy = new RSABoy();
            String signString = RSABoy.sign(stringToSign, privkeyFile);
            System.out.println(signString);
        }

        /**
         * Ex;
         * {"command": "decrypt", "message": "a0p0bdfxeqrBmxV5vCp/vk37E4xq6MlIfk+TnT4WIXm9LQvfZn8FSNngCrhjWYsmUlE7C8LnGszitcvJ7baKTvMgG8J1vQSc+vVnUrhO1UqY0qaKtRFZh0rmcUcc9jFwKn1JsWT2kcxZEfaAFoYpHSmlUI3z5bou4cl9pSXCstmZPSfRQ0TlqumTDu7B/4vSUlEdEDOjs4EpNwadfbfWky2NUXXJTsLo653yfnhIV3TYHZUkKG8kvj6jAJbYwg37Zlp3sjtI9m+yW8G/Ls3yi7kuT3XY0gZApuPmp05VMoHFHbzPdJPXIev02hYfwpqCeWi9QLekcYrGjE0nLAnH/g==", "privkey_file": "OrangA.priv.pem"}
         *
         * {
         *     "command": "decrypt",
         *     "message": "a0p0bdfxeqrBmxV5vCp/vk37E4xq6MlIfk+TnT4WIXm9LQvfZn8FSNngCrhjWYsmUlE7C8LnGszitcvJ7baKTvMgG8J1vQSc+vVnUrhO1UqY0qaKtRFZh0rmcUcc9jFwKn1JsWT2kcxZEfaAFoYpHSmlUI3z5bou4cl9pSXCstmZPSfRQ0TlqumTDu7B/4vSUlEdEDOjs4EpNwadfbfWky2NUXXJTsLo653yfnhIV3TYHZUkKG8kvj6jAJbYwg37Zlp3sjtI9m+yW8G/Ls3yi7kuT3XY0gZApuPmp05VMoHFHbzPdJPXIev02hYfwpqCeWi9QLekcYrGjE0nLAnH/g==",
         *     "privkey_file": "OrangA.priv.pem"
         * }
         *
         * Explanation:
         * message = base64 string
         */
        else if (command.equals("decrypt") ) {
            String stringToDecrypt = rootObj.get("message").getAsString();
            String privkeyFile = rootObj.get("privkey_file").getAsString();

            RSABoy RSABoy = new RSABoy();
            String decryptedString = RSABoy.decrypt(stringToDecrypt, privkeyFile);
            System.out.println(decryptedString);
        }


        /**
         * Ex;
         * {"command": "verify", "message":"encrypt ini", "signature": "AK2f1WWM/RWsWj1iXnPT6uRgYyLwOBMckCGpge9o4a5htmjVSJl0fG8AUW82qYzbHUvdjy+Sg3nWDCDwZ8vgvcLs7DkX48aEsOP9uK0J1FTcTfip/w+gXxTSu5Pt5BPJogo/ln2wYCc0nLlSFygam1tXfhhXpGnOoeQEDhsI6G3MS5Nypyhj9Ye6WyR9tPUYGo7stG999YENVKXh7GkdB+6UEexhDMW8Rz5qRR7Rt+5uzHm5SO3ftkaqKqs5C0XU2nuHuqq5pDDR0a1feugbwebegDihf7FUyM8vW26g75soqLlQ/lO55r3OBmZFRozQ5yZXgbUFKqUkSb+sYTH4kw==", "pubkey_file": "OrangB.pub.pem"}
         *
         * {
         *     "command": "verify",
         *     "message":"encrypt ini",
         *     "signature": "AK2f1WWM/RWsWj1iXnPT6uRgYyLwOBMckCGpge9o4a5htmjVSJl0fG8AUW82qYzbHUvdjy+Sg3nWDCDwZ8vgvcLs7DkX48aEsOP9uK0J1FTcTfip/w+gXxTSu5Pt5BPJogo/ln2wYCc0nLlSFygam1tXfhhXpGnOoeQEDhsI6G3MS5Nypyhj9Ye6WyR9tPUYGo7stG999YENVKXh7GkdB+6UEexhDMW8Rz5qRR7Rt+5uzHm5SO3ftkaqKqs5C0XU2nuHuqq5pDDR0a1feugbwebegDihf7FUyM8vW26g75soqLlQ/lO55r3OBmZFRozQ5yZXgbUFKqUkSb+sYTH4kw==",
         *     "pubkey_file": "OrangB.pub.pem"
         * }
         */
        else if (command.equals("verify") ) {
            String stringToVerify = rootObj.get("message").getAsString();
            String signature = rootObj.get("signature").getAsString();
            String pubkeyFile = rootObj.get("pubkey_file").getAsString();

            RSABoy RSABoy = new RSABoy();
            Boolean hasil = RSABoy.verify(stringToVerify, signature, pubkeyFile );
            System.out.println(hasil);
        }
    }
}
