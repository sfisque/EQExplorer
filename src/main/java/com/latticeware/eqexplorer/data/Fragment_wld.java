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
public class Fragment_wld
{        
    public Integer size;
    public Integer id;
    
    
    @Override
    public String toString()
    {
        return String.format( "Fragment_wld{ size=%d, id=%d }"
                , size, id );
    }
    
    
    public void processBytes( byte[] _buffer )
    {
        // no op
    }
}
