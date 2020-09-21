package net.tassia.pancake.http;

import net.tassia.pancake.Pancake;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpView {
	private final String data;

	/* Constructor */
	public HttpView(Path path) {
		try {
			byte[] binData = Files.readAllBytes(path);
			this.data = new String(binData, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new Error("Failed to load view: " + path.toString(), ex);
		}
	}

	public HttpView(String view) {
		byte[] viewData;
		try {
			viewData = Pancake.class.getResourceAsStream(view).readAllBytes();
		} catch (IOException ex) {
			throw new Error("Failed to load view: " + view, ex);
		}
		this.data = new String(viewData, StandardCharsets.UTF_8);
	}

	public HttpView(byte[] view) {
		this.data = new String(view, StandardCharsets.UTF_8);
	}
	/* Constructor */



	/* View */
	public String view(String[]...replacements) {
		String str = data;
		for (String[] replacement : replacements) {
			str = str.replace("{! " + replacement[0] + " !}", replacement[1]);
			str = str.replace("{{ " + replacement[0] + " }}", Pancake.escapeXSS(replacement[1]));
		}
		return str;
	}

	public byte[] viewData(String[]...replacements) {
		return view(replacements).getBytes(StandardCharsets.UTF_8);
	}
	/* View */

}
