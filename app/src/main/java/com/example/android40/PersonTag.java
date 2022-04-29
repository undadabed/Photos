package com.example.android40;

import java.io.Serializable;

public class PersonTag implements Serializable {

    private static final String typeP = "person";
    private String value;
    /**
     * Create a new tag
     *
     * @param v value of new tag
     */
    public PersonTag(String v) {
        this.value = v;
    }


    /**
     * Update tag's value
     * @param v new value
     */
    public void setValue(String v){
        this.value = v;
    }

    /**
     * Get tag's value
     * @return tag's value
     */
    public String getValue(){
        return value;
    }

    /**
     * Tostring a tag, just returns the type
     */
    public String toString(){
        return value;
    }

    public boolean equals(Object o){
        if(o instanceof PersonTag){
            return ((PersonTag)o).value.equals(this.value);
        }else
            return false;
    }




}
