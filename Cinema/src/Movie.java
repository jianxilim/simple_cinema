/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author sheng
 */

public class Movie {
    private String title;
    private String genre;
    private int duration;
    private String rating;
    private String contactEmail; 

    public Movie(String title, String genre, int duration, String rating, String email) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.contactEmail = email;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public String getRating() { return rating; }
    public String getContactEmail() { return contactEmail; }

    public void setTitle(String title) { 
        this.title = title; 
    }

    public void setGenre(String genre) { 
        this.genre = genre; 
    }

    public void setDuration(int duration) { 
        this.duration = duration; 
    }

    public void setRating(String rating) { 
        this.rating = rating; 
    }

    public void setContactEmail(String email) { 
        this.contactEmail = email; 
    }
    
    public String toFileString() {
        return title + "|" + genre + "|" + duration + "|" + rating + "|" + contactEmail;
    }
    
    public static Movie fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
            return new Movie(parts[0], parts[1], Integer.parseInt(parts[2]), 
                             parts[3], parts[4]);
        }
        return null;
    }
}