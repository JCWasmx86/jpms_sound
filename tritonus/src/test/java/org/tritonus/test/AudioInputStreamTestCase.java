/*
 *	AudioInputStreamTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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
 */

package org.tritonus.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AudioInputStreamTestCase {


	@Test
	public void testConstructorNullPointers() {
		@SuppressWarnings("unused") AudioInputStream ais = null;
		InputStream is = new ByteArrayInputStream(new byte[0]);
		AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		try {
			ais = new AudioInputStream(null, format, AudioSystem.NOT_SPECIFIED);
			Assertions.fail("no NullpointerException thrown for null InputStream");
		} catch (NullPointerException e) {
		}
		try {
			ais = new AudioInputStream(is, null, AudioSystem.NOT_SPECIFIED);
			Assertions.fail("no NullpointerException thrown for null AudioFormat");
		} catch (NullPointerException e) {
		}
	}

}