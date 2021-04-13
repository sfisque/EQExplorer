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
public class Footer_s3d
{
    
    public Footer_s3d( String _signature, Integer _date )
    {
        this.signature = _signature;
        this.date = _date;
    }
    public String signature;
    public Integer date;
    
}
