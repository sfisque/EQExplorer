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
public class BspTree
extends Fragment_Ref_wld
{  
    public Integer bspCount;
    public List<Integer> bspList = new ArrayList<>();

    
    @Override
    public void processBytes( byte[] _buffer )
    {
        super.processBytes( _buffer );
        int _index = 0;

        _index += 4;
        byte[] _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        bspCount = Munger.fourBytesToInt( _slice );
        
        for( int _i = 0; _i < bspCount; _i++ )
        {
            _index += 4;
            _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
            bspList.add( Munger.fourBytesToInt( _slice ) );
        }
    }

    
    @Override
    public String toString()
    {
        return "BspTree{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", bspCount=" + bspCount 
                + ", bspList=" + bspList 
                + '}';
    }

    
}
