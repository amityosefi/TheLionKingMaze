<<<<<<< HEAD
package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class MazeSound extends Canvas
{
    StringProperty startMusic = new SimpleStringProperty();
    StringProperty endMusic = new SimpleStringProperty();

    private Media media = null;
    private MediaPlayer mediaPlayer = null;


    public void setStartMusic(String startMusic) {
        this.startMusic.set(startMusic);
    }

    public void setEndMusic(String endMusic) {
        this.endMusic.set(endMusic);
    }

    public String getStartMusic() {
        return startMusic.get();
    }

    public StringProperty startMusicProperty() {
        return startMusic;
    }

    public String getEndMusic() {
        return endMusic.get();
    }

    public boolean musisExist()
    {
        return (mediaPlayer != null);
    }

    public void muteMusic()
    {
        mediaPlayer.setMute(true);
    }

    public void unmuteMusic()
    {
        mediaPlayer.setMute(false);
    }

    public void playStartMusic() {

        if (media != null)
            mediaPlayer.stop();

        try {

            media = new Media(new File(getStartMusic()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (Exception e) {
            System.out.println("There is no start sound....");
        }
    }

    public void playEndMusic() {

        if (media != null)
            mediaPlayer.stop();

        try {

            media = new Media(new File(getEndMusic()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

        } catch (Exception e) {
            System.out.println("There is no end sound....");
        }
    }
=======
package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class MazeSound extends Canvas
{
    StringProperty startMusic = new SimpleStringProperty();
    StringProperty endMusic = new SimpleStringProperty();

    private Media media = null;
    private MediaPlayer mediaPlayer = null;


    public void setStartMusic(String startMusic) {
        this.startMusic.set(startMusic);
    }

    public void setEndMusic(String endMusic) {
        this.endMusic.set(endMusic);
    }

    public String getStartMusic() {
        return startMusic.get();
    }

    public StringProperty startMusicProperty() {
        return startMusic;
    }

    public String getEndMusic() {
        return endMusic.get();
    }

    public boolean musisExist()
    {
        return (mediaPlayer != null);
    }

    public void muteMusic()
    {
        mediaPlayer.setMute(true);
    }

    public void unmuteMusic()
    {
        mediaPlayer.setMute(false);
    }

    public void playStartMusic() {

        if (media != null)
            mediaPlayer.stop();

        try {

            media = new Media(new File(getStartMusic()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (Exception e) {
            System.out.println("There is no start sound....");
        }
    }

    public void playEndMusic() {

        if (media != null)
            mediaPlayer.stop();

        try {

            media = new Media(new File(getEndMusic()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

        } catch (Exception e) {
            System.out.println("There is no end sound....");
        }
    }
>>>>>>> 61bbfae54443a08292d12e13556d9d38a6627a8d
}