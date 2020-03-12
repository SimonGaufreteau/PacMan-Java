package Controller;

import javafx.concurrent.Task;

import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;

public class BindingController {

	public static void bindText(Text text, Task task, BorderPane pane){
		text.visibleProperty().bind(task.messageProperty().isEqualTo("finished"));
		text.textProperty().bind(task.messageProperty());
		pane.setTop(text);
	}
}
