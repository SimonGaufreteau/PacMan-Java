package Controller;

import javafx.concurrent.Task;

import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;

public class BindingController {
	public enum direction {TOP,RIGHT,BOTTOM,LEFT};
	public static void bindText(Text text, Task task, BorderPane pane,direction direction){
		text.setVisible(true);
		text.textProperty().bind(task.messageProperty());
		switch (direction){
			case TOP:
				pane.setTop(text);
				break;
			case RIGHT:
				pane.setRight(text);
				break;
			case BOTTOM:
				pane.setBottom(text);
				break;
			case LEFT:
				pane.setLeft(text);
				break;
		}
	}
}
