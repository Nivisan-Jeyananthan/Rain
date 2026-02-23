package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.Container;
import ch.nivisan.raincloud.serialization.ContainerType;

/**
 * Stream of bytes
 */
public class Field extends Container{
    public Field(String name, byte type){
    	super(ContainerType.Field,name, type);
    }
}
