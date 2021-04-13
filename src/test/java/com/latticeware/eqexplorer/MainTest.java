/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer;


import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author sfisque
 */
public class MainTest
{
    
    public MainTest()
    {
    }


    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass()
            throws Exception
    {
    }


    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass()
            throws Exception
    {
    }


    @org.junit.jupiter.api.BeforeEach
    public void setUp()
            throws Exception
    {
    }


    @org.junit.jupiter.api.AfterEach
    public void tearDown()
            throws Exception
    {
    }
    

    /**
     * Test of fourBytesToInt method, of class Main.
     */
    @org.junit.jupiter.api.Test
    public void testFourBytesToInt()
    {
        System.out.println( "fourBytesToInt" );
        byte[] _buffer = new byte[] { 1, 1, 1, 0 };
        Main instance = new Main();
        int expResult = 0; // 1 + ( 1 << 8 ) + ( 1 << 16 ) + ( 0 << 24 );
        
        for( int _i = 3; _i >= 0; _i -- )
        {
            expResult = expResult << 8;
            expResult += Byte.toUnsignedInt( _buffer[ _i ] );
        }
        int result = instance.fourBytesToInt( _buffer );

        assertEquals( expResult, result );
    }


    /**
     * Test of bytesToBitMask method, of class Main.
     */
//    @org.junit.jupiter.api.Test
    public void testBytesToBitMask()
    {
        System.out.println( "bytesToBitMask" );
        byte[] _buffer = null;
        Main instance = new Main();
        int expResult = 0;
        int result = instance.bytesToBitMask( _buffer );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of load method, of class Main.
     */
//    @org.junit.jupiter.api.Test
    public void testLoad()
            throws Exception
    {
        System.out.println( "load" );
        String _fileName = "";
        Main instance = new Main();
        instance.load( _fileName );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of hexDump method, of class Main.
     */
//    @org.junit.jupiter.api.Test
    public void testHexDump()
    {
        System.out.println( "hexDump" );
        byte[] _buffer = null;
        Main instance = new Main();
        Munger.hexDump( _buffer );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of expand method, of class Main.
     */
//    @org.junit.jupiter.api.Test
    public void testExpand()
            throws Exception
    {
        System.out.println( "expand" );
        byte[] _buffer = null;
        int _compSize = 0;
        int _expSize = 0;
        Main instance = new Main();
        byte[] expResult = null;
        byte[] result = Munger.expand( _buffer, _compSize, _expSize );
        assertArrayEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }
    
}
