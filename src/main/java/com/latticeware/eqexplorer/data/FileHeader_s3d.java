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
public class FileHeader_s3d
{
    public FileHeader_s3d()
    {
    }

    public FileHeader_s3d( Integer _offset, String _cookie, Integer _magicValue )
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
