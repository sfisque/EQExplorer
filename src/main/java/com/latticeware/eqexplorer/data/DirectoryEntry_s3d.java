/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

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
}
