import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{

    int width = 360, height = 640;

    // loading images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
  
    // loading bird 
    int birdXpos = width/8;
    int birdYpos = height/2;
    int birdWidth = 34;
    int birdHeight = 24;
    class Bird{
        int x = birdXpos;
        int y = birdYpos;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        Bird(Image img){
            this.img=img;
        }
    }

    //loading pipes
    int pipeXpos = width;
    int pipeYpos = 0; //pipe intially at top-right
    int pipeWidth = 64;
    int pipeHeight = 512;
    class Pipe{
        int x = pipeXpos;
        int y = pipeYpos;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean pipePassed = false;
        Pipe(Image img){
            this.img = img;
        } 
    }


    // main game functionality
    Bird bird;
    int speedY = 0; //bird moving up speed 
    int gravity = 1;
    int speedX = -4;//pipe moving left speed
    ArrayList<Pipe> pipes;
    // Random random = new Random();
    
    Timer gameLoop;
    Timer placingPipesLoop;

    boolean gameOver = false;
    double score = 0;


    FlappyBird(){
        setPreferredSize(new Dimension(width,height));

        setFocusable(true); //makes sure this class takes in the keyevents
        addKeyListener(this); //validates the 3 keyListenerEvents

        backgroundImg = new ImageIcon(getClass().getResource("./images/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./images/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./images/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./images/bottompipe.png")).getImage();

        
        bird = new Bird(birdImg);
        pipes= new ArrayList<Pipe>();

        // placing pipes timer
        placingPipesLoop = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });

        // game timer
        gameLoop = new Timer(1000/60,this); // 60FPS--> we wanna draw 60 frames per sec -> implements keylistener
        gameLoop.start();
        placingPipesLoop.start();
        
    }




    //FUNCTIONS
    public void placePipes(){
        // 0 - 128 - (0-256) --> 
        // pos-height/4-0 TO pos-height/4 - height/2 -->
        // 1/4 height -> 3/4 height
        int randomPipeYpos = (int)(pipeYpos - pipeHeight/4 - Math.random()*(pipeHeight/2)); 
        int passingSpace = pipeHeight/4;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeYpos;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + passingSpace ;
        pipes.add(bottomPipe);
    }


    public void move(){       
        // bird
        bird.y += speedY;  //bird moving up
        speedY += gravity; //bird falling down
        bird.y = Math.max(0,bird.y); //limit to top of screen
        
        //pipes 
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += speedX; //pipe moving left
            if (collision(bird,pipe)) gameOver = true;
            if (!pipe.pipePassed && bird.x > pipe.x+pipe.width){
                pipe.pipePassed = true;
                score += 0.5;
            }
        }

        if (bird.y > height){
            gameOver = true;
        }

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawSomething(g);
    }


    public void drawSomething(Graphics g){
        //drawing background
        g.drawImage(backgroundImg,0,0,width,height,null);

        //drawing bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        //drawing pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("sans-serrif",Font.PLAIN,30));
        if (gameOver){
            g.drawString("GAME OVER!",9,35);
            g.drawString("SCORE : "+ String.valueOf((int)score),9,63);
        } else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }


    public boolean collision(Bird bird, Pipe pipe){
        return  bird.x < pipe.x + pipe.width &&
                bird.x + bird.width > pipe.x &&
                bird.y < pipe.y + pipe.height &&
                bird.y + bird.height > pipe.y;
    }


    

    //ACTION-PERFORMED METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (gameOver){
            placingPipesLoop.stop();
            gameLoop.stop();
        }
        repaint(); //this will call paintComponent() and redraw all the components bcz paintComponent is calling drawSomething() which draws all components and this will render 60 frames per sec due to timer passed as arguement during Timer class' calling
    }


    //KEYLISTENERS
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP){
            speedY = -7; //reset 
            if (gameOver){
                //reset conditions
                gameOver = false;
                bird.y = birdYpos;
                bird.x = birdXpos;
                pipes.clear();
                gameLoop.start();
                placingPipesLoop.start();
                
            }
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}
