/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author sheng
 */
public class Hall {
    private String name;
    private int capacity;
    private boolean[][] seats;

    public Hall(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.seats = new boolean[10][25];
    }

    public String getName() { return name; }
    public boolean[][] getSeats() { return seats; }
    public int getCapacity() { return capacity; }

    public void setSeatStatus(int row, int col, boolean status) {
        this.seats[row][col] = status;
    }
    
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("|").append(capacity).append("|");
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                sb.append(seats[i][j] ? "1" : "0");
                if (!(i == seats.length - 1 && j == seats[i].length - 1)) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }
    
    public static Hall fromFileString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 2) return null;
            String name = parts[0];
            int capacity = Integer.parseInt(parts[1]);
            Hall hall = new Hall(name, capacity);
            
            if (parts.length >= 3) {
                String[] seatValues = parts[2].split(",");
                int index = 0;
                for (int i = 0; i < 10 && index < seatValues.length; i++) {
                    for (int j = 0; j < 25 && index < seatValues.length; j++) {
                        hall.seats[i][j] = seatValues[index].equals("1");
                        index++;
                    }
                }
            }
            return hall;
        } catch (Exception e) {
            return null;
        }
    }
}
