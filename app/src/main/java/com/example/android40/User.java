package com.example.android40;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User object
 * Represents each individual user
 * Keeps track of all relevant information for each user
 * Keeps track of username, albums, and tags
 * @author Jonathan
 */

public class User implements Serializable{
    /**
     * Keep track of user's username
     */
    private String username;
    /**
     * Keep track of user's albums
     */
    private ArrayList<Album> albums;
    /**
     * Keep track of user's unique tag types
     */
    private ArrayList<String> tagTypes;

    /**
     * Initialize a new user with a name
     * @param username user's name
     */
    public User(String username) {
        this.setUsername(username);
        albums = new ArrayList<Album>();
        this.tagTypes = new ArrayList<>();
        tagTypes.add("person");
        tagTypes.add("location");
    }

    /**
     * Get user's name
     * @return user's name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Update user's name
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get user's albums
     * @return user's albums
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Update user's albums
     * @param albums new albums
     */
    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    /**
     * Add an album
     * @param album album to add
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    /**
     * Get the user's tag types
     * @return user's tag types
     */
    public ArrayList<String> getTagTypes(){
        return tagTypes;
    }

    /**
     * Creates and returns an arraylist of tags with the same types but no value
     * @return arraylist of tags with the same types but no value
     */
    public ArrayList<Tag> getTagShells(){
        ArrayList<Tag> tags = new ArrayList<>();
        for(String t : tagTypes){
            tags.add(new Tag(t, null));
        }
        return tags;
    }

    /**
     * Add a new tag type
     * @param type new tag type
     */
    public void addTagType(String type){
        tagTypes.add(type);
    }

    /**
     * Delete a tag type
     * @param type tag type to delete
     */
    public void deleteTagType(String type){
        tagTypes.remove(type);
        for(Album a : albums){
            for(Photo p : a.getPhotos()){
                p.removeTagType(type);
            }
        }
    }
}

