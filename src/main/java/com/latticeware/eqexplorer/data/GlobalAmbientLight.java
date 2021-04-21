/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

import com.latticeware.eqexplorer.Munger;

/**
 *
 * @author sfisque
 */
public class GlobalAmbientLight
extends Fragment_wld
{
    public Integer argbColor;
    
    
    @Override
    public void processBytes( byte[] _buffer )
    {
        argbColor = Munger.fourBytesToInt( _buffer );
    }

    @Override
    public String toString()
    {
        return "GlobalAmbientLight{" 
                + "size=" + size
                + ", id=" + id
                + ", argbColor=" + argbColor 
                + '}';
    }
    
    
    
}
