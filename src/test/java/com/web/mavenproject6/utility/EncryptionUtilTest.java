/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.utility;

import java.security.GeneralSecurityException;
import javax.crypto.NoSuchPaddingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Aleks
 */
public class EncryptionUtilTest {
    
//    public EncryptionUtilTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of encrypt method, of class EncryptionUtil.
//     */
//    @Test
//    public void testEncrypt() throws Exception {
//        System.out.println("encrypt");
//        String input = "";
//        EncryptionUtil instance = new EncryptionUtil();
//        byte[] expResult = null;
//        byte[] result = instance.encrypt(input);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of decrypt method, of class EncryptionUtil.
//     */
//    @Test
//    public void testDecrypt() throws Exception {
//        System.out.println("decrypt");
//        byte[] input = null;
//        EncryptionUtil instance = new EncryptionUtil();
//        String expResult = "";
//        String result = instance.decrypt(input);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
//    @Test 
//	public void testEncryptionDecryption(){
//		String testString = "hello world";
//		String resultString = "";
//                EncryptionUtil instance = new EncryptionUtil();
//		try {
//			resultString = instance.decrypt(instance.encrypt(testString));
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//		} catch (GeneralSecurityException e) {
//					e.printStackTrace();
//		}
//		
//		assert(resultString != null);
//		assert(resultString.equals(testString));
//		
//	}
//    
}
