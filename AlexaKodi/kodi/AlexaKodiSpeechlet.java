/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package kodi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class AlexaKodiSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(AlexaKodiSpeechlet.class);

    private static final String URL = "https://archania.net";
    private static final String PORT = "8000";
    
    private static final String USERNAME = "kodi";
    private static final String PASSWORD = "murcielago";
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("PlayIntent".equals(intentName)) {
            return getPlayResponse();
        } else if ("PauseIntent".equals(intentName)) {
            return getPauseResponse();
        } else if ("StopIntent".equals(intentName)) {
            return getStopResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getStopResponse() {
        String speechText = "Stopping playback";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Kodi Stop");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        try {
        	sendPost("stop");
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }

        return SpeechletResponse.newTellResponse(speech, card);
	}

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getPauseResponse() {
        String speechText = "Pausing playback.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Kodi Pause");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        try {
        	sendPost("pause");
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }

        return SpeechletResponse.newTellResponse(speech, card);
	}

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getPlayResponse() {
        String speechText = "Starting playback.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Kodi Play");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        try {
        	sendPost("play");
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }

        return SpeechletResponse.newTellResponse(speech, card);
	}

	@Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Alexa Skills Kodi Control";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Alexa Kodi Voice Control");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }


    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can control playback to kodi. Say play, pause, or stop.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Kodi");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    private String sendPost(String args) throws MalformedURLException, IOException {
    	String url = URL + ":" + PORT +"/kodi";
    	String param = "u=" + USERNAME + "&p=" + PASSWORD + "&args=" + args;
    	
    	log.info("URL: " + url);
    	log.info("params: " + param);
    	
    	byte[] postData = param.getBytes( StandardCharsets.UTF_8 );
    	
    	URL address = new URL(url);
    	HttpsURLConnection connection = (HttpsURLConnection)address.openConnection();
    	
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        connection.setRequestProperty( "charset", "utf-8");
        connection.setRequestProperty( "Content-Length", String.valueOf(postData.length));
        connection.setDoOutput( true );

        connection.getOutputStream().write(postData);
        
        int responseCode = connection.getResponseCode();
        log.info("POST Response Code :: " + responseCode);
 
        if (responseCode == HttpsURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
 
            // print result
            log.info(response.toString());
            return response.toString();
        } else {
            log.warn("POST request failed.");
            return null;
        }
        
        
    }
}
