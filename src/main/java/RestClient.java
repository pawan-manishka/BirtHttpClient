import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawan on 8/26/16.
 */
public class RestClient {

    public static CloseableHttpClient client;
    static List<String> history = new ArrayList<String>();

    public static CloseableHttpClient getHttpsClient() throws Exception {

        if (client != null) {
            return client;
        }
        SSLContext sslcontext = getSSLContext();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        client = HttpClients.custom().setSSLSocketFactory(factory).build();

        return client;
    }

    private static SSLContext getSSLContext() throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {

        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File("/home/pawan/Downloads/stuff/wso2das-3.0.1/" +
                "repository/resources/security/client-truststore.jks"));
        try {
            trustStore.load(instream, "wso2carbon".toCharArray());   // Give keystore password
        } finally {
            instream.close();
        }
        return SSLContexts.custom()
                .loadTrustMaterial(trustStore)
                .build();
    }

    public static List<String> getClientStuff(CloseableHttpClient client){


        try {
            //  String getURL = "https://localhost:9443/analytics/tables"; / https://localhost:9443/analytics/table_exists?table=uni_sys
            String getURL = "https://localhost:9443/analytics/tables/uni_sys";
            HttpGet get = new HttpGet(getURL);

            get.setHeader("Content-Type", "application/json");
            get.setHeader("Authorization", "Basic YWRtaW46YWRtaW4=");

            HttpResponse responseGet = client.execute(get);
            int statusCode = responseGet.getStatusLine().getStatusCode();
            HttpEntity resEntityGet = responseGet.getEntity();

            if (resEntityGet != null) {

                String apiOutput = EntityUtils.toString(resEntityGet);
                //Lets see what we got from API
                System.out.println(apiOutput);

                String split[] = apiOutput.split("\"values\":");

                System.out.println("The split parts of the String are");

                for(String s:split) {
                    history.add(s);
                   System.out.println(s + "\n");
                }
                System.out.println("StatusCode : "+statusCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }


}
