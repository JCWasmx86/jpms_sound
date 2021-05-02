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

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.jlp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class jlpTest {
	@Test
	void testPlay() {
		String[] args = new String[2];
		args[0] = "-url";
		args[1] = this.getClass().getClassLoader().getResource("test.mp3").toString();
		jlp player = jlp.createInstance(args);
		try {
			player.play();
			Assertions.assertTrue(true, "Play");
		} catch (JavaLayerException e) {
			e.printStackTrace();
			Assertions.assertTrue(false, "JavaLayerException : " + e.getMessage());
		}
	}
}
