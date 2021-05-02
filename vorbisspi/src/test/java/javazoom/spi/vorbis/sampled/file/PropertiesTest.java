package javazoom.spi.vorbis.sampled.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Properties;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

public class PropertiesTest {
	private static File filename;
	private static URL fileurl;
	private static Properties props;

	@BeforeAll
	public static void setupFiles() {
		try (final var in = PropertiesTest.class.getClassLoader().getResourceAsStream("test.ogg")) {
			filename = File.createTempFile("vorbisspi", "test.ogg");
			fileurl = filename.toURI().toURL();
			Files.copy(in, filename.toPath(), StandardCopyOption.REPLACE_EXISTING);
			props = new Properties();
			props.load(PropertiesTest.class.getClassLoader().getResourceAsStream("test.ogg.properties"));
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void testPropertiesFile() {
		String[] testPropsAFF = {"duration", "title", "author", "album", "date", "comment",
			"copyright", "ogg.bitrate.min", "ogg.bitrate.nominal", "ogg.bitrate.max"};
		String[] testPropsAF = {"vbr", "bitrate"};

		File file = filename;
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		try {
			baseFileFormat = AudioSystem.getAudioFileFormat(file);
			baseFormat = baseFileFormat.getFormat();
			if (baseFileFormat instanceof TAudioFileFormat) {
				Map properties = ((TAudioFileFormat) baseFileFormat).properties();
				for (int i = 0; i < testPropsAFF.length; i++) {
					String key = testPropsAFF[i];
					if (properties.get(key) != null) {
						String val = (properties.get(key)).toString();
						String valexpected = props.getProperty(key);
						Assertions.assertEquals(valexpected, val, key);
					}
				}
			} else {
				Assertions.fail("testPropertiesFile : TAudioFileFormat expected");
			}

			if (baseFormat instanceof TAudioFormat) {
				Map properties = ((TAudioFormat) baseFormat).properties();
				for (int i = 0; i < testPropsAF.length; i++) {
					String key = testPropsAF[i];
					if (properties.get(key) != null) {
						String val = (properties.get(key)).toString();
						String valexpected = props.getProperty(key);
						Assertions.assertEquals(valexpected, val, key);
					}
				}
			} else {
				Assertions.fail("testPropertiesFile : TAudioFormat expected");
			}
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void testPropertiesURL() {
		String[] testPropsAFF = {"duration", "title", "author", "album", "date", "comment",
			"copyright", "ogg.bitrate.min", "ogg.bitrate.nominal", "ogg.bitrate.max"};
		String[] testPropsAF = {"vbr", "bitrate"};
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		try {
			URL url = fileurl;
			baseFileFormat = AudioSystem.getAudioFileFormat(url);
			baseFormat = baseFileFormat.getFormat();
			if (baseFileFormat instanceof TAudioFileFormat) {
				Map properties = ((TAudioFileFormat) baseFileFormat).properties();
				for (int i = 0; i < testPropsAFF.length; i++) {
					String key = testPropsAFF[i];
					if (properties.get(key) != null) {
						String val = (properties.get(key)).toString();
						String valexpected = props.getProperty(key);
						Assertions.assertEquals(valexpected, val, key);
					}
				}
			} else {
				Assertions.fail("testPropertiesURL : TAudioFileFormat expected");
			}
			if (baseFormat instanceof TAudioFormat) {
				Map properties = ((TAudioFormat) baseFormat).properties();
				for (int i = 0; i < testPropsAF.length; i++) {
					String key = testPropsAF[i];
					if (properties.get(key) != null) {
						String val = (properties.get(key)).toString();
						String valexpected = props.getProperty(key);
						Assertions.assertEquals(valexpected, val, key);
					}
				}
			} else {
				Assertions.fail("testPropertiesURL : TAudioFormat expected");
			}
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}
}
