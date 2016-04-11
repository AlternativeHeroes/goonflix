package com.goonsquad.goonflix;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * Created by koushikkrishnan on 4/11/16.
 */
public class RottenTomatoesApiTest {

    private static String rotten_tomatoes_url = "http://api.rottentomatoes.com/api/public/v1.0";

    private final String USER_AGENT = "Mozilla/5.0";

    @Test
    public void test() throws Exception{

        URL obj = new URL(rotten_tomatoes_url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + rotten_tomatoes_url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        assertEquals(false, response.equals(""));
        assertEquals(true, response.lastIndexOf("lists") != -1);
        assertEquals(true, response.lastIndexOf("movies") != -1);


    }

}
