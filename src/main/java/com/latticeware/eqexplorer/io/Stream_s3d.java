/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.io;

import com.latticeware.eqexplorer.Main;
import com.latticeware.eqexplorer.Munger;
import com.latticeware.eqexplorer.data.DirectoryEntry_s3d;
import com.latticeware.eqexplorer.data.FileHeader_s3d;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author sfisque
 */
public class Stream_s3d
{
    private static final Logger LOG = Logger.getLogger( Stream_s3d.class.getName() );


    public void load( String _fileName ) 
    throws IOException
    {
        File _file = new File( _fileName );
        FileHeader_s3d _archiveHeader = new FileHeader_s3d();
        try (final RandomAccessFile _raf = new RandomAccessFile( _file, "r" ))
        {
            byte[] _buffer = new byte[ 4 ];
            _raf.read( _buffer, 0, 4 );
            _archiveHeader.offset = Munger.fourBytesToInt( _buffer );
            _raf.read( _buffer, 0, 4 );
            _archiveHeader.cookie = new String( _buffer );
            _raf.read( _buffer, 0, 4 );
            _archiveHeader.magicValue = Munger.fourBytesToInt( _buffer );
            LOG.log( Level.INFO, "{0}", _archiveHeader );
            _raf.seek( _archiveHeader.offset );
            //            Integer _numEntries = _raf.readUnsignedShort();
            _raf.read( _buffer, 0, 4 );
            Integer _numEntries = Munger.fourBytesToInt( _buffer );
            LOG.log( Level.INFO, "ArchiveHeader : {0} : {1} : {2}", new Object[ ]{ _file.length(), _archiveHeader.offset, _numEntries } );
            TreeMap<Integer, DirectoryEntry_s3d> _directoryMap = new TreeMap<>();
            for ( int _i = 0; _i < _numEntries; _i++ )
            {
                _raf.read( _buffer, 0, 4 );
                Integer _crc = Munger.fourBytesToInt( _buffer );
                _raf.read( _buffer, 0, 4 );
                Integer _dataOffset = Munger.fourBytesToInt( _buffer );
                _raf.read( _buffer, 0, 4 );
                Integer _inflatedLength = Munger.fourBytesToInt( _buffer );
                DirectoryEntry_s3d _entry = new DirectoryEntry_s3d( _crc, _dataOffset, _inflatedLength );
                _directoryMap.put( _dataOffset, _entry );
                LOG.log( Level.INFO, "Directory Entry : {0}: {1} : {2} : {3}", new Object[ ]{ _i, _crc, _dataOffset, _inflatedLength } );
                Long _returnPointer = _raf.getFilePointer();
                _raf.seek( _dataOffset );
                int _countBig = 0;
                int _countLittle = 0;
                while ( _countBig < _entry.inflatedSize )
                {
                    _raf.read( _buffer, 0, 4 );
                    Integer _compressedSize = Munger.fourBytesToInt( _buffer );
                    _raf.read( _buffer, 0, 4 );
                    Integer _uncompressedSize = Munger.fourBytesToInt( _buffer );
                    LOG.log( Level.INFO, "\tDataBlock : {0}: {1}", new Object[ ]{ _compressedSize, _uncompressedSize } );
                    byte[] _temp = new byte[ _compressedSize ];
                    _raf.read( _temp, 0, _compressedSize );
                    try
                    {
                        System.arraycopy( Munger.expand( _temp, _compressedSize, _uncompressedSize ), 0, _entry.rawData, _countBig, _uncompressedSize );
                    }
                    catch ( DataFormatException ex )
                    {
                        Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    _countBig += _uncompressedSize;
                    _countLittle += _compressedSize;
                }
                _entry.compressedSize = _countLittle;
                LOG.log( Level.INFO, "\t\tbuffer read :: {0}", _countBig );
                // then restore the file pointer for next directory entry
                _raf.seek( _returnPointer );
            }
            // process the footer
            byte[] _footer = new byte[ 5 ];
            _raf.read( _footer, 0, 5 );
            String _tag = new String( _footer, 0, 5 );
            _raf.read( _footer, 0, 4 );
            Integer _date = Munger.fourBytesToInt( _footer );
            LOG.log( Level.INFO, "{0} : {1}", new Object[ ]{ _tag, _date } );
            // process the file name list
            DirectoryEntry_s3d _fileListing = _directoryMap.lastEntry().getValue();
            //            try
            //            {
            //                expand( _fileListing.rawData
            //                        , _fileListing.compressedSize
            //                        , _fileListing.inflatedSize );
            //            }
            //            catch( DataFormatException ex )
            //            {
            //                LOG.log( Level.SEVERE, null, ex );
            //            }
            Iterator<DirectoryEntry_s3d> _dirIter = _directoryMap.values().iterator();
            DirectoryEntry_s3d _current = _dirIter.next();
            int _currentOffset = 0;
            byte[] _aFileName = new byte[ 256 ];
            LOG.log( Level.INFO, "{0}", new Object[ ]{ _fileListing } );
            int _cur = 8;
            while ( _cur < _fileListing.inflatedSize )
            {
                if ( _cur == _fileListing.inflatedSize - 1 )
                {
                    _current.fileName = new String( _aFileName, 0, _currentOffset );
                    _currentOffset = 0;
                }
                else if ( _fileListing.rawData[_cur] < 32 && _fileListing.rawData[_cur + 1] == 0 && _fileListing.rawData[_cur + 2] == 0 && _fileListing.rawData[_cur + 3] == 0 )
                {
                    _current.fileName = new String( _aFileName, 0, _currentOffset - 1 );
                    LOG.log( Level.INFO, "FileName :: {0}", _current.fileName );
                    _currentOffset = 0;
                    _current = _dirIter.next();
                    _cur += 3;
                }
                else
                {
                    _aFileName[_currentOffset] = _fileListing.rawData[_cur];
                    _currentOffset++;
                }
                _cur += 1;
            }
            //            hexDump( _directoryMap.firstEntry().getValue().rawData );
            //            LOG.log( Level.INFO, "first entry :: {0}", _directoryMap.firstEntry().getValue() );
            //            String _path = _file.getParent();
            String _path = "/tmp";
            for ( int _i : _directoryMap.navigableKeySet() )
            {
                if ( _directoryMap.get( _i ).fileName != null )
                {
                    File _f = new File( _path + "/" + _directoryMap.get( _i ).fileName );
                    FileOutputStream _fos = new FileOutputStream( _f );
                    _fos.write( _directoryMap.get( _i ).rawData );
                    _fos.close();
                }
            }
        }
    }
    
}
