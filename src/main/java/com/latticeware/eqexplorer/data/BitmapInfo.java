/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;


import com.latticeware.eqexplorer.Munger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author sfisque
 */
public class BitmapInfo
extends Fragment_Ref_wld
{  
    public Integer flags;

    public List<Integer> bitmapNames = new ArrayList<>();

    public Integer animationDelay;

    
    @Override
    public void processBytes( byte[] _buffer )
    {
        super.processBytes( _buffer );
        
        byte[] _slice = Arrays.copyOfRange( _buffer, 4, 8 );
        flags = Munger.fourBytesToInt( _slice );
        
        _slice = Arrays.copyOfRange( _buffer, 8, 12 );
        Integer _count = Munger.fourBytesToInt( _slice );

        _slice = Arrays.copyOfRange( _buffer, 12, 16 );
        animationDelay = Munger.fourBytesToInt( _slice );
        
        for( int _i = 0; _i < _count; _i ++ )
        {
            _slice = Arrays.copyOfRange( _buffer, 16 + _i * 4, 20 + _i * 4 );
            bitmapNames.add( Munger.fourBytesToInt( _slice ) );
        }
    }
    
    
    public Boolean IsAnimated()
    {
        return ( flags.intValue() & 0x08 ) != 0;
    }

    
    @Override
    public String toString()
    {
        return "BitmapInfo{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", flags=" + flags 
                + ", animationDelay=" + animationDelay 
                + ", bitmapNames=" + bitmapNames 
                + '}';
    }
    
}
