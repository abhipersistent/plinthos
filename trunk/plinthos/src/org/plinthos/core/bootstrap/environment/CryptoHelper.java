/*
 * PlinthOS, Open Source Multi-Core and Distributed Computing.
 * Copyright 2003-2009, Emptoris Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.plinthos.core.bootstrap.environment;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoHelper {

	private static Cipher getChipher(int mode) throws Exception {
		String cryptAlgorithm = "DES";
		String salt = "pOS2inthTsa=";

		byte[] keyBytes = Base64.decodeBase64(salt);
		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, cryptAlgorithm);
		Cipher cipher = Cipher.getInstance(cryptAlgorithm);
		if (cipher == null)
			throw new RuntimeException(
					"Cypher could not be created for Algorithm:"
							+ cryptAlgorithm);
		cipher.init(mode, skeySpec);
		return cipher;
	}

	public static String encrypt(String value) {
		try {
			if (value == null) {
				return null;
			}
			Cipher cipher = getChipher(Cipher.ENCRYPT_MODE);
			byte[] result = cipher.doFinal(value.getBytes());
			String base64Crypted = Base64.encodeBase64String(result);
			return base64Crypted;
		} catch (Exception e) {
			throw new RuntimeException("Failed to encrypt", e);
		}
	}

	protected static String decrypt(String value) {
		try {
			if (value == null || value.length() == 0) {
				return null;
			}
			if(!value.startsWith("DES(") || !value.endsWith("")){
				return value;
			}
			value = value.substring(4);
			value = value.substring(0, value.length()-1);			
			byte[] valBytes = Base64.decodeBase64(value);

			Cipher cipher = getChipher(Cipher.DECRYPT_MODE);
			byte[] result = cipher.doFinal(valBytes);
			return new String(result);
		} catch (Exception e) {
			throw new RuntimeException("Failed to encrypt", e);
		}
	}

	public static void main(String[] args) {
		System.out.println("DES("+CryptoHelper.encrypt(args[0])+")");
	}

}
