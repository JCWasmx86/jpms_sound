/*
 *   MpegAudioFileReaderTest - JavaZOOM : http://www.javazoom.net
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package javazoom.spi.mpeg.sampled.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MpegAudioFileReaderTest {
	private static File filename;
	private static URL fileurl;
	private static Properties props;

	@BeforeAll
	public static void setupFiles() {
		try (final var in = MpegAudioFileReaderTest.class.getClassLoader().getResourceAsStream("test.mp3")) {
			filename = File.createTempFile("mp3spi", "test.mp3");
			fileurl = filename.toURI().toURL();
			Files.copy(in, filename.toPath(), StandardCopyOption.REPLACE_EXISTING);
			props = new Properties();
			props.load(MpegAudioFileReaderTest.class.getClassLoader().getResourceAsStream("test.mp3.properties"));
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioFileFormatFile() {
		try {
			File file = filename;
			AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(file);
			dumpAudioFileFormat(baseFileFormat, System.out, file.toString());
			Assertions.assertEquals(Integer.parseInt(props.getProperty("FrameLength")),
				baseFileFormat.getFrameLength(), "FrameLength");
			Assertions.assertEquals(Integer.parseInt(props.getProperty("ByteLength")),
				baseFileFormat.getByteLength(), "ByteLength");
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioFileFormatURL() {
		try {
			URL url = fileurl;
			AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(url);
			dumpAudioFileFormat(baseFileFormat, System.out, url.toString());
			Assertions.assertEquals(-1, baseFileFormat.getFrameLength(), "FrameLength");
			Assertions.assertEquals(-1, baseFileFormat.getByteLength(), "ByteLength");
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioFileFormatInputStream() {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(in);
			dumpAudioFileFormat(baseFileFormat, System.out, in.toString());
			in.close();
			Assertions.assertEquals(-1, baseFileFormat.getFrameLength(), "FrameLength");
			Assertions.assertEquals(-1, baseFileFormat.getByteLength(), "ByteLength");
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioInputStreamInputStream() {
		try {
			InputStream fin = new BufferedInputStream(new FileInputStream(filename));
			AudioInputStream in = AudioSystem.getAudioInputStream(fin);
			dumpAudioInputStream(in, System.out, fin.toString());
			Assertions.assertEquals(-1, in.getFrameLength(), "FrameLength");
			Assertions.assertEquals(Integer.parseInt(props.getProperty("Available")),
				in.available(), "Available");
			fin.close();
			in.close();
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioInputStreamFile() {
		try {
			File file = filename;
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			dumpAudioInputStream(in, System.out, file.toString());
			Assertions.assertEquals(-1, in.getFrameLength(), "FrameLength");
			Assertions.assertEquals(Integer.parseInt(props.getProperty("Available")),
				in.available(), "Available");
			in.close();
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void _testGetAudioInputStreamURL() {
		try {
			URL url = fileurl;
			AudioInputStream in = AudioSystem.getAudioInputStream(url);
			dumpAudioInputStream(in, System.out, url.toString());
			Assertions.assertEquals(-1, in.getFrameLength(), "FrameLength");
			Assertions.assertEquals(Integer.parseInt(props.getProperty("Available")),
				in.available(), "Available");
			in.close();
		} catch (UnsupportedAudioFileException e) {
			Assertions.fail(e);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	private void dumpAudioFileFormat(AudioFileFormat baseFileFormat, PrintStream out, String info) throws UnsupportedAudioFileException {
		AudioFormat baseFormat = baseFileFormat.getFormat();
		Assertions.assertEquals(props.getProperty("Type"), baseFileFormat.getType().toString(), "Type");
		Assertions.assertEquals(props.getProperty("SourceFormat"), baseFormat.toString(), "SourceFormat");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("Channels")),
			baseFormat.getChannels(), "Channels");
		Assertions.assertTrue(
			Float.parseFloat(props.getProperty("FrameRate")) == baseFormat.getFrameRate(), "FrameRate");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("FrameSize")),
			baseFormat.getFrameSize(), "FrameSize");
		Assertions.assertTrue(
			Float.parseFloat(props.getProperty("SampleRate")) == baseFormat.getSampleRate(), "SampleRate");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("SampleSizeInBits")),
			baseFormat.getSampleSizeInBits(), "SampleSizeInBits");
		Assertions.assertEquals(props.getProperty("Encoding"),
			baseFormat.getEncoding().toString(), "Encoding");
	}

	private void dumpAudioInputStream(AudioInputStream in, PrintStream out, String info) throws IOException {
		AudioFormat baseFormat = in.getFormat();
		Assertions.assertEquals(props.getProperty("SourceFormat"), baseFormat.toString(), "SourceFormat");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("Channels")),
			baseFormat.getChannels(), "Channels");
		Assertions.assertTrue(
			Float.parseFloat(props.getProperty("FrameRate")) == baseFormat.getFrameRate(), "FrameRate");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("FrameSize")),
			baseFormat.getFrameSize(), "FrameSize");
		Assertions.assertTrue(
			Float.parseFloat(props.getProperty("SampleRate")) == baseFormat.getSampleRate(), "FrameSize");
		Assertions.assertEquals(Integer.parseInt(props.getProperty("SampleSizeInBits")),
			baseFormat.getSampleSizeInBits(), "SampleSizeInBits");
		Assertions.assertEquals(props.getProperty("Encoding"),
			baseFormat.getEncoding().toString(), "Encoding");

	}
}
