/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

import com.latticeware.eqexplorer.Munger;
import java.util.Arrays;

/**
 *
 * @author sfisque
 */
public class Material
extends Fragment_Ref_wld
{  
    public Integer flags;
    public Integer parameters;
    public Integer argb;
    public Integer fragmentReference;

    
    @Override
    public void processBytes( byte[] _buffer )
    {
        super.processBytes( _buffer );
        int _index = 0;

        _index += 4;
        byte[] _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        flags = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        parameters = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        argb = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        fragmentReference = Munger.fourBytesToInt( _slice );
    }

    
    @Override
    public String toString()
    {
        return "Material{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", flags=" + flags 
                + ", parameters=" + parameters 
                + ", argb=" + argb 
                + ", fragmentReference=" + fragmentReference
                + '}';
    }

    
}
