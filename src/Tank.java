import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GOval;
import acm.graphics.GPolygon;

public class Tank extends GCompound {
	public GImage logo;
	public String type = "player";
	private int player;
	public double angle = 0;
	private double speed = 0;
	public boolean forwardsKeyPressed = false;
	public boolean backwardsKeyPressed = false;
	public boolean leftKeyPressed = false;
	public boolean rightKeyPressed = false;
	public boolean shootKeyPressed = false;
	protected int turretLength = 70;
	public static final int WALL_DISTANCE = 1;

	public GPolygon body;
	public GOval hatch;
	public GPolygon turret;

	private boolean colliding = false;
	private boolean readyToFire = true;

	public ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	public Timer fireTimer;
	public String tankState = "ready";



	public Tank(int player, double angle, double speed, double X, double Y) {
		super();

		Color tankColor = null;

		this.player = player;
		this.angle = angle;
		this.speed = speed;
		this.setLocation(X,Y);

		switch (player) {
		case 1: tankColor = Color.red; break;
		case 2: tankColor = Color.green; break;
		case 3: tankColor = Color.yellow; break;
		case 4: tankColor = Color.blue; break;
		default: tankColor = Color.gray; break;
		}

		body = new GPolygon();
		body.addVertex(0, -30);
		body.addVertex(0, 50);
		body.addVertex(50, 50);
		body.addVertex(50, -30);
		body.setFilled(true);
		body.setFillColor(Color.black);

		hatch = new GOval(-20,-20,40,40);
		hatch.setFilled(true);
		hatch.setColor(tankColor);
		hatch.setFillColor(tankColor);

		turret = new GPolygon();
		turret.addVertex(20, 0);
		turret.addVertex(30, 0);
		turret.addVertex(30, turretLength);
		turret.addVertex(20, turretLength);
		turret.setFilled(true);
		turret.setColor(tankColor);
		turret.setFillColor(tankColor);


		body.recenter();
		turret.recenter();

		add(body);
		add(hatch);
		add(turret);

		rotate(this.angle);
		scale(TankTrouble.TANK_SCALE);
		turretLength=(int) Math.round(turretLength*TankTrouble.TANK_SCALE);
		speed=(int) Math.round(speed*TankTrouble.TANK_SCALE);
		this.sendToFront();
	}

//	public boolean checkHits(double X, double Y) {
//		if (body.contains(X, Y)) {
//			return true;
//		}
//		return false;
//	}
	
	public void rotate(double angle) {
		this.angle+=angle;
		this.angle%=360;
		if (this.angle<0) { 
			this.angle=360+this.angle;
		}
		angle=-angle;

		turret.setLocation((turretLength/2)*Math.sin(Math.toRadians(this.angle)), -(turretLength/2)*Math.cos(Math.toRadians(this.angle)));

		body.rotate(angle);
		turret.rotate(angle);
	}

	public void move(String direction) {
		if (colliding==false) {
			double dX = 0;
			double dY = 0;

			if (direction.equals("Forwards")){
				dX = Math.sin(Math.toRadians(angle)) * speed;
				dY = -Math.cos(Math.toRadians(angle)) * speed;
			} else if (direction.equals("Backwards")){
				dX = -Math.sin(Math.toRadians(angle)) * speed;
				dY = Math.cos(Math.toRadians(angle)) * speed;			
			}

			this.setLocation(this.getX()+dX, this.getY()+dY);
		}
	}

	public void checkCollision(double left, double right, double top, double bottom) {
		double tankLeft = (getX()-(getWidth()/2));
		double tankRight = (getX()+(getWidth()/2));
		double tankTop = (getY()-(getHeight()/2));
		double tankBottom = (getY()+(getHeight()/2));
		double tankWidth = getWidth();
		double tankHeight = getHeight();
		double closeness = 0.5;



		if ((tankBottom>top)&&(tankBottom<bottom)&&(tankLeft+tankWidth*closeness>left)&&(tankRight-tankWidth*closeness<right)) {
			colliding=true;
			//System.out.println("topSide");
			setLocation(getX(), top-(getHeight()/2));
		} else if ((tankTop>top)&&(tankTop<bottom)&&(tankLeft+tankWidth*closeness>left)&&(tankRight-tankWidth*closeness<right)) {
			colliding=true;
			//System.out.println("bottomSide");
			setLocation(getX(), bottom+(getHeight()/2));
		} else if ((tankRight>left)&&(tankRight<right)&&(tankTop+tankHeight*closeness>top)&&(tankBottom-tankHeight*closeness<bottom)) {
			colliding=true;
			//System.out.println("leftSide");
			setLocation(left-(getWidth()/2), getY());
		} else if ((tankLeft>left)&&(tankLeft<right)&&(tankTop+tankHeight*closeness>top)&&(tankBottom-tankHeight*closeness<bottom)) {
			colliding=true;
			//System.out.println("rightSide");
			setLocation(right+(getWidth()/2), getY());
		}	
		else {
			colliding=false;
		}
	}

	public void checkCollision(int width, int height) {
		if ((this.getX()-(this.getWidth()/2))<0) {
			colliding=true;
			setLocation(this.getWidth()/2+WALL_DISTANCE,this.getY());
		} else if ((this.getX()+(this.getWidth()/2))>width) {
			colliding=true;
			setLocation(width-this.getWidth()/2-WALL_DISTANCE,this.getY());
		} else if ((this.getY()-(this.getHeight()/2))<0) {
			colliding=true;
			setLocation(this.getX(),this.getHeight()/2+WALL_DISTANCE);
		} else if ((this.getY()+(this.getHeight()/2))>height) {
			colliding=true;
			setLocation(this.getX(),height-this.getHeight()/2-WALL_DISTANCE);
		} else {
			colliding=false;
		}

	}

	public void shoot() {
		if (getShootKeyPressed()==true) {
			setShootKeyPressed(false);
		}
		if (bulletArray.size()<=TankTrouble.MAX_BULLETS-1){
			bulletArray.add(new Bullet(this.getBulletX(), this.getBulletY(), TankTrouble.BULLET_RADIUS, this.getAngle(), TankTrouble.BULLET_SPEED));
		}
		//System.out.println(bulletArray.toString());
		//System.out.println(numberOfBullets);

	}

	public void advanceBullets() {
		for (int count=0;count<bulletArray.size();count++) {
			bulletArray.get(count).advanceOneStep(this);
		}
	}

	public void startFireTimer(int delay) {
		fireTimer = new Timer();
		fireTimer.schedule(new fireTask(this), delay);
	}

	public int getPlayer() {
		return player;		
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public double getAngle() {
		return angle;		
	}
	public void setAngle(double angle) {
		this.angle = angle%360;
		rotate(this.angle);
	}
	public double getBulletX(){
		return ((turretLength-10)*Math.sin(Math.toRadians(this.angle)))+this.getX()-(TankTrouble.BULLET_RADIUS/2);
	}
	public double getBulletY() {
		return (-(turretLength-10)*Math.cos(Math.toRadians(this.angle)))+this.getY()-(TankTrouble.BULLET_RADIUS/2);
	}

	public boolean getForwardsKeyPressed() {
		return forwardsKeyPressed;
	}

	public void setForwardsKeyPressed(boolean forwardsKeyPressed) {
		this.forwardsKeyPressed = forwardsKeyPressed;
	}
	public boolean getBackwardsKeyPressed() {
		return backwardsKeyPressed;
	}
	public void setBackwardsKeyPressed(boolean backwardsKeyPressed) {
		this.backwardsKeyPressed = backwardsKeyPressed;
	}
	public boolean getLeftKeyPressed() {
		return leftKeyPressed;
	}
	public void setLeftKeyPressed(boolean leftKeyPressed) {
		this.leftKeyPressed = leftKeyPressed;
	}
	public boolean getRightKeyPressed() {
		return rightKeyPressed;
	}
	public void setRightKeyPressed(boolean rightKeyPressed) {
		this.rightKeyPressed = rightKeyPressed;
	}
	public void setColliding(boolean colliding) {
		this.colliding=colliding;
	}

	public void setShootKeyPressed(boolean shootKeyPressed) {
		this.shootKeyPressed = shootKeyPressed;	
	}
	public boolean getShootKeyPressed() {
		return shootKeyPressed;
	}

	public boolean isReadyToFire() {
		return readyToFire;
	}

	public void setReadyToFire(boolean readyToFire) {
		this.readyToFire = readyToFire;
	}
	
	public void explode() {
		//this.setVisible(false);
		logo = new GImage("fire.jpeg");
		logo.setSize(110, 110);
		double x = (getWidth() - logo.getWidth()) / 2;
		double y = (getHeight() - logo.getHeight()) / 2;
		add(logo, x-45, y-45);
		TankTrouble.gameState=false;
		
		
		//TankTrouble.p1Score.setLabel(arg0);
	}
}
