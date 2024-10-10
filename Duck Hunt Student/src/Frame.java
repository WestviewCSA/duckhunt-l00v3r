import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	Font bigFont = new Font("Serif", Font.PLAIN, 60);
	Font medFont = new Font("Serif", Font.PLAIN, 30);
	Font smFont = new Font("Serif", Font.PLAIN, 15);


	
	//create duck/skeleton head
	SkeletonHead duck = new SkeletonHead();
	
	//create dog/cat
	Cat dog = new Cat();
	
	//create foreground
	Foreground fore = new Foreground();
	
	//create background
	Background back = new Background();
	
	//create tree to hide behind/graves
	Hiding grave = new Hiding();
	
	//score related vars and timer
	int roundTimer;
	int score;
	long time; //long is bigger int that can hold bigger whole numbers
	int wave;
	int randVx = (int)(Math.random()*(5+5+1)-5);
	int randVy = (int)(Math.random()*(-5+3+1)-3);
	int randX = (int)(Math.random()*(691)+10);

	
	//initialize any variables, objects, etc for the "start" of the game
	public void init() {
		//init the roundTimer and score
		roundTimer = 30;
		score = 0;
		time = 0;
		wave = 1;
	
		dog.setXY(170, 350);
		
		duck.setWidthHeight(60, 60);
		duck.setX(randX);
		duck.setY(400);
		duck.setVx(randVx);
		duck.setVy(randVy);
		StdAudio.playInBackground("clack.wav");
	}
	
	//resetting for multiple rounds etc
	public void reset() {
		
	}
	
	public void nextRound() {
		wave += 1;
		roundTimer += 30;
		
		duck.setXY(420, 430);
		duck.setVx(2*wave);
		duck.setVy(-2*wave);
		
		dog.setXY(170, 350);
		dog.setVx(0);
		dog.setVy(0);
	}
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		//add 16 to time since paint is called every 20 ms
		time += 20; //time elapse updates
		
		if(time%1000 == 0) {
			roundTimer -= 1;
			if(roundTimer == 0) {
				t.stop(); //stops the timer after this round is over
				nextRound();
			}
		}
		
		//draw the round String
		
		//LAYER your objects as you want them to layer visually !!
		back.paint(g);
		dog.paint(g);
		duck.paint(g);
		grave.paint(g);
		fore.paint(g);
		
		//logic for resetting the duck position
		if(duck.getY() > 500 && dog.getY() == 350) {
			duck.setY(430);
			if(dog.getX() != 0) {
				duck.setX((int)(Math.random()*(691)+10));
				duck.setVx((int)(Math.random()*(5+5+1)-5));
				duck.setVy((int)(Math.random()*(-5+3+1)-3));
			}
			if(wave > 1) {
				duck.setX((int)(Math.random()*(691)+10));
				duck.setVx((int)(Math.random()*(5+5+1)-5)*wave);
				duck.setVy((int)(Math.random()*(-5+3+1)-3)*wave);
				}
			}

		//for cat moving
		if(dog.getY() < 240) {
			dog.setVy(4);
			}
		if(dog.getY() > 350) {
			dog.setXY(170, 350);
			dog.setVy(0);
		}
		//for making skull bounce around
		if(duck.getY() < 0) {
			duck.setVy(duck.getVy()*-1);
			StdAudio.playInBackground("clang.wav");
		}
		if(duck.getX() < 0) {
			duck.setVx(duck.getVx()*-1);
			StdAudio.playInBackground("bonesHurt.wav");

		}
		if(duck.getX() > 720) {
			duck.setVx(duck.getVx()*-1);
			StdAudio.playInBackground("ouch.wav");

		}
		
		//draw time related Strings last so they are overlaid on top of everything else
		
		//text for after a round ends
		if(!t.isRunning()) {
			g.setFont(smFont);
			g.setColor(Color.red);
			g.drawString("Press the space bar for next round!", 290, 200);
			g.setFont(bigFont);
			g.setColor(Color.white);
			g.drawString("Round Complete", 200, 250);
			StdAudio.playInBackground("waveComplete.wav");
		}
		
		g.setFont(medFont);
		g.setColor(Color.white);
		
		// scoring
		g.drawString("Score: "+this.score, 400, 550);
		
		//timer
		if(roundTimer >= 10) {
			g.drawString("Time: "+this.roundTimer, 660, 550);
		}
		else if(roundTimer >= 0 && roundTimer < 10) {
			g.drawString("Time: "+this.roundTimer, 660, 550);
		}
		else {
			duck.setVx(0);
			duck.setXY(420, 430);
		}
		
		//waves
		g.setFont(medFont);
		g.drawString("Round: "+this.wave, 10, 30);
		
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		JFrame f = new JFrame("Duck Hunt");
		f.setSize(new Dimension(800, 600));
		f.setBackground(Color.blue);
		f.add(this);
		f.setResizable(false);
		f.setLayout(new GridLayout(1,2));
		f.addMouseListener(this);
		f.addKeyListener(this);
		
		init(); //call your init method to give properties to the object + variables
		
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	//make the timer visible to the other methods
	Timer t = new Timer(16, this);
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent mouse) {
		// TODO Auto-generated method stub
		//perform a rectangle collision with the mouse and the object
				Rectangle rMouse = new Rectangle(mouse.getX(), mouse.getY(), 25, 25); //guess and check size for now
				
				//2nd rectangle will be for your object (aka your duck)
				Rectangle rDuck = new Rectangle(
											duck.getX()+8, duck.getY()+6,
											duck.getWidth(), duck.getHeight()
						);
				
				//check if they're colliding
				if(rMouse.intersects(rDuck)) { //do the 2 rect intersect?
					if(roundTimer>0) {
						duck.setVy(10);
						duck.setVx(0);
						score += 1;
						dog.setVy(-4);
						StdAudio.playInBackground("meow.wav");
					}
				}
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode() == 32) {
			if(!t.isRunning()) {
				t.start();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
