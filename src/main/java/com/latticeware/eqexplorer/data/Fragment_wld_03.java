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
public class Fragment_wld_03
implements Fragment_wld_extra
{  
    /*
    Size1 : DWORD
Contains the number of texture filenames in this fragment. Most of the time there is only one name. Entries (there are Size1 of them):
NameLength: WORD
Contains the length of the filename in bytes.
FileName: BYTEs
    */
    public Integer size1;
    public Short nameLength;
    public String fileName;
}
