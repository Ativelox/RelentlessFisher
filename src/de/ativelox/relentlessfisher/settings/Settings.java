package de.ativelox.relentlessfisher.settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a class to ease configuration file access, parsing a given file to a
 * map.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class Settings {

    /**
     * The internally used map to represent the given file.
     */
    private final Map<String, String> mMap;

    /**
     * Creates a new {@link Settings} object.
     */
    public Settings() {
	mMap = new HashMap<>();

    }

    /**
     * The value for the given key, which was prior specified by a call to
     * {@link Settings#load(String)}.
     * 
     * @param key The key used to identify the value.
     * @return The value for the given key, or <tt>null</tt> if no value exists for
     *         the given key.
     */
    public String get(final String key) {
	return mMap.get(key);

    }

    /**
     * Loads the given file, allowing for access by calling
     * {@link Settings#get(String)}.
     * 
     * @param fileName The relative path to the file, including the file extension.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void load(final String fileName) throws IOException {
	mMap.clear();

	final Path path = Paths.get(fileName);

	if (!Files.exists(path, new LinkOption[0])) {
	    throw new FileNotFoundException("The file " + fileName + " couldn't be found");

	}

	for (final String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
	    final String[] keyValue = line.split("=");
	    mMap.put(keyValue[0], keyValue[1]);
	}
    }
}
