/*
 *   SkipTest - JavaZOOM : http://www.javazoom.net
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

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SkipTest {
	@Test
	public void testSkipFile() {
		byte[] bytes = null;
		try {
			bytes = this.getClass().getClassLoader().getResourceAsStream("test.mp3").readAllBytes();
		} catch (IOException e) {
			Assertions.fail(e);
		}
		try (final var stream = this.getClass().getClassLoader().getResourceAsStream("test.mp3")) {
			AudioInputStream in = AudioSystem.getAudioInputStream(stream);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			long toSkip = (long) (bytes.length / 2);
			long skipped = skip(din, toSkip);
			rawplay(decodedFormat, din);
			in.close();
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	private long skip(AudioInputStream in, long bytes) throws IOException {
		long SKIP_INACCURACY_SIZE = 1200;
		long totalSkipped = 0;
		long skipped = 0;
		while (totalSkipped < (bytes - SKIP_INACCURACY_SIZE)) {
			skipped = in.skip(bytes - totalSkipped);
			if (skipped == 0) break;
			totalSkipped = totalSkipped + skipped;
		}
		return totalSkipped;
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}
}
