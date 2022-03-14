/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

import com.jme3.math.Vector3f;
import com.latticeware.eqexplorer.Munger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sfisque
 */
public class Mesh
extends Fragment_Ref_wld
{  
    public Integer flags;
    public Integer materialCount;
    public Integer meshAnimation;
    public Integer unknown1;
    public Integer unknown2;
    
    public Vector3f center;
    
    public Integer unknown3;
    public Integer unknown4;
    public Integer unknown5;
    
    public Float maxDistance;
    public Vector3f minPosition;
    public Vector3f maxPosition;
    
    public Integer vertexCount;
    public Integer textureCoordinateCount;
    public Integer normalsCount;
    public Integer colorsCount;
    public Integer polygonCount;
    public Integer vertexPieceCount;
    public Integer polygonTextureCount;
    public Integer vertexTextureCount;
    public Integer size9;
    public Float scale;
    
    public List<Vector3f> vertices;
    public List<Vector3f> normals;
    


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
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        meshAnimation = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        unknown1 = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        unknown2 = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 12 );
        center = Munger.twelveBytesToVector3f( _slice );
        
        _index += 12;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        unknown3 = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        unknown4 = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        unknown5 = Munger.fourBytesToInt( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 4 );
        maxDistance = Munger.fourBytesToFloat( _slice );
        
        _index += 4;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 12 );
        minPosition = Munger.twelveBytesToVector3f( _slice );
        
        _index += 12;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 12 );
        maxPosition = Munger.twelveBytesToVector3f( _slice );
        
        _index += 12;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        vertexCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        textureCoordinateCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        normalsCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        colorsCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        polygonCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        vertexPieceCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        polygonTextureCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        vertexTextureCount = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        size9 = Munger.twoBytesToInt( _slice );
        
        _index += 2;
        _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
        scale = 1.0f / ( 1 << Munger.twoBytesToInt( _slice ) );
        
        vertices = new ArrayList<>( vertexCount * 3 / 2 );
        for( int _i = 0; _i < vertexCount; _i ++ )
        {
            _index += 2;
            _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
            Float _x = 1.0f * Munger.twoBytesToInt( _slice );

            _index += 2;
            _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
            Float _y = 1.0f * Munger.twoBytesToInt( _slice );

            _index += 2;
            _slice = Arrays.copyOfRange( _buffer, _index, _index + 2 );
            Float _z = 1.0f * Munger.twoBytesToInt( _slice );
            
            vertices.add( new Vector3f( _x * scale, _y * scale, _z * scale ) );
        }
        
        // more processing, leaving for later
        
/*
            Normals = new List<vec3>();
            Colors = new List<Color>();
            TextureUvCoordinates = new List<vec2>();

        */    
        
        
        
    }


    @Override
    public String toString()
    {
        return "Mesh{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", flags=" + flags 
                + ", materialCount=" + materialCount 
                + ", meshAnimation=" + meshAnimation 
                + ", unknown1=" + unknown1 
                + ", unknown2=" + unknown2
                + ", center=" + center
                + ", unknown3=" + unknown3
                + ", unknown4=" + unknown4
                + ", unknown5=" + unknown5
                + ", maxDistance =" + maxDistance
                + ", minPosition =" + minPosition
                + ", maxPosition =" + maxPosition
                
                + ", vertexCount =" + vertexCount
                + ", textureCoordinateCount =" + textureCoordinateCount
                + ", normalsCount =" + normalsCount
                + ", colorsCount =" + colorsCount
                + ", polygonCount =" + polygonCount
                + ", vertexPieceCount =" + vertexPieceCount
                + ", polygonTextureCount =" + polygonTextureCount
                + ", vertexTextureCount =" + vertexTextureCount
                + ", size9 =" + size9
                + ", scale =" + scale

                + ", vertices =" + vertices
                + '}';
    }

}
