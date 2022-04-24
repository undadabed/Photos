package com.example.android40;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Photo object
 * Represents a photo by containing the file path to an image
 * Also has bookkeeping information
 * @author Jonathan
 */


public class Photo implements Serializable {
    /**
     * Absolute file path to the photo
     */
    private String path;
    /**
     * Time when the photo was created
     */
    private Date time;
    /**
     * Caption for the photo
     */
    private String caption;
    /**
     * All the tags for the photo
     */
    private ArrayList<Tag> tags;

    /**
     * Create new photo with path given
     * @param path path to the photo
     */
    public Photo(String path) {
        tags = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        setTime(cal.getTime());
        this.setPath(path);
        setCaption("");
    }

    /**
     * Get the caption
     * @return caption as a string
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Update the caption
     * @param caption new caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Get date when picture was created
     * @return date
     */
    public Date getTime() {
        return time;
    }

    /**
     * Update the date
     * @param time new date
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Get absolute path to photo
     * @return absolute path
     */
    public String getPath() {
        return path;
    }

    /**
     * Update absolute path
     * @param path new absolute path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Update tags
     * @param t new tags
     */
    public void setTags(ArrayList<Tag> t){
        this.tags = t;
    }

    /**
     * Add a new tag
     * @param t new tag to add
     */
    public void addTag(Tag t){
        tags.add(t);
    }


    /**
     * @param t
     */
    public void deleteTag(Tag t){
        tags.remove(t);
    }

    /**
     * Get tags
     * @return tags
     */
    public ArrayList<Tag> getTags(){
        return tags;
    }

    /**
     * Set a tag of type t to v
     * @param t tag type to find
     * @param v new value to set to
     * @return true if updated, false otherwise
     */
    public boolean setTag(String t, String v){
        for(Tag tag : tags){
            if(tag.getType().equals(t)){
                tag.setValue(v);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes all tags of type t
     * @param t tag type to delete
     */
    public void removeTagType(String t){
        ArrayList<Tag> tagT = new ArrayList<>();
        for(Tag ta : tags){
            if(ta.getType().equals(t))
                tagT.add(ta);
        }
        for(Tag ta: tagT){
            tags.remove(ta);
        }
    }

    public String toString() {
        return path + "\n" + caption;
    }
}
