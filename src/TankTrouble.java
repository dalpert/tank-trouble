import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.Timer;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

@SuppressWarnings("serial")
public class TankTrouble extends GraphicsProgram {
	public static boolean gameState = true;
	public static int p1 = 0;
	public static int p2 = 0;
	
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 600;
	public static final int APPLICATION_WIDTH = WINDOW_WIDTH+50;
	public static final int APPLICATION_HEIGHT = WINDOW_HEIGHT+100;

	public static final int BULLET_RADIUS = 5;
	public static final int BULLET_SPEED = 2;
	public static final int MAX_BULLETS = 5;
	public static final int FIRE_DELAY = 0;

	public static final double TANK_ROTATION_ANGLE = 2;
	public static final double TANK_SPEED = 2;
	public static final double TANK_SCALE = 0.75;

	public static final int LINE_DRAWS = 100;

	public static final char TANK1_FORWARDS =	'e';
	public static final char TANK1_BACKWARDS =	'd';
	public static final char TANK1_LEFT =		's';
	public static final char TANK1_RIGHT =		'f';
	public static final char TANK1_SHOOT =		'q';
	
	public static final char TANK2_FORWARDS =	'i';
	public static final char TANK2_BACKWARDS =	'k';
	public static final char TANK2_LEFT =		'j';
	public static final char TANK2_RIGHT =		'l';
	public static final char TANK2_SHOOT =		'y';
	private static final int MAX_DISTANCE = 1000;

	public static ArrayList<Wall> objectList = new ArrayList<Wall>();

	//Add tanks
	public static Tank tank1 = new Tank(1,0,TANK_SPEED,30, 60);
	public static Tank tank2 = new Tank(2,0,TANK_SPEED,500, 500);
	
	public static GLabel p1Score = new GLabel("Daniel: "+p1,20,630);
	public static GLabel p2Score = new GLabel("Luca: "+p2,520, 630);
	

	GLine lineToTarget = new GLine(0,0,0,0);

	public static void main(String[] args) {
		new TankTrouble().start(args);
	} 

	@Override
	public void init() {
		p1Score.setFont(new Font("Serif", Font.BOLD, 22));
		add(p1Score);
		p2Score.setFont(new Font("Serif", Font.BOLD, 22));
		add(p2Score);
		setTitle("TankTrouble by Daniel & Luca");
		addKeyListeners();
		addMouseListeners();

		add(tank1);
		add(tank2);

		//Add lines around borders
		add(new GLine(0,WINDOW_HEIGHT,WINDOW_HEIGHT,WINDOW_HEIGHT));
		add(new GLine(WINDOW_HEIGHT,0,WINDOW_HEIGHT,WINDOW_HEIGHT));
		add(new GLine(0,0,WINDOW_HEIGHT,0));
		add(new GLine(0,0,0,WINDOW_HEIGHT));

		//Add walls around border
		Wall left = new Wall(0,0,0,WINDOW_WIDTH);
		Wall right = new Wall(WINDOW_WIDTH,0,WINDOW_WIDTH,WINDOW_WIDTH);
		Wall top = new Wall(0,0,WINDOW_WIDTH,0);
		Wall bottom = new Wall(0,WINDOW_WIDTH,WINDOW_WIDTH,WINDOW_WIDTH);
		objectList.add(left);
		objectList.add(right);
		objectList.add(top);
		objectList.add(bottom);

		//Add other walls
		Wall wall = new Wall(0,WINDOW_WIDTH/2,WINDOW_WIDTH/2,WINDOW_WIDTH/2);
		Wall wall2 = new Wall(300, 400, 600, 400);
		Wall wall3 = new Wall(300,395,300,500);
		Wall wall4 = new Wall(100,0,100,200);
		Wall wall5 = new Wall(200,100,200,WINDOW_HEIGHT/2);
		Wall wall6 = new Wall(150, WINDOW_HEIGHT/2, 150,450);
		Wall wall7 = new Wall(200, 150, 500,150);
		Wall wall8 = new Wall(400, 280, WINDOW_WIDTH,280);
		objectList.add(wall);
		objectList.add(wall2);
		objectList.add(wall3);
		objectList.add(wall4);
		objectList.add(wall5);
		objectList.add(wall6);
		objectList.add(wall7);
		objectList.add(wall8);
		
		addObjectList();
		
		JButton play = new JButton("PLAY");
		add(play, SOUTH);
		play.addActionListener(this);

		
		waitForClick();
		
		//tank1.explode();
		

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("PLAY")) {
			tank1.bulletArray.clear();
			tank2.bulletArray.clear();
			
			//remove(tank1.logo);
			//remove(tank2.logo);
			gameState=true;
			
		}
	}

	@Override
	public void run() {
		while (gameState==true) {		
			tank1.advanceBullets();
			tank1.checkCollision(WINDOW_WIDTH,WINDOW_HEIGHT);
			tankCollisions(tank1); //collisions with objects
			checkKeyPresses(tank1);

			tank2.advanceBullets();
			tank2.checkCollision(WINDOW_WIDTH,WINDOW_HEIGHT);
			tankCollisions(tank2); //collisions with objects
			checkKeyPresses(tank2);

//			compTank.advanceBullets();
//			//drawLineToTarget();
//
//			checkAITankMovement();
			pause(10);
		}
	}

//	public void checkAITankMovement() {
//		//System.out.println(compTank.getTankState());
//		checkKeyPresses(compTank);	//physically move the tank based on which "keypresses" are set
//		if (compTank.angleToTarget==-1) compTank.findTargetAngle(); //if the tank has no target, find the angle to the target
//		
//		if (compTank.getTankState()=="ready") {  //if the tank is finished with its last move:
//			if (directLine(compTank.getX(), compTank.getY(), tank1.getX(), tank1.getY()) ) { //&& (compTank.shootKeyPressed==true)
//				compTank.setRotationToTarget();
//			} else {
//				System.out.println("waiting");
//				compTank.angleToTarget=findOptimalAngle(compTank.getX(), compTank.getY());
//				System.out.println("done");
//				compTank.setRotationToTarget();
//				//double optAngle = findOptimalAngle(compTank.getX(), compTank.getY());
//				//System.out.println("optimal angle is "+optAngle);
//			}
//		} else compTank.setRotationToTarget();
//	}

	//Draws a line from the AI tank to its target
//	public void drawLineToTarget() {
//		add(lineToTarget);
//		remove(lineToTarget);
//		lineToTarget = new GLine (tank1.getX(),tank1.getY(), compTank.getX(), compTank.getY());
//		add(lineToTarget);
//	}

	//Moves one step back on the line so the new collision point isn't IN the wall
	public GPoint moveBackOneStep(double X, double Y, double angle) {
		X = X-Math.sin(Math.toRadians(angle));
		Y = Y+Math.cos(Math.toRadians(angle));
		return new GPoint(X,Y);
	}

	//Finds the next point where a bullet will ricochet
	public GPoint nextWallCollision(double startX, double startY, double angle) {
		double currentX = startX;
		double currentY = startY;
		while ((isWall(currentX,currentY)==false) && (hitsTargetTank(currentX,currentY)==false)) {
			currentX+=Math.sin(Math.toRadians(angle));
			currentY-=Math.cos(Math.toRadians(angle));
		}
		return (new GPoint(currentX, currentY));
	}

	//Checks whether the AI tank can draw a direct line to its target
//	public static boolean directLine(double sourceX, double sourceY, double targetX, double targetY) {
//		double currentX = sourceX;
//		double currentY = sourceY;
//		
//		compTank.findTargetAngle();
//
//		while (isWall(currentX,currentY)==false) {
//			//			GLine line = new GLine(sourceX, sourceY, currentX, currentY);
//			//			line.setColor(Color.blue);
//			//			add(line);
//			currentX+=Math.sin(Math.toRadians(compTank.angleToTarget));
//			currentY-=Math.cos(Math.toRadians(compTank.angleToTarget));
//			if (hitsTargetTank(currentX, currentY)) {
//				return true;
//			}
//		}
//		return false;
//	}

	//Checks whether a given point is in a wall or not
	public static boolean isWall(double X, double Y) {
		for (Wall n:objectList) {
			if (n.contains(X, Y)) {
				return true;
			}
			if (X > WINDOW_WIDTH || X < 0 || Y > WINDOW_HEIGHT || Y < 0) {
				return true;
			}
		}
		return false;
	}

	public double findOptimalAngle(double sourceX, double sourceY) {
		ArrayList<Double> distancesForAllAngles = new ArrayList<Double>();

		//preloads array with null elements
		for (int i = 0; i < 360; i++) {
			distancesForAllAngles.add(i, null);
		}

		//runs through all angles from 0 to 359
		for (int x = 0; x < 360; x++) {
			//System.out.println(x);
			//waitForClick();
			double angle = x; //angle is the iteration of the for loop
			double currentX = sourceX; //current x position starts as the AI tank
			double currentY = sourceY; //current y position starts as the AI tank
			double newAngle = 0; //keeps track of newly calculated angle
			double prevAngle = 0; //keeps track of the last angle
			GPoint collisionPoint = nextWallCollision(sourceX, sourceY, angle); //the point where the line intersects a wall
			ArrayList<GLine> bulletPath = new ArrayList<GLine>(); //an array of all of the GLines (bounces) 
			double cumulativeDistance=0; //total distance of the path the ball will travel for that angle

			while ((hitsTargetTank(currentX,currentY)==false) && (cumulativeDistance<MAX_DISTANCE)) {	//keeps going until the line intersects the enemy tank or the distance exceeds some maximum
				pause(20);
				collisionPoint = nextWallCollision(currentX, currentY, angle); //Finds the next point where a bullet will ricochet
				GOval oval = new GOval(collisionPoint.getX(), collisionPoint.getY() ,1,1); //draws a red GOval and adds it where there is a collision
				oval.setColor(Color.red);
				add(oval);
				bulletPath.add(new GLine(currentX, currentY, collisionPoint.getX(), collisionPoint.getY()));
				cumulativeDistance+= Math.sqrt(Math.pow(currentX-collisionPoint.getX(), 2)+Math.pow(currentY-collisionPoint.getY(), 2));
				add(new GLine(currentX, currentY, collisionPoint.getX(), collisionPoint.getY()));
				if (hitsTargetTank(collisionPoint.getX(), collisionPoint.getY())==false) {
					//for horizontal
					if ((getWallAt(collisionPoint).getDirection().equals("horizontal")) && collisionPoint.getX() >= getWallAt(collisionPoint).getLeft() && collisionPoint.getX() <= getWallAt(collisionPoint).getRight()) {
						prevAngle = angle;
						newAngle = 180-angle;

						angle = newAngle;
						if (angle < 0) {
							angle+=360;
						}
					}
					//for vertical
					/*CHECK THIS LINE*/if ((getWallAt(collisionPoint).getDirection().equals("vertical")) && collisionPoint.getY() >= getWallAt(collisionPoint).getTop() && collisionPoint.getY() <= getWallAt(collisionPoint).getBottom()) {
						if ((angle>=0)&&(angle<=90)) {
							prevAngle = angle;
							angle = 360-angle;
						} else if ((angle>90)&&(angle<180)) {
							prevAngle = angle;
							angle = 360-angle;
						} else if ((angle>=180)&&(angle<=270)) {
							prevAngle = angle;
							angle = 360-angle;
						} else if ((angle>270)&&(angle<360)) {                                              
							prevAngle = angle;
							angle = 360-angle;
						}
							//angle = newAngle;//new line
						if (angle < 0) {
							angle+=360;
						}
					}
					collisionPoint = moveBackOneStep(collisionPoint.getX(), collisionPoint.getY(), prevAngle);
					currentX = collisionPoint.getX();
					currentY = collisionPoint.getY();
				}
				distancesForAllAngles.set(x, cumulativeDistance);
			}
		}
		//System.out.println(distancesForAllAngles);
		double smallest = distancesForAllAngles.get(0);
		int optimalAngle = 0;
		for (int i = 0; i < distancesForAllAngles.size(); i++) {
			if (distancesForAllAngles.get(i)<smallest) {
				smallest = distancesForAllAngles.get(i);
				optimalAngle = i;
			}
		}
		System.out.println("optimalAngle="+optimalAngle);
		return optimalAngle;
	}

	//Tells whether or not a point is in the target (player) tank
	public static boolean hitsTargetTank(double X, double Y) {
		if(tank1.contains(X, Y)) {
			return true;
		}
		return false;
	}

	//Gets the wall at a given point
	public Wall getWallAt(GPoint point) {
		for (Wall n:objectList) {
			if (n.contains(point)) {
				return n;
			}
		}
		return null;
	}


	public void checkKeyPresses(Tank tank) {
		if (tank.getForwardsKeyPressed()==true) {
			tank.move("Forwards");
		} else if (tank.getBackwardsKeyPressed()==true) {
			tank.move("Backwards");
		}

		if (tank.getLeftKeyPressed()==true) {
			if (tank.type=="player") tank.rotate(-TANK_ROTATION_ANGLE);
			else if (tank.type=="computer") tank.rotate(-0.5);
		} else if (tank.getRightKeyPressed()==true) {
			if (tank.type=="player") tank.rotate(TANK_ROTATION_ANGLE);
			else if (tank.type=="computer") tank.rotate(0.5);
		}

		if ((tank.getShootKeyPressed()==true)&&(tank.isReadyToFire()==true)) {
			tank.shoot();
			add(tank.bulletArray.get(tank.bulletArray.size()-1));
			tank.setReadyToFire(false);
			tank.setShootKeyPressed(false); //uncomment for one at a time shooting
			tank.startFireTimer(FIRE_DELAY);
			if (tank.type=="computer") {
				tank.setShootKeyPressed(false);
				//tank.tankState ="ready";
			}
			}
		}

	//Adds the walls in objectList to the screen
	public void addObjectList() {
		for (int count=0;count<objectList.size();count++) {
			add(objectList.get(count));
		}
	}

	public void tankCollisions(Tank tank) {
		for (int count=0;count<objectList.size();count++) {
			tank.checkCollision(objectList.get(count).getLeft(), objectList.get(count).getRight(), objectList.get(count).getTop(), objectList.get(count).getBottom());
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case TANK1_FORWARDS: tank1.setForwardsKeyPressed(true); break;
		case TANK1_BACKWARDS: tank1.setBackwardsKeyPressed(true); break;
		case TANK1_LEFT: tank1.setLeftKeyPressed(true); break;
		case TANK1_RIGHT: tank1.setRightKeyPressed(true); break;
		case TANK1_SHOOT: tank1.setShootKeyPressed(true); break;
		
		case TANK2_FORWARDS: tank2.setForwardsKeyPressed(true); break;
		case TANK2_BACKWARDS: tank2.setBackwardsKeyPressed(true); break;
		case TANK2_LEFT: tank2.setLeftKeyPressed(true); break;
		case TANK2_RIGHT: tank2.setRightKeyPressed(true); break;
		case TANK2_SHOOT: tank2.setShootKeyPressed(true); break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {
		case TANK1_FORWARDS: tank1.setForwardsKeyPressed(false); break;
		case TANK1_BACKWARDS: tank1.setBackwardsKeyPressed(false); break;
		case TANK1_LEFT: tank1.setLeftKeyPressed(false); break;
		case TANK1_RIGHT: tank1.setRightKeyPressed(false); break;
		case TANK1_SHOOT: tank1.setShootKeyPressed(false); break;
		
		case TANK2_FORWARDS: tank2.setForwardsKeyPressed(false); break;
		case TANK2_BACKWARDS: tank2.setBackwardsKeyPressed(false); break;
		case TANK2_LEFT: tank2.setLeftKeyPressed(false); break;
		case TANK2_RIGHT: tank2.setRightKeyPressed(false); break;
		case TANK2_SHOOT: tank2.setShootKeyPressed(false); break;
		}
	}

//		public void mousePressed(MouseEvent e) {
////			System.out.println(e.getX());
////			System.out.println(e.getY());
//			tank1.setLocation(e.getX(), e.getY());
//		}

	//	public void mousePressed(MouseEvent e) {
	//		int dX=0;
	//		int dY=0;
	//		Wall wall = new Wall(e.getX()-50,e.getY(),e.getX()+50,e.getY());
	//		System.out.println(e.getX());
	//		objectList.add(wall);
	//		add(wall);
	//		System.out.println(wall.getX());
	//		//System.out.println("adding wall");
	//		//System.out.println("left="+objectList.get(objectList.size()-1).getLeft());
	//
	//	}
}
