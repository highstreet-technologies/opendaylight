/*
 * Copyright (C) 2019 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.onap.ccsdk.features.sdnr.wt.odlclient.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseHTTPClient {

    private static final Logger LOG = LoggerFactory.getLogger(BaseHTTPClient.class);
    private static final int SSLCERT_NONE = -1;
    private static final int SSLCERT_PCKS = 0;
    private static final int SSLCERT_PEM = 1;
    private static final int BUFSIZE = 1024;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String SSLCONTEXT = "TLSv1.2";
    private static final int DEFAULT_HTTP_TIMEOUT_MS = 30000; // in ms

    private final boolean trustAll;
    private final String baseUrl;

    private SSLContext sc = null;

    public BaseHTTPClient(String base) {
        this(base, false);
    }

    public BaseHTTPClient(String base, boolean trustAllCerts) {
        this(base, trustAllCerts, null, null, SSLCERT_NONE);
    }

    public BaseHTTPClient(String base, boolean trustAllCerts, String certFilename, String passphrase,
            int sslCertType) {
        if (!base.endsWith("/")) {
            base += "/";
        }
        this.baseUrl = base;
        this.trustAll = trustAllCerts;
        try {
            sc = setupSsl(trustAll, certFilename, passphrase, sslCertType);
        } catch (KeyManagementException | NoSuchAlgorithmException | UnrecoverableKeyException
                | CertificateException | KeyStoreException | IOException | InvalidKeySpecException e) {
            LOG.warn("problem ssl setup: " + e.getMessage());
        }
    }

    public BaseHTTPResponse sendRequest(String uri, final String method, final String body,
            final Map<String, String> headers) throws IOException {
        return this.sendRequest(uri, method, body, headers, DEFAULT_HTTP_TIMEOUT_MS);
    }

    public BaseHTTPResponse sendRequest(String uri, final String method, final String body,
            final Map<String, String> headers, final int timeout) throws IOException {
        return this.sendRequest(uri, method, body != null ? body.getBytes(CHARSET) : null, headers,
                timeout);
    }

    public BaseHTTPResponse sendRequest(String uri, final String method, final byte[] body,
            final Map<String, String> headers, final int timeout) throws IOException {
        if (uri == null) {
            uri = "";
        }
        String surl = this.baseUrl;
        if (!surl.endsWith("/") && uri.length() > 0) {
            surl += "/";
        }
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        surl += uri;
        LOG.debug("try to send request with url=" + this.baseUrl + uri + " as method=" + method);
        LOG.trace("body:" + (body == null ? "null" : new String(body, CHARSET)));
        URL url = new URL(surl);
        URLConnection http = url.openConnection();
        http.setConnectTimeout(timeout);
        if (surl.toString().startsWith("https")) {
            if (sc != null) {
                ((HttpsURLConnection) http).setSSLSocketFactory(sc.getSocketFactory());
                if (trustAll) {
                    LOG.debug("trusting all certs");
                    HostnameVerifier allHostsValid = (hostname, session) -> true;
                    ((HttpsURLConnection) http).setHostnameVerifier(allHostsValid);
                }
            } else {
                // Should never happen
                LOG.warn("No SSL context available");
                return new BaseHTTPResponse(-1, "", "");
            }
        }
        ((HttpURLConnection) http).setRequestMethod(method);
        http.setDoOutput(true);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                http.setRequestProperty(key, headers.get(key));
                LOG.trace("set http header " + key + ": " + headers.get(key));
            }
        }
        byte[] buffer = new byte[BUFSIZE];
        int len = 0;
        int lensum = 0;
        // send request
        // Send the message to destination
        if (!method.equals("GET") && body != null && body.length > 0) {
            try (OutputStream output = http.getOutputStream()) {
                output.write(body);
            }
        }
        // Receive answer
        int responseCode = ((HttpURLConnection) http).getResponseCode();
        String contentType = "";
        String sresponse = "";
        InputStream response = null;
        try {
            if (responseCode >= 200 && responseCode < 300) {
                contentType = http.getHeaderField("Content-Type");
                if (contentType == null) {
                    contentType = "";
                }
                response = http.getInputStream();
            } else {
                response = ((HttpURLConnection) http).getErrorStream();
                if (response == null) {
                    response = http.getInputStream();
                }
            }
            if (response != null) {
                while (true) {
                    len = response.read(buffer, 0, BUFSIZE);
                    if (len <= 0) {
                        break;
                    }
                    lensum += len;
                    sresponse += new String(buffer, 0, len, CHARSET);
                }
            } else {
                LOG.debug("response is null");
            }
        } catch (IOException e) {
            LOG.debug("No response. ", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        LOG.debug("ResponseCode: " + responseCode);
        LOG.trace("Response (len:{}): {}", String.valueOf(lensum), sresponse);
        return new BaseHTTPResponse(responseCode, sresponse, contentType);
    }

    public static SSLContext setupSsl(boolean trustall) throws NoSuchAlgorithmException,
            KeyManagementException, CertificateException, FileNotFoundException, IOException,
            UnrecoverableKeyException, KeyStoreException, InvalidKeySpecException {

        return setupSsl(trustall, null, null, SSLCERT_NONE);
    }



    public static SSLContext setupSsl(boolean trustall, String certFilename, String passPhrase,
            int certType) throws NoSuchAlgorithmException, KeyManagementException, CertificateException,
            FileNotFoundException, IOException, UnrecoverableKeyException, KeyStoreException,
            InvalidKeySpecException {

        SSLContext sc = SSLContext.getInstance(SSLCONTEXT);
        TrustManager[] trustCerts = null;
        if (trustall) {
            trustCerts = new TrustManager[] { new javax.net.ssl.X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                        String authType) {
                }
            } };

        }
        KeyManager[] kms = null;
        if (certFilename != null && passPhrase != null && !certFilename.isEmpty()
                && !passPhrase.isEmpty()) {
            if (certType == SSLCERT_PCKS) {
                LOG.debug("try to load pcks file " + certFilename + " with passphrase=" + passPhrase);
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                FileInputStream fileInputStream = new FileInputStream(certFilename);
                keyStore.load(fileInputStream, passPhrase.toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory
                        .getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, passPhrase.toCharArray());
                kms = kmf.getKeyManagers();
                fileInputStream.close();
                LOG.debug("successful");

            } else if (certType == SSLCERT_PEM) {
                LOG.debug("try to load pem files cert=" + certFilename + " key=" + passPhrase);
                File fcert = new File(certFilename);
                File fkey = new File(passPhrase);
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(null);
                byte[] certBytes = parseDERFromPEM(Files.readAllBytes(fcert.toPath()),
                        "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
                byte[] keyBytes = parseDERFromPEM(Files.readAllBytes(fkey.toPath()),
                        "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

                X509Certificate cert = generateCertificateFromDER(certBytes);
                RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);
                keyStore.setCertificateEntry("cert-alias", cert);
                keyStore.setKeyEntry("key-alias", key, "changeit".toCharArray(),
                        new Certificate[] { cert });

                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(keyStore, "changeit".toCharArray());
                kms = kmf.getKeyManagers();
                LOG.debug("successful");
            }
        }
        // Init the SSLContext with a TrustManager[] and SecureRandom()
        sc.init(kms, trustCerts, new java.security.SecureRandom());
        return sc;
    }

    protected static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }

    protected static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    protected static X509Certificate generateCertificateFromDER(byte[] certBytes)
            throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    public static String getAuthorizationHeaderValue(String username, String password) {
        return "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
    }

    public static int getSslCertPcks() {
        return SSLCERT_PCKS;
    }

    public static int getSslCertNone() {
        return SSLCERT_NONE;
    }

    public static int getSslCertPEM() {
        return SSLCERT_PEM;
    }

}
