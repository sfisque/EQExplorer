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
public class FileHeader
{
    
    public FileHeader( Integer _offset, String _cookie, Integer _magicValue )
    {
        this.offset = _offset;
        this.cookie = _cookie;
        this.magicValue = _magicValue;
    }
    public Integer offset;
    public String cookie;
    public Integer magicValue; // usually 13107
    
}
