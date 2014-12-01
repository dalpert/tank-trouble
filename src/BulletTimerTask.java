import java.awt.Color;
import java.util.TimerTask;


class BulletTimerTask extends TimerTask {
	Bullet bullet;
	Tank tank;
	public BulletTimerTask(Tank tank, Bullet bullet) {
		super();
		this.bullet = bullet;
		this.tank = tank;
	}
	public void run() {
		//TankTrouble.remove(tank.bulletArray.get(tank.bulletArray.indexOf(bullet)));
		if (tank.bulletArray.indexOf(bullet)>=0){
			tank.bulletArray.get(tank.bulletArray.indexOf(bullet)).setVisible(false);
			tank.bulletArray.remove(bullet);
		}
		bullet.bulletTimer.cancel(); //Terminate the timer thread
		//tank.decreaseNumberOfBullets();
	}
}