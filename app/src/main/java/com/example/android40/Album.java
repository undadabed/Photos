package com.example.android40;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * An album object
 * Represents an album by holding a list of photos and bookkeping information
 * @author Jonathan
 */

public class Album implements Serializable {
    /**
     * Keeps track of album name
     */
    private String album;
    /**
     * Keep track of amount of photos in album
     */
    private int photoCount;
    /**
     * Keep track of earliest date of any photo in the album
     */
    private Date startDate;
    /**
     * Keeps track of the latest date of any photo in the album
     */
    private Date endDate;
    /**
     * List of all photos in album
     */
    private ArrayList<Photo> photos;

    /**
     * Initialize album with a name and photo count
     * @param name album name
     * @param count amount of photos in album at start
     */
    public Album(String name, int count) {
        setAlbum(name);
        setPhotoCount(count);
        startDate = null;
        endDate = null;
        photos = new ArrayList<Photo>();
    }

    /**
     * Gets all photos in the album
     * @return Arraylist of all photos in the album
     */
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    /**
     * Sets album to new arraylist of photos
     * @param photos new arraylist of photos
     */
    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
        this.photoCount = photos.size();
    }

    /**
     * Get latest date of any photo in the album
     * @return latest date of any photo in the album
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Update end date (new latest date of any photo in album)
     * @param endDate new end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    /**
     * Get the earliest date any photo was taken in the album
     * @return earliest date of any photo in the album
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Update start date (earliest date of any photo in album)
     * @param startDate new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the amount of photos in the album
     * @return amount of photos in the album
     */
    public int getPhotoCount() {
        return photoCount;
    }

    /**
     * Update the amount of photos in the album
     * @param photoCount new photo count
     */
    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    /**
     * Get album name
     * @return album name
     */
    public String getAlbum() {
        return album;
    }
    /**
     * Change album name
     * @param album new album name
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Add a photo to the album
     * @param p new photo
     */
    public void addPhoto(Photo p) {
        photos.add(p);
        if (startDate == null) {
            startDate = p.getTime();
        }
        if (endDate == null) {
            endDate = p.getTime();
        }
        if (p.getTime().after(endDate)) {
            endDate = p.getTime();
        }
        if (startDate.after(p.getTime())) {
            startDate = p.getTime();
        }
        this.photoCount++;
    }

    /**
     * Delete a photo from the album
     * @param p photo to delete
     */
    public void deletePhoto(Photo p){
        Date save = p.getTime();
        photos.remove(p);

        if (getStartDate().equals(save)) {
            Date min = null;
            for (Photo ph : getPhotos()) {
                if (min == null) {
                    min = ph.getTime();
                }
                if (min.after(ph.getTime())) {
                    min = ph.getTime();
                }
            }
            setStartDate(min);
        }
        if (getEndDate().equals(save)) {
            Date max = null;
            for (Photo ph : getPhotos()) {
                if (max == null) {
                    max = ph.getTime();
                }
                if (ph.getTime().after(max)) {
                    max = ph.getTime();
                }
            }
            setEndDate(max);
        }

        photoCount--;

    }

    /**
     * Gets a photo at a certain index
     * @param index index of desired photo
     * @return photo that is at the given index
     */
    public Photo indexOf(int index){
        return photos.get(index);
    }

    public String toString() {
        if (startDate == null || endDate == null) {
            return album + "\n" + photoCount + " photos";
        }
        return album + "\n" + photoCount + " photos from " + startDate + " to " + endDate;
    }
}

