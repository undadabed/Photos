package com.example.android40;

import java.io.Serializable;
/**
 * Tag object
 * Represents tags by keeping type and value pairs
 * @author Pedro
 */
public class Tag implements Serializable{
    /**
     * Keeps track of the type of the tag
     */
    private String type;
    /**
     * Keeps track of the value of the tag
     */
    private String value;

    /**
     * Create a new tag
     * @param t type of new tag
     * @param v value of new tag
     */
    public Tag(String t, String v){
        this.type = t;
        this.value = v;
    }

    /**
     * Update tag's type
     * @param t new type
     */
    public void setType(String t){
        this.type = t;
    }

    /**
     * Get tag's type
     * @return tag's type
     */
    public String getType(){
        return type;
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
        return type;
    }

    /**
     * Checks if one tag has the same type nad value as another
     * @param t tag to compare to
     * @return true if they share the same type and value, false otherwise
     */
    public boolean equals(Tag t){
        return this.type == t.getType() && this.value == t.getValue();
    }



}

