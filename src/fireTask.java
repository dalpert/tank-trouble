import java.util.TimerTask;


class fireTask extends TimerTask {
	Tank tank;
	public fireTask(Tank tank) {
		super();
		this.tank = tank;
	}
	public void run() {
		tank.setReadyToFire(true);
		tank.fireTimer.cancel(); //Terminate the timer thread
	}
}