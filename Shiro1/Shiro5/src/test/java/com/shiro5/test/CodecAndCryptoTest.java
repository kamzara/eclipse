package com.shiro5.test;

import static org.junit.Assert.*;
import junit.framework.Assert;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.*;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.junit.Test;

public class CodecAndCryptoTest {

	@Test
	public void testBase64() {
		String str = "hello";
		String base64Encoded = Base64.encodeToString(str.getBytes());
		System.out.println(base64Encoded);
		String str2 = Base64.decodeToString(base64Encoded);
		System.out.println(str2);
		Assert.assertEquals(str, str2);

	}

	@Test
	public void testHex() {
		String str = "hello";
		String base64Encoded = Hex.encodeToString(str.getBytes());
		String str2 = new String(Hex.decode(base64Encoded.getBytes()));
		Assert.assertEquals(str, str2);
	}

	@Test
	public void testCodecSupport() {
		String str = "hello";
		byte[] bytes = CodecSupport.toBytes(str, "utf-8");
		String str2 = CodecSupport.toString(bytes, "utf-8");
		Assert.assertEquals(str, str2);
	}

	@Test
	public void testMd5() {
		String str = "hello";
		String salt = "123";
		String md5 = new Md5Hash(str, salt).toString();
		System.out.println(md5);
	}

	@Test
	public void testRandom() {
		SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
		randomNumberGenerator.setSeed("123".getBytes());
		System.out.println(randomNumberGenerator.nextBytes().toHex()); // 23ae809ddacaf96af0fd78ed04b6a265
	}

	@Test
	public void testSha1() {
		String str = "hello";
		String salt = "123";
		String sha1 = new Sha1Hash(str, salt).toString();
		System.out.println(sha1);
	}

	@Test
	public void testSha256() {
		String str = "hello";
		String salt = "123";
		String sha1 = new Sha256Hash(str, salt).toString();
		System.out.println(sha1);
	}

	@Test
	public void testSha384() {
		String str = "hello";
		String salt = "123";
		String sha1 = new Sha384Hash(str, salt).toString();
		System.out.println(sha1);
	}

	@Test
	public void testSha512() {
		String str = "hello";
		String salt = "123";
		String sha1 = new Sha512Hash(str, salt).toString();
		System.out.println(sha1);
	}

	@Test
	public void testSimpleHash() {
		String str = "hello";
		String salt = "123";
		String simpleHash = new SimpleHash("SHA-1", str, salt).toString();
		System.out.println(simpleHash);
	}

	@Test
	public void testHashService() {
		DefaultHashService hashService = new DefaultHashService();
		// 设置默认算法 SHA-512
		hashService.setHashAlgorithmName("SHA-512");
		// 私盐 默认无
		hashService.setPrivateSalt(new SimpleByteSource("123"));
		// 是否生成共盐，默认false
		hashService.setGeneratePublicSalt(true);
		// 用于生成公盐。默认就这个
		hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());
		// 生成hash值的迭代次数
		hashService.setHashIterations(1);
		HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5")
				.setSource(ByteSource.Util.bytes("hello")).setSalt(ByteSource.Util.bytes("123")).setIterations(2)
				.build();

		String hex = hashService.computeHash(request).toHex();
		System.out.println(hex);

	}

	@Test
	public void testAesCipherService() {
		AesCipherService aesCipherService = new AesCipherService();
		// 设置key长度
		aesCipherService.setKeySize(128);
		// 生成key
		Key key = aesCipherService.generateNewKey();
		String text = "hello";
		// 加密
		String encrptText = aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		// 解密
		String text2 = new String(aesCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

		Assert.assertEquals(text, text2);
	}

	@Test
	public void testBlowfishCipherService() {
		BlowfishCipherService blowfishCipherService = new BlowfishCipherService();
		blowfishCipherService.setKeySize(128);

		// 生成key
		Key key = blowfishCipherService.generateNewKey();

		String text = "hello";

		// 加密
		String encrptText = blowfishCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		// 解密
		String text2 = new String(blowfishCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());
		Assert.assertEquals(text, text2);
	}

	@Test
	public void testDefaultBlockCipherService() throws Exception {

		// 对称加密，使用Java的JCA（javax.crypto.Cipher）加密API，常见的如 'AES', 'Blowfish'
		DefaultBlockCipherService cipherService = new DefaultBlockCipherService("AES");
		cipherService.setKeySize(128);

		// 生成key
		Key key = cipherService.generateNewKey();

		String text = "hello";

		// 加密
		String encrptText = cipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		// 解密
		String text2 = new String(cipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

		Assert.assertEquals(text, text2);
	}

}