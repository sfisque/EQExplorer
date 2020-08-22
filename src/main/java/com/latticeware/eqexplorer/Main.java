/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer;


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
                
                _raf.seek( _returnPointer );
            } 
            
            byte[] _footer = new byte[ 5 ];
            _raf.read( _footer, 0, 5 );
            String _tag = new String( _footer, 0, 5 );
            
            _raf.read( _footer, 0, 4 );
            Integer _date = fourBytesToInt( _footer );
            
            System.out.println( String.format( "%s : %d", _tag, _date ) );
            
        }
    }
    
    
    public static class FileHeader
    {
        public FileHeader( Integer _offset, String _cookie, Integer _magicValue )
        {
            this.offset = _offset;
            this.cookie = _cookie;
            this.magicValue = _magicValue;
        }
        
        public Integer offset;
        public String cookie;
        public Integer magicValue;  // usually 13107
    }
    
    
    public static class DirectoryEntry
    {
        public DirectoryEntry( Integer _crc, Integer _offset, Integer _inflatedSize )
        {
            this.crc = _crc;
            this.offset = _offset;
            this.inflatedSize = _inflatedSize;
            rawData = new byte[ _inflatedSize ];
        }

        public Integer crc;
        public Integer offset;
        public Integer inflatedSize;
        public byte[] rawData;
    }
    
    
    public static class Footer
    {
        public Footer( String _signature, Integer _date )
        {
            this.signature = _signature;
            this.date = _date;
        }

        public String signature;
        public Integer date;
    }
    
    
    public static class DataHeader
    {
        public DataHeader( Integer _compressedSize, Integer _inflatedSize )
        {
            this.compressedSize = _compressedSize;
            this.inflatedSize = _inflatedSize;
        }

        public Integer compressedSize;
        public Integer inflatedSize;
    }
    
    /*
  TS3DFileHeader = Packed Record
    DirectoryOffset        : LongWord;                   // File position of the S3D directory
    MagicCookie            : Packed Array[0..3] Of Char; // Always "PFS ".  It identifies the file type
    Unknown_Always_131072  : LongWord;                   // As the spec says, it always contains 131072 (128k)
  End;
    
  FDirectory = array of ...
  TS3DDirectoryEntry = Packed Record
    CRC                    : LongWord;                   // Filename CRC.  Calculated with the standard IEEE 802.3 Ethernet CRC-32 algorithm.
    DataOffset             : LongWord;                   // Position in the archive of the compressed data
    DataLengthInflated     : LongWord;                   // The file size once inflated
  End;
    
  TS3DFileFooter = Packed Record
    SteveCookie5           : Packed Array[0..4] Of Char; // Always "STEVE".
    Date                   : LongWord;                   // I think the patcher uses this to check a file's version
  End;
    
  TS3DDataBlockHeader = Packed Record
    DeflatedLength         : LongWord;                   // Compressed size
    InflatedLength         : LongWord;                   // Uncompressed size
  End;

    */
}
