package SongLibrary;

public class Song { //Song datatype
    
    //variables
    private String name, artist, album, year;



    //constructors
    public Song(String name, String artist, String album, String year){
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    //methods
    public String getName() {
        return name;
    }
    public String getAlbum() {
        return album;
    }
    public String getArtist() {
        return artist;
    }
    public String getYear() {
        return year;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return name + ", by " + artist;
    }

}