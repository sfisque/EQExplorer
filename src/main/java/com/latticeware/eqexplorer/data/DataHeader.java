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
public class DataHeader
{
    
    public DataHeader( Integer _compressedSize, Integer _inflatedSize )
    {
        this.compressedSize = _compressedSize;
        this.inflatedSize = _inflatedSize;
    }
    
    public Integer compressedSize;
    public Integer inflatedSize;
    
}
/*
TS3DFileHeader = Packed Record
DirectoryOffset        : LongWord;                   // File position of the S3D directory
MagicCookie            : Packed Array[0..3] Of Char; // Always "PFS ".  It identifies the file type
Unknown_Always_131072  : LongWord;                   // As the spec says, it always contains 131072 (128k)
End;
FDirectory = array of ...
TS3DDirectoryEntry = Packed Record
CRC                    : LongWord;                   // Filename CRC.  Calculated with the standard IEEE 802.3 Ethernet CRC-32 algorithm.
DataOffset             : LongWord;                   // Position in the archive of the compressed data
DataLengthInflated     : LongWord;                   // The file size once inflated
End;
TS3DFileFooter = Packed Record
SteveCookie5           : Packed Array[0..4] Of Char; // Always "STEVE".
Date                   : LongWord;                   // I think the patcher uses this to check a file's version
End;
TS3DDataBlockHeader = Packed Record
DeflatedLength         : LongWord;                   // Compressed size
InflatedLength         : LongWord;                   // Uncompressed size
End;
 */