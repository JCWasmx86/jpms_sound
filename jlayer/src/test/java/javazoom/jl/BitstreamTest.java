/*
 * 11/19/2004 : 1.0 moved to LGPL.
 * 01/01/2004 : Initial version by E.B javalayer@javazoom.net
 *-----------------------------------------------------------------------
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
 *----------------------------------------------------------------------
 */
package javazoom.jl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class BitstreamTest {
	private static Properties PROPS;
	private static Bitstream in;

	@BeforeAll
	static void loadFile() {
		BitstreamTest.PROPS = new Properties();
		try (InputStream pin = BitstreamTest.class.getClassLoader().getResourceAsStream("test.mp3.properties")) {
			PROPS.load(pin);
			in = new Bitstream(Bitstream.class.getClassLoader().getResourceAsStream("test.mp3"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterAll
	static void closeStream() throws BitstreamException {
		in.close();
	}

	@Test
	void test() {
		try {
			InputStream id3in = in.getRawID3v2();
			//int size = id3in.available();
			Header header = in.readFrame();
			System.out.println("--- test.mp3 ---");
			//	System.out.println("ID3v2Size=" + size);
			System.out.println("version=" + header.version());
			System.out.println("version_string=" + header.version_string());
			System.out.println("layer=" + header.layer());
			System.out.println("frequency=" + header.frequency());
			System.out.println("frequency_string=" + header.sample_frequency_string());
			System.out.println("bitrate=" + header.bitrate());
			System.out.println("bitrate_string=" + header.bitrate_string());
			System.out.println("mode=" + header.mode());
			System.out.println("mode_string=" + header.mode_string());
			System.out.println("slots=" + header.slots());
			System.out.println("vbr=" + header.vbr());
			System.out.println("vbr_scale=" + header.vbr_scale());
			//System.out.println("max_number_of_frames=" + header.max_number_of_frames(mp3in.available()));
			//System.out.println("min_number_of_frames=" + header.min_number_of_frames(mp3in.available()));
			System.out.println("ms_per_frame=" + header.ms_per_frame());
			System.out.println("frames_per_second=" + (float) ((1.0 / (header.ms_per_frame())) * 1000.0));
			//System.out.println("total_ms=" + header.total_ms(mp3in.available()));
			System.out.println("SyncHeader=" + header.getSyncHeader());
			System.out.println("checksums=" + header.checksums());
			System.out.println("copyright=" + header.copyright());
			System.out.println("original=" + header.original());
			System.out.println("padding=" + header.padding());
			System.out.println("framesize=" + header.calculate_framesize());
			System.out.println("number_of_subbands=" + header.number_of_subbands());
			//	Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("ID3v2Size")), size, "ID3v2Size");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("version")), header.version(), "version");
			Assertions.assertEquals(PROPS.getProperty("version_string"), header.version_string(), "version_string");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("layer")), header.layer(), "layer");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("frequency")), header.frequency(), "frequency");
			Assertions.assertEquals(PROPS.getProperty("frequency_string"),
				header.sample_frequency_string(), "frequency_string");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("bitrate")), header.bitrate(), "bitrate");
			Assertions.assertEquals(PROPS.getProperty("bitrate_string"), header.bitrate_string(), "bitrate_string");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("mode")), header.mode(), "mode");
			Assertions.assertEquals(PROPS.getProperty("mode_string"), header.mode_string(),
				"mode_string");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("slots")), header.slots(), "slots");
			Assertions.assertEquals(Boolean.valueOf(PROPS.getProperty("vbr")), header.vbr(), "vbr");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("vbr_scale")), header.vbr_scale(),
				"vbr_scale");
			//Assertions.assertEquals("max_number_of_frames", Integer.parseInt(PROPS.getProperty
			// ("max_number_of_frames")),
			//	header.max_number_of_frames(mp3in.available()));
			//Assertions.assertEquals("min_number_of_frames", Integer.parseInt(PROPS.getProperty
			// ("min_number_of_frames")),
			//	header.min_number_of_frames(mp3in.available()));
			Assertions.assertTrue(
				Float.parseFloat(PROPS.getProperty("ms_per_frame")) == header.ms_per_frame(), "ms_per_frame");
			Assertions.assertTrue(
				Float.parseFloat(PROPS.getProperty("frames_per_second")) == (float) ((1.0 / (header.ms_per_frame())) * 1000.0), "frames_per_second");
			//assertTrue("total_ms",
			//Float.parseFloat(PROPS.getProperty("total_ms")) == header.total_ms(mp3in.available()));
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("SyncHeader")),
				header.getSyncHeader(), "SyncHeader");
			Assertions.assertEquals(Boolean.valueOf(PROPS.getProperty("checksums")),
				header.checksums(), "checksums");
			Assertions.assertEquals(Boolean.valueOf(PROPS.getProperty("copyright")),
				header.copyright(), "copyright");
			Assertions.assertEquals(Boolean.valueOf(PROPS.getProperty("original")),
				header.original(), "original");
			Assertions.assertEquals(Boolean.valueOf(PROPS.getProperty("padding")),
				header.padding(), "padding");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("framesize")),
				header.calculate_framesize(), "framesize");
			Assertions.assertEquals(Integer.parseInt(PROPS.getProperty("number_of_subbands")),
				header.number_of_subbands(), "number_of_subbands");
			in.closeFrame();
		} catch (BitstreamException e) {
			Assertions.fail(e);
		}
	}
}
