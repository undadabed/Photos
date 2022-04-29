package com.example.android40;

import android.app.Person;
import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    private ArrayList<PersonTag> pTags;

    private ArrayList<LocationTag> lTags;

    /**
     * Create new photo with path given
     * @param path path to the photo
     */
    public Photo(String path) {
        pTags = new ArrayList<>();
        lTags = new ArrayList<>();
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

    /**
     * Add a new tag
     * @param t new tag to add
     */
    public void addTag(Object t){
        if(t instanceof PersonTag){
            pTags.add((PersonTag) t);
        }else if(t instanceof LocationTag){
            lTags.add((LocationTag) t);
        }
    }



    public ArrayList<PersonTag> getPersonTags(){
        return pTags;
    }

    public ArrayList<LocationTag> getLocationTags(){
        return lTags;
    }




    public void setTags(ArrayList<PersonTag> p, ArrayList<LocationTag> t){
        pTags.clear();
        pTags.addAll(p);
        lTags.clear();
        lTags.addAll(t);
    }

    public void removeTagByIndexP(int index){
        if(index < pTags.size() || index >= 0)
            pTags.remove(index);

    }

    public void removeTagByIndexL(int index){
        if(index < lTags.size() || index >= 0)
            lTags.remove(index);

    }

    public String toString() {
        return path + "\n" + caption;
    }
}
