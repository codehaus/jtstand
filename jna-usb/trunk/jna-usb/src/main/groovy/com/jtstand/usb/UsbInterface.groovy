/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Platform
/**
 *
 * @author albert_kurucz
 */
class UsbInterface extends Structure{
    /**
     * struct usb_interface_descriptor *altsetting;
     */
    //public UsbInterfaceDescriptor.ByReference altsetting;
    public Pointer altsetting
    /**
     * int num_altsetting;
     */
    public int num_altsetting

    UsbInterface(){
        super()
        if(Platform.isWindows()){
            setAlignType(Structure.ALIGN_NONE)
        }
    }

    UsbInterfaceDescriptor[] getUsbInterfaceDescriptors(){
        ((num_altsetting==0)||(altsetting==null))?null:Structure.updateStructureByReference(UsbInterfaceDescriptor, null, altsetting)?.toArray(num_altsetting)
    }

    def print(){
//        if(num_altsetting > 0){
//            if(altsetting != null){
//                if(num_altsetting > 1){
//                    UsbInterfaceDescriptor[] descriptors=Structure.updateStructureByReference(UsbInterfaceDescriptor, null, altsetting)?.toArray(num_altsetting)
//                    for(int i=0;i<num_altsetting;i++){
//                        println 'altsetting #'+ i
//                        descriptors[i].print()
//                    }
//                }else{
//                    Structure.updateStructureByReference(UsbInterfaceDescriptor, null, altsetting).print()
//                }
//            }
//        }
        getUsbInterfaceDescriptors()?.each({it.print()})
    }
}

