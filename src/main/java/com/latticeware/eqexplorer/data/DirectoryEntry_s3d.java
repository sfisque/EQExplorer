/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 *
 * @author sfisque
 */
public class DirectoryEntry_s3d
{
        public DirectoryEntry_s3d( Integer _crc, Integer _offset, Integer _inflatedSize )
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
        
        
        public void save( String _path ) 
        throws IOException
        {
            if ( this.fileName != null )
            {
                File _f = new File( _path + "/" + this.fileName );
                try( FileOutputStream _fos = new FileOutputStream( _f ) )
                {
                    _fos.write( this.rawData );
                }
            }
        }
}
