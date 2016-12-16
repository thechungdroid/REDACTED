package redacted;


import javafx.scene.paint.Color;

public class HealthBar extends StatusBar {

	public HealthBar() {
		super();
		setFillColor(Color.RED);
	}

	public HealthBar(int maxValue) {
		super(maxValue);
		setFillColor(Color.RED);
	}
}
