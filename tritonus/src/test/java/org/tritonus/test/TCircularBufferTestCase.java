/*
 *	TCircularBufferTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import org.junit.jupiter.api.Assertions;
import org.tritonus.share.TCircularBuffer;


public class TCircularBufferTestCase {


	public void testBufferSize() {
		int nSize = 45678;
		TCircularBuffer buffer = new TCircularBuffer(
			nSize, false, false, null);
		Assertions.assertEquals(nSize, buffer.availableWrite(), "buffer size");
		nSize = 0;
		buffer = new TCircularBuffer(
			nSize, false, false, null);
		Assertions.assertEquals(nSize, buffer.availableWrite(), "buffer size");
	}


	public void testAvailable() {
		int nBufferSize = 45678;
		int nWriteSize1 = nBufferSize / 2;
		int nWriteSize2 = nBufferSize / 5;
		int nReadSize1 = nBufferSize / 10;
		int nReadSize2 = nBufferSize / 3;
		TCircularBuffer buffer = new TCircularBuffer(
			nBufferSize, true, true, null);
		Assertions.assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(0, buffer.availableRead(), "availableRead()");
		buffer.write(new byte[nBufferSize]);
		Assertions.assertEquals(0, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nBufferSize, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nBufferSize]);
		Assertions.assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(0, buffer.availableRead(), "availableRead()");


		buffer.write(new byte[nWriteSize1]);
		Assertions.assertEquals(nBufferSize - nWriteSize1, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nWriteSize1, buffer.availableRead(), "availableRead()");
		buffer.write(new byte[nWriteSize2]);
		Assertions.assertEquals(nBufferSize - nWriteSize1 - nWriteSize2, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nWriteSize1 + nWriteSize2, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nReadSize1]);
		Assertions.assertEquals(nBufferSize - nWriteSize1 - nWriteSize2 + nReadSize1,
			buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nWriteSize1 + nWriteSize2 - nReadSize1, buffer.availableRead(), "availableRead()");
		buffer.read(new byte[nReadSize2]);
		Assertions.assertEquals(nBufferSize - nWriteSize1 - nWriteSize2 + nReadSize1 + nReadSize2,
			buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nWriteSize1 + nWriteSize2 - nReadSize1 - nReadSize2,
			buffer.availableRead(), "availableRead()");
	}


	public void testReadWrite() {
		int nBufferSize = 8901 * 4;
		int nResult;
		byte[] abWriteArray = new byte[nBufferSize];
		byte[] abReadArray = new byte[nBufferSize];
		TCircularBuffer buffer = new TCircularBuffer(
			nBufferSize, true, true, null);
		for (int i = 0; i < abWriteArray.length; i++) {
			abWriteArray[i] = (byte) (i % 256);
		}
		nResult = buffer.write(abWriteArray);
		Assertions.assertEquals(abWriteArray.length, nResult, "written length");
		nResult = buffer.read(abReadArray);
		Assertions.assertEquals(abReadArray.length, nResult, "read length");
		Assertions.assertTrue(Util.compareByteArrays(abReadArray, 0, abWriteArray, 0, abReadArray.length), "data " +
			"content");

		buffer.write(new byte[nBufferSize / 3]);
		nResult = buffer.write(abWriteArray, nBufferSize / 4, nBufferSize / 2);
		Assertions.assertEquals(nBufferSize / 2, nResult, "written length");
		buffer.read(new byte[nBufferSize / 3]);
		nResult = buffer.read(abReadArray, 0, nBufferSize / 2);
		Assertions.assertEquals(nBufferSize / 2, nResult, "written length");
		Assertions.assertTrue(Util.compareByteArrays(abReadArray, 0, abWriteArray, nBufferSize / 4,
			nBufferSize / 2), "data content");
	}


	public void testTrigger() {
		TestTrigger trigger = new TestTrigger();

		int nBufferSize = 45678;
		TCircularBuffer buffer = new TCircularBuffer(
			nBufferSize, false, true, trigger);
		buffer.read(new byte[10]);
		Assertions.assertTrue(trigger.isCalled(), "trigger called");

		trigger.reset();
		buffer.write(new byte[nBufferSize / 3]);
		buffer.read(new byte[nBufferSize / 2]);
		Assertions.assertTrue(trigger.isCalled(), "trigger called");
	}


	public void testClose() {
		int nResult;
		int nBufferSize = 45678;
		TestTrigger trigger = new TestTrigger();
		TCircularBuffer buffer = new TCircularBuffer(
			nBufferSize, true, true, trigger);
		buffer.write(new byte[nBufferSize / 2]);
		Assertions.assertEquals(nBufferSize / 2, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nBufferSize / 2, buffer.availableRead(), "availableRead()");
		buffer.close();
		Assertions.assertEquals(nBufferSize / 2, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(nBufferSize / 2, buffer.availableRead(), "availableRead()");
		nResult = buffer.read(new byte[nBufferSize / 2]);
		Assertions.assertEquals(nBufferSize / 2, nResult, "read length");
		Assertions.assertEquals(nBufferSize, buffer.availableWrite(), "availableWrite()");
		Assertions.assertEquals(0, buffer.availableRead(), "availableRead()");
		nResult = buffer.read(new byte[nBufferSize / 2]);
		Assertions.assertEquals(-1, nResult, "read length");
		Assertions.assertTrue(!trigger.isCalled(), "trigger invocation");
	}


	private static class TestTrigger
		implements TCircularBuffer.Trigger {
		private boolean m_bCalled = false;


		public void execute() {
			m_bCalled = true;
		}


		public boolean isCalled() {
			return m_bCalled;
		}


		public void reset() {
			m_bCalled = false;
		}
	}
}