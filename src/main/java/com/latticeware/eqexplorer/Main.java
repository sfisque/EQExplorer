/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;



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
    
    
    public void load( String _fileName ) throws IOException
    {
        File _file = new File( _fileName );
        FileHeader _archiveHeader = new FileHeader();
        
        
        try( RandomAccessFile _raf = new RandomAccessFile( _file, "r" ) )
        {
            byte[] _buffer = new byte[ 4 ];

            _raf.read( _buffer, 0, 4 );
            _archiveHeader.offset = fourBytesToInt( _buffer );

            _raf.read( _buffer, 0, 4 );
            _archiveHeader.cookie = new String( _buffer );
            
            _raf.read( _buffer, 0 , 4 );
            _archiveHeader.magicValue = fourBytesToInt( _buffer );
            
            System.out.println( _archiveHeader.toString() );

            _raf.seek( _archiveHeader.offset );
            
//            Integer _numEntries = _raf.readUnsignedShort();
            _raf.read( _buffer, 0, 4 );
            Integer _numEntries = fourBytesToInt( _buffer );
            
            System.out.println( String.format( "ArchiveHeader : %d : %d : %d", _file.length(), _archiveHeader.offset, _numEntries ) );
            TreeMap<Integer, DirectoryEntry> _directoryMap = new TreeMap<>();
                                    
            for( int _i = 0 ; _i < _numEntries ; _i ++ )
            {
                _raf.read( _buffer, 0, 4 );
                Integer _crc = fourBytesToInt( _buffer );

                _raf.read( _buffer, 0, 4 );
                Integer _dataOffset = fourBytesToInt( _buffer );

                _raf.read( _buffer, 0, 4 );
                Integer _inflatedLength = fourBytesToInt( _buffer );
                
                DirectoryEntry _entry = new DirectoryEntry( _crc, _dataOffset, _inflatedLength );
                _directoryMap.put( _dataOffset, _entry);
                
                System.out.println( String.format( "Directory Entry : %d : %d : %d : %d", _i, _crc, _dataOffset, _inflatedLength ) );
            
                Long _returnPointer = _raf.getFilePointer();
                _raf.seek( _dataOffset );
                
                int _countBig = 0;
                int _countLittle = 0;
                byte[] _fileBuffer = new byte[ _inflatedLength ];
                
                while( _countBig < _entry.inflatedSize )
                {
                    _raf.read( _buffer, 0 , 4 );
                    Integer _compressedSize = fourBytesToInt( _buffer );
                    _raf.read( _buffer, 0 , 4 );
                    Integer _uncompressedSize = fourBytesToInt( _buffer );

                    System.out.println( String.format( "\tDataBlock : %d : %d", _compressedSize, _uncompressedSize ) );
                    
                    byte[] _temp = new byte[ _compressedSize ];
                    _raf.read( _temp, 0, _compressedSize );
                    
                    try
                    {
                        System.arraycopy( expand( _temp, _compressedSize, _uncompressedSize )
                                , 0, _entry.rawData, _countBig, _uncompressedSize );
                    }
                    catch( DataFormatException ex )
                    {
                        Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    
                    _countBig += _uncompressedSize;
                    _countLittle += _compressedSize;
                }
                _entry.compressedSize = _countLittle;
                
                System.out.println( String.format( "\t\tbuffer read :: %d", _countBig ) );

                // then uncompress the data
//                expand( _entry.rawData );
                
                // then restore the file pointer for next directory entry
                
                _raf.seek( _returnPointer );
            } 
            
            // process the file name list
            
            
            
            // process the footer
            
            byte[] _footer = new byte[ 5 ];
            _raf.read( _footer, 0, 5 );
            String _tag = new String( _footer, 0, 5 );
            
            _raf.read( _footer, 0, 4 );
            Integer _date = fourBytesToInt( _footer );
            
            System.out.println( String.format( "%s : %d", _tag, _date ) );
            
            DirectoryEntry _fileListing = _directoryMap.lastEntry().getValue();
            try
            {
                expand( _fileListing.rawData
                        , _fileListing.compressedSize
                        , _fileListing.inflatedSize );
            }
            catch( DataFormatException ex )
            {
                LOG.log( Level.SEVERE, null, ex );
            }
            
            Iterator<DirectoryEntry> _dirIter = _directoryMap.values().iterator();
            DirectoryEntry _current = _dirIter.next();
            int _currentOffset = 0;
            byte[] _aFileName = new byte[ 256 ];
            
            System.out.println( _fileListing.toString() );
            
            int _cur = 8;
            while( _cur < _fileListing.inflatedSize )
            {
                if( _cur == _fileListing.inflatedSize - 1 )
                {
                    _current.fileName = new String( _aFileName, 0, _currentOffset );
                    _currentOffset = 0; 
                }
                else if( _fileListing.rawData[ _cur ] < 32
                        && _fileListing.rawData[ _cur + 1 ] == 0
                        && _fileListing.rawData[ _cur + 2 ] == 0 
                        && _fileListing.rawData[ _cur + 3 ] == 0 )
                {
                    _current.fileName = new String( _aFileName, 0, _currentOffset - 1 );
                    System.out.println( "FileName :: " + _current.fileName );

                    _currentOffset = 0;
                    _current = _dirIter.next();
                    _cur += 3;
                }
                else
                {
                    _aFileName[ _currentOffset ] = _fileListing.rawData[ _cur ];
                    _currentOffset ++;
                }
                
                _cur += 1;
            }
            
            hexDump( _directoryMap.firstEntry().getValue().rawData );
//            try
//            {
                System.out.println( _directoryMap.firstEntry().getValue() );
//                expand( _directoryMap.firstEntry().getValue().rawData
//                        , _directoryMap.firstEntry().getValue().compressedSize
//                        , _directoryMap.firstEntry().getValue().inflatedSize );
                
                String _path = _file.getParent();
                File _f = new File( _path + "/" + _directoryMap.firstEntry().getValue().fileName );
                FileOutputStream _fos = new FileOutputStream( _f );
                _fos.write( _directoryMap.firstEntry().getValue().rawData );
//            }
//            catch( DataFormatException ex )
//            {
//                LOG.log( Level.SEVERE, null, ex );
//            }
//            hexDump( _directoryMap.firstEntry().getValue().rawData );
        }
//        catch( DataFormatException ex )
//        {
//            LOG.log( Level.SEVERE, null, ex );
//        }
    }
    

    public void hexDump( byte[] _buffer )
    {
        int _colCount = 0;
        for( int _cur = 0; _cur < _buffer.length; _cur ++ )
        {
            if( _colCount % 16 == 0 )
            {
                System.out.println();
            }

            if( _buffer[ _cur ] < 32 )
            {
                System.out.print( String.format( "%02x ", _buffer[ _cur ] ) );
            }
            else
            {
                System.out.print( String.format( "%c ", _buffer[ _cur ] ) );
            }
            _colCount ++;
        }
        System.out.println();
    }
    
    
    public byte[] expand( byte[] _buffer, int _compSize, int _expSize ) 
    throws DataFormatException
    {
        byte[] _temp = new byte[ _expSize ];
        Inflater _inflater = new Inflater();
        
        _inflater.setInput( _buffer, 0, _compSize );

        int _count = _inflater.inflate( _temp );
//        System.arraycopy( _temp, 0, _buffer, 0, _count );
        
        System.out.println( String.format( "\texpand: %d, %d, %d", _compSize, _expSize, _count ) );
        
        return _temp;
    }
    
    
    public static class FileHeader
    {
        public FileHeader()
        {
        }

        public FileHeader( Integer _offset, String _cookie, Integer _magicValue )
        {
            this.offset = _offset;
            this.cookie = _cookie;
            this.magicValue = _magicValue;
        }
        
        public Integer offset;
        public String cookie;
        public Integer magicValue;  // usually 131072


        @Override
        public String toString()
        {
            return "FileHeader{" + "offset=" + offset 
                    + ", cookie=" + cookie
                    + ", magicValue=" + magicValue
                    + '}';
        }
    }
    
    
    public static class DirectoryEntry
    {
        public DirectoryEntry( Integer _crc, Integer _offset, Integer _inflatedSize )
        {
            this.crc = _crc;
            this.offset = _offset;
            this.inflatedSize = _inflatedSize;
            
            this.rawData = new byte[ _inflatedSize ];
        }

        public int crc;
        public int offset;
        public int inflatedSize;
        
        public int compressedSize;
        public byte[] rawData;
        public String fileName;


        @Override
        public String toString()
        {
            return "DirectoryEntry{" 
                    + "crc=" + crc 
                    + ", offset=" + offset
                    + ", inflatedSize=" + inflatedSize
                    + ", compressedSize=" + compressedSize 
                    + ", rawData length=" + rawData.length
                    + ", fileName=" + fileName + '}';
        }
        
        
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
    
    
//    public static class DataHeader
//    {
//        public DataHeader( Integer _compressedSize, Integer _inflatedSize )
//        {
//            this.compressedSize = _compressedSize;
//            this.inflatedSize = _inflatedSize;
//        }
//
//        public Integer compressedSize;
//        public Integer inflatedSize;
//    }
    
}
