/*
 *	ControlTypeTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import javax.sound.sampled.Control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Tests for class javax.sound.sampled.Control.
 */
public class ControlTypeTestCase {


	/**
	 * Checks the constructor().
	 * The test checks if the constructor does not throw an
	 * exception.
	 */
	@Test
	public void testConstructor()
	throws Exception {
		String strTypeName = "TeSt";
		@SuppressWarnings("unused") Control.Type type =
			new TestControlType(strTypeName);
	}


	/**
	 * Checks equals().
	 * The test checks if an object is considered equal to
	 * itself.
	 */
	@Test
	public void testEqualsSelfIdentity()
	throws Exception {
		String strTypeName = "TeSt";
		Control.Type type = new TestControlType(strTypeName);
		Assertions.assertTrue(type.equals(type), "self-identity");
	}


	/**
	 * Checks equals().
	 * The test checks if two objects are considered unequal,
	 * even if they have the same type string.
	 */
	@Test
	public void testEqualsSelfUnequality()
	throws Exception {
		String strTypeName = "TeSt";
		Control.Type type0 = new TestControlType(strTypeName);
		Control.Type type1 = new TestControlType(strTypeName);
		Assertions.assertTrue(!type0.equals(type1), "unequality");
	}


	/**
	 * Checks hashCode().
	 * The test checks if two calls to hashCode() on the
	 * same object return the same value.
	 */
	@Test
	public void testHashCode()
	throws Exception {
		String strTypeName = "TeSt";
		Control.Type type = new TestControlType(strTypeName);
		Assertions.assertTrue(type.hashCode() == type.hashCode(), "hash code");
	}


	/**
	 * Checks toString().
	 * The test checks if the string returned by toString()
	 * equals the one passed in the constructor
	 * (and doesn't throw an exception).
	 */
	@Test
	public void testToString()
	throws Exception {
		String strTypeName = "TeSt";
		Control.Type type = new TestControlType(strTypeName);
		String strReturnedTypeName = type.toString();
		Assertions.assertEquals(strTypeName, strReturnedTypeName, "toString() result");
	}


	/**
	 * Inner class used to get around protected constructor.
	 */
	private class TestControlType
		extends Control.Type {
		public TestControlType(String strName) {
			super(strName);
		}
	}
}