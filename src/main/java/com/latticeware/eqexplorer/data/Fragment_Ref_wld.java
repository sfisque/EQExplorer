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
public class Fragment_Ref_wld
extends Fragment_wld
{
    public Integer nameReference;
    
    
    @Override
    public void processBytes( byte[] _buffer )
    {
        byte[] _slice = new byte[] { _buffer[ 0 ], _buffer[ 1 ], _buffer[ 2 ], _buffer[ 3 ] };
        
        nameReference = Munger.fourBytesToInt( _slice );
    }

    @Override
    public String toString()
    {
        return "Fragment_Ref_wld{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + '}';
    }
    
    
    
}
