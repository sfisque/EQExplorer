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
public class BitmapInfoReference
extends Fragment_Ref_wld
{  
    public Integer bitMapInfo;
    public Integer flags;

    
    @Override
    public void processBytes( byte[] _buffer )
    {
        super.processBytes( _buffer );
        int _index = 0;
        
        _index += 4;
        byte[] _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        bitMapInfo = Munger.fourBytesToInt( _slice );

        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        flags = Munger.fourBytesToInt( _slice );
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
                + ", bitMapInfo=" + bitMapInfo 
                + ", flags=" + flags 
                + '}';
    }
    
}
