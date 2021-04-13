/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer;


import com.latticeware.eqexplorer.data.DirectoryEntry;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author sfisque
 */
public class Main
{
    private static final Logger LOG = Logger.getLogger( Main.class.getName() );


    public static void main( String[] _argv )
    {
        Main _m = new Main();
        
        try
        {
            _m.load( _argv[ 0 ] );
            
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
        }
    }
    
    
    public int fourBytesToInt( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[ 0 ] ) 
                + ( Byte.toUnsignedInt( _buffer[ 1 ] ) * 256 ) 
                + ( Byte.toUnsignedInt( _buffer[ 2 ] ) * 256 * 256 ) 
                + ( Byte.toUnsignedInt( _buffer[ 3 ] ) * 256 * 256 * 256 );
    }
    
    public int bytesToBitMask( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[ 4 ] ) 
                + ( Byte.toUnsignedInt( _buffer[ 2 ] ) * 256 ) 
                + ( Byte.toUnsignedInt( _buffer[ 1 ] ) * 256 * 256 ) 
                + ( Byte.toUnsignedInt( _buffer[ 0 ] ) * 256 * 256 * 256 );
    }
    
    
    public int twoBytesToInt( byte[] _buffer )
    {
        return Byte.toUnsignedInt( _buffer[ 0 ] ) 
                + ( Byte.toUnsignedInt( _buffer[ 1 ] ) * 256 );
    }
        
    
    public void load( String _fileName ) throws IOException
    {
        File _file = new File( _fileName );
        
        try( RandomAccessFile _raf = new RandomAccessFile( _file, "r" ) )
        {
            byte[] _buffer = new byte[ 4 ];

            _raf.read( _buffer, 0, 4 );
            Integer _preamble = fourBytesToInt( _buffer );

            _raf.read( _buffer, 0, 4 );
            String _magic = new String( _buffer );
            
            _raf.read( _buffer, 0 , 4 );
            Integer _postamble = fourBytesToInt( _buffer );
            
            System.out.println( String.format( "%d, %s, %d", _preamble, _magic, _postamble ) );

            _raf.seek( _preamble );
            
//            Integer _numEntries = _raf.readUnsignedShort();
            _raf.read( _buffer, 0, 4 );
            Integer _numEntries = fourBytesToInt( _buffer );
            
            System.out.println( String.format( "%d, %d, %d", _file.length(), _preamble, _numEntries ) );
            ArrayList<DirectoryEntry> _directory = new ArrayList<>();
            
            Integer _fileListPos = 0;
            
            for( int _i = 0 ; _i < _numEntries ; _i ++ )
            {
                _raf.read( _buffer, 0, 4 );
                Integer _crc = fourBytesToInt( _buffer );

                _raf.read( _buffer, 0, 4 );
                Integer _dataOffset = fourBytesToInt( _buffer );

                _raf.read( _buffer, 0, 4 );
                Integer _inflatedLength = fourBytesToInt( _buffer );
                
                DirectoryEntry _entry = new DirectoryEntry( _crc, _dataOffset, _inflatedLength );
                _directory.add( _entry );
                
                System.out.println( String.format( "%d : %d : %d : %d", _i, _crc, _dataOffset, _inflatedLength ) );
                
                Long _returnPointer = _raf.getFilePointer();
                _raf.seek( _dataOffset );
                
                _raf.read( _buffer, 0 , 4 );
                Integer _compressedSize = fourBytesToInt( _buffer );
                _raf.read( _buffer, 0 , 4 );
                Integer _uncompressedSize = fourBytesToInt( _buffer );
                
                _raf.read( _entry.rawData, 0, _compressedSize );
                
                // increment the _fileListPos so we can find the last one
                if( _entry.offset > _directory.get( _fileListPos ).offset )
                {
                    _fileListPos = _i;
                } 
                // then uncompress the data
                
                // then restore the file pointer for next directory entry
                
                _raf.seek( _returnPointer );
            } 
            
            byte[] _footer = new byte[ 5 ];
            _raf.read( _footer, 0, 5 );
            String _tag = new String( _footer, 0, 5 );
            
            _raf.read( _footer, 0, 4 );
            Integer _date = fourBytesToInt( _footer );
            
            // process the file list
            
            
            
            System.out.println( String.format( "%s : %d", _tag, _date ) );
        
        }
    }
    
}
