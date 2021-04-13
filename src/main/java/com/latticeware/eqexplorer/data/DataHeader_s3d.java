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
public class DataHeader_s3d
{
    public Integer compressedSize;
    public Integer inflatedSize;
    
    
    public DataHeader_s3d( Integer _compressedSize, Integer _inflatedSize )
    {
        this.compressedSize = _compressedSize;
        this.inflatedSize = _inflatedSize;
    }
    
}
