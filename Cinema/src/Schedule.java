/**
 * @author sheng
 */

import java.util.ArrayList;

public class Schedule {
    private Movie movie;
    private Hall hall;
    private String showTime;

    public Schedule(Movie movie, Hall hall, String showTime) {
        this.movie = movie;
        this.hall = hall;
        this.showTime = showTime;
    }

    public void display() {
        System.out.printf("%-10s | %-15s | %-25s | %d min\n", 
            this.showTime, 
            this.hall.getName(), 
            this.movie.getTitle(), 
            this.movie.getDuration());
    }
    
    public Hall getHall() { return hall; }
    public Movie getMovie() { return movie; }
    public String getShowTime() { return showTime; }
    
    public String toFileString() {
        return movie.getTitle() + "|" + hall.getName() + "|" + showTime;
    }
    
    public static Schedule fromFileString(String line, ArrayList<Movie> movies, ArrayList<Hall> halls) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) return null;
        
        String movieTitle = parts[0];
        String hallName = parts[1];
        String showTime = parts[2];
        
        Movie foundMovie = null;
        for (Movie m : movies) {
            if (m.getTitle().equals(movieTitle)) {
                foundMovie = m;
                break;
            }
        }
        
        Hall foundHall = null;
        for (Hall h : halls) {
            if (h.getName().equals(hallName)) {
                foundHall = h;
                break;
            }
        }
        
        if (foundMovie != null && foundHall != null) {
            return new Schedule(foundMovie, foundHall, showTime);
        }
        return null;
    }
}
