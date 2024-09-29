import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class App {
    public static void main(String[] args){

        int width = 360;
        int height = 640; //bcz bg-img is of those dimensions
        JFrame frame = new JFrame("FLAPPY BIRD");
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null); //centers the frame
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); //prevents overflow of components
        flappyBird.requestFocus();

        frame.setVisible(true);
        
    }
}
