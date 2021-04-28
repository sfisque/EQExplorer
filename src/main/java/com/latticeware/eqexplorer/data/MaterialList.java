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
public class MaterialList
extends Fragment_Ref_wld
{  
    public Integer flags;
    public Integer materialCount;
    public List<Integer> materialList = new ArrayList<>();

    
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
        materialCount = Munger.fourBytesToInt( _slice );
        
        for( int _i = 0; _i < materialCount; _i++ )
        {
            _index += 4;
            _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
            materialList.add( Munger.fourBytesToInt( _slice ) );
        }
    }

    
    @Override
    public String toString()
    {
        return "Material{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", flags=" + flags 
                + ", materialCount=" + materialCount 
                + ", materialList=" + materialList 
                + '}';
    }

    
}
