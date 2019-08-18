package de.ativelox.relentlessfisher.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Provides a Utility class to formulate HTTP requests.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class HTTPRequest {

    /**
     * Parses the given string as a JSON object on the "top-level-view". Meaning
     * that key-value pairs only get parsed on the first "layer" of the JSON object,
     * e.g. arrays in JSON objects as values will not be properly encoded, and
     * rather just be parsed as their JSON equivalent in a string.
     * 
     * 
     * @param json The json to parse to a map.
     * @return A map representing the top-level-view of the given json.
     */
    private static Map<String, String> parseSimpleJSON(final String json) {
	int depth = 0;
	String current = "";

	final List<String> contents = new ArrayList<>();

	for (final char s : json.toCharArray()) {
	    if (s == '{' || s == '[') {
		depth++;
	    } else if (s == '}' || s == ']') {
		depth--;

	    }

	    if (depth <= 1) {
		if (s == ':' || s == ',' || s == '}') {
		    contents.add(current);
		    current = "";
		    continue;
		}
		if (s == '\"' || s == '{') {
		    continue;
		}
	    }
	    current += s;

	}

	final Map<String, String> mapping = new HashMap<>();

	for (int i = 0; i < contents.size(); i += 2) {
	    mapping.put(contents.get(i), contents.get(i + 1));

	}
	return mapping;
    }

    /**
     * Sends a POST request to the given URL with the given contents. This method
     * expects for the server to respond with a JSON object (request was proper),
     * and returns that.
     * 
     * @param url     The url on which to perform a POST request.
     * @param content The content to send with the POST request.
     * @return A JSON objects representation of the servers response.
     * @throws IOException if an I/O exception occurs.
     */
    public static Map<String, String> Post(final String url, final String content) throws IOException {
	final URL formedUrl = new URL(url);
	final HttpsURLConnection connection = (HttpsURLConnection) formedUrl.openConnection();

	connection.setRequestMethod("POST");
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setUseCaches(false);
	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	connection.setRequestProperty("Content-Length", String.valueOf(content.length()));

	final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	writer.write(content);
	writer.flush();

	final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

	String answer = "";

	String line = reader.readLine();

	while (line != null) {
	    answer += line;
	    line = reader.readLine();

	}
	return parseSimpleJSON(answer);

    }
}
