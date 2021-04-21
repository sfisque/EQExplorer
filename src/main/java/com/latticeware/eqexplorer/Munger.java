/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 *
 * @author sfisque
 */
public class Munger
{
    private static final int[] conversion = { 0x95, 0x3A, 0xC5, 0x2A, 0x95, 0x7A, 0x95, 0x6A };

    /**
     *
     * @param _buffer the value of _buffer
     * @return the int
     */
    public static int twoBytesToInt( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[0] ) + ( Byte.toUnsignedInt( _buffer[1] ) * 256 );
    }

    /**
     *
     * @param _buffer the value of _buffer
     * @return the int
     */
    public static int bytesToBitMask( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[4] ) + ( Byte.toUnsignedInt( _buffer[2] ) * 256 ) + ( Byte.toUnsignedInt( _buffer[1] ) * 256 * 256 ) + ( Byte.toUnsignedInt( _buffer[0] ) * 256 * 256 * 256 );
    }

    /**
     *
     * @param _buffer the value of _buffer
     * @return the int
     */
    public static int fourBytesToInt( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[0] ) + ( Byte.toUnsignedInt( _buffer[1] ) * 256 ) + ( Byte.toUnsignedInt( _buffer[2] ) * 256 * 256 ) + ( Byte.toUnsignedInt( _buffer[3] ) * 256 * 256 * 256 );
    }

    
    public static void hexDump( byte[] _buffer )
    {
        int _colCount = 0;
        for ( int _cur = 0; _cur < _buffer.length; _cur++ )
        {
            if ( _colCount % 16 == 0 )
            {
                System.out.println();
            }
            if ( _buffer[_cur] < 32 )
            {
                System.out.print( String.format( "%02x ", _buffer[_cur] ) );
            }
            else
            {
                System.out.print( String.format( "%c ", _buffer[_cur] ) );
            }
            _colCount++;
        }
        System.out.println();
    }

    
    public static byte[] expand( byte[] _buffer, int _compSize, int _expSize ) 
    throws DataFormatException
    {
        byte[] _temp = new byte[ _expSize ];
        Inflater _inflater = new Inflater();
        _inflater.setInput( _buffer, 0, _compSize );
        int _count = _inflater.inflate( _temp );
        System.out.println( String.format( "\texpand: %d, %d, %d", _compSize, _expSize, _count ) );
        return _temp;
    }
    
    
    public static List<String> wldStringConvert( int _count, byte[] _buffer )
    {
        List<String> _list = new ArrayList<>();
        ByteArrayOutputStream _string = new ByteArrayOutputStream();
        
        int _len = 0;

        for( int _index = 0; _index < _count; _index ++ )
        {
            int _char = ( _buffer[ _index ] ^ conversion[ _index % 8 ] ) % 256;

            if( _char == 0 )
            {
                if( _len > 0 )
                {
                    _list.add( _string.toString() );
                    _string.reset();
                }
                _len = 0;
            }
            else
            {
                _string.write( _char );
                _len ++;
            }
            
            if( _len == _count )
            {
                _list.add( _string.toString() );
                _string.reset();
            }
        }
        
        return _list;
    }
}
