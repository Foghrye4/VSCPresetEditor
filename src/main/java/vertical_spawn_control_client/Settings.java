package vertical_spawn_control_client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Settings {
	
	public File recentDirectory;
	
	public Settings() {
		recentDirectory = new File("./");
	}
	
	public Settings(File recentDirectoryIn) {
		recentDirectory = recentDirectoryIn;
	}

	public static Settings fromFile(File file) {
		if(!file.exists()) {
			new Settings().toFile(file);
		}
		try (JsonReader reader = new JsonReader(new FileReader(file))) {
			File recentDirectoryIn = new File("./");
        	reader.beginObject();
        	while(reader.hasNext()) {
        		String key = reader.nextName();
        		if(key.equalsIgnoreCase("last_path")) {
        			recentDirectoryIn = new File(reader.nextString());
        		}
        		else {
        			reader.skipValue();
        		}
        	}
        	reader.endObject();
        	reader.close();
        	return new Settings(recentDirectoryIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Settings();

	}
/**
 * @return true if file created*/
	public boolean toFile(File file) {
		try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
			writer.setIndent(" ");
        	writer.beginObject();
        	writer.name("last_path");
        	writer.value(recentDirectory.getAbsolutePath());
        	writer.endObject();
        	writer.close();
        	return true;
		} catch (IOException e) {
			e.printStackTrace();
        	return false;
		}
	}
}
