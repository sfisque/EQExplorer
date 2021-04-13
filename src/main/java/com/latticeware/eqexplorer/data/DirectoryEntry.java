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
public class DirectoryEntry
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
