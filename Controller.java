package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String discColor1 = "#24303E";
	private static final String discColor2 = "#4CAA88";


	private static String PLAYER_ONE = "Player One";
	private static String PLAYER_TWO = "Player Two";

	private boolean isPlayerOneTurn = true;

	private Disc[] [] insertedDiscsArray = new Disc[ROWS] [COLUMNS]; //FOR STRUCTUREAL CHANGES FOR DEVELOPERS


	@FXML
	public GridPane rootGridPane;
	@FXML
	public Pane insertedDiscsPane;
	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField playerOneTextField, playerTwoTextField;
	@FXML
	public Button setNamesButton;

private boolean isAllowedToInsert = true;
	public void createPlayground() {
		Platform.runLater(() -> setNamesButton.requestFocus());

		Shape rectangleWithHoles = createGameStructuralGrid();
		rootGridPane.add(rectangleWithHoles, 0,1);

		List<Rectangle> rectangleList = createClickableColumns();

		for (Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle,0,1);
		}
		setNamesButton.setOnAction(event -> {
			PLAYER_ONE = playerOneTextField.getText();
			PLAYER_TWO = playerTwoTextField.getText();
		});
	}

	private Shape createGameStructuralGrid() {


		Shape rectangleWithHoles = 	new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++ ) {
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2);
				circle.setCenterY(CIRCLE_DIAMETER / 2);
				circle.setSmooth(true);

				circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
				circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4);
				rectangleWithHoles = Shape.subtract( rectangleWithHoles, circle);

			}
		}


		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;




	}

	private List<Rectangle> createClickableColumns() {
		List<Rectangle> rectangleList = new ArrayList<>();


		for (int col = 0; col < COLUMNS; col++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) +  CIRCLE_DIAMETER / 4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			final int column = col;
			rectangle.setOnMouseClicked(event -> {

				if (isAllowedToInsert) {
					isAllowedToInsert = false;

					insertDisc(new Disc(isPlayerOneTurn), column);
				}
			});
			rectangleList.add(rectangle);


		}
		return rectangleList;
	}
	private void insertDisc(Disc disc, int column) {

		int row = ROWS-1;
		while (row >= 0) {
			if (getDiscIfPresent(row, column) == null)
				break;
			row--;

		}
		if (row < 0) // if row is full we cannot insert anymore disc
			return;
		insertedDiscsArray[row] [column] = disc;// for structural Changes : for developers
		insertedDiscsPane.getChildren().add(disc);
		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
		int currentRow = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
		translateTransition.setOnFinished(event -> {

			isAllowedToInsert = true;
			if (gameEnded(currentRow, column)) {

				gameOver();
				return;



			}
			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO);
		});
		translateTransition.play();


	}

	private boolean gameEnded(int row , int column) {



		List<Point2D> verticalPoints = IntStream.rangeClosed(row-3, row +3)   // range of rows values = 0,1,2,3,4,5
				.mapToObj(r -> new Point2D(r, column))  //0,3  1,3  2,3  3,3  4,3  5,3--> point2D
				.collect(Collectors.toList());

		List<Point2D> horizontalPoints = IntStream.rangeClosed(column-3 ,column +3)     // range of rows values = 0,1,2,3,4,5
				.mapToObj(col -> new Point2D(row, col))
				.collect(Collectors.toList());


		Point2D startPoint1 = new Point2D(row-3, column+3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
				.mapToObj(i -> startPoint1.add(i, -i))
				.collect(Collectors.toList());

		Point2D startPoint2 = new Point2D(row-3, column-3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
				.mapToObj(i -> startPoint2.add(i, i))
				.collect(Collectors.toList());




		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1Points)  || checkCombinations(diagonal2Points);

		return isEnded;





	}

	private boolean checkCombinations(List<Point2D> points) {
		int chain = 0;
		for (Point2D point : points) {
			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();


			Disc disc = getDiscIfPresent(rowIndexForArray, columnIndexForArray);

			if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn)
				chain++;
			if(chain==4) {
				return true;
			} else{
			chain=0;
			}





		}
		return false;
	}


		private Disc getDiscIfPresent ( int row, int column){

			if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
				return null;

			return insertedDiscsArray[row][column];
		}


		private void gameOver () {
			String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
			System.out.println("winner is:" + winner);

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Connect four");
			alert.setHeaderText("The Winner is :" + winner);
			alert.setContentText("want to play again?");

			ButtonType yesBtn = new ButtonType("Yes");
			ButtonType noBtn = new ButtonType("No,Exit");
			alert.getButtonTypes().setAll(yesBtn, noBtn);


			Platform.runLater(() -> {
				Optional<ButtonType> btnClicked = alert.showAndWait();
				if (btnClicked.isPresent() && btnClicked.get() == yesBtn) {
					//....user has chosen yes button so RESET the game
					resetGame();
				} else {
					//...user has chosen no button so exit the game
					Platform.exit();
					System.exit(0);
				}


			});

		}

		public void resetGame () {
			insertedDiscsPane.getChildren().clear();

			for (int row = 0; row < insertedDiscsArray.length; row++) {

				for (int col = 0; col < insertedDiscsArray[row].length; col++) {

					insertedDiscsArray[row][col] = null;
				}
			}

			isPlayerOneTurn = true;
			playerNameLabel.setText(PLAYER_ONE);

			createPlayground();

		}

		@Override
		public void initialize (URL location, ResourceBundle resources){

		}

		private static class Disc extends Circle {
			private final boolean isPlayerOneMove;

			public Disc(boolean isPlayerOneMove) {
				this.isPlayerOneMove = isPlayerOneMove;
				setRadius(CIRCLE_DIAMETER / 2);
				setFill(isPlayerOneMove ? Color.valueOf(discColor1) : Color.valueOf(discColor2));
				setCenterX(CIRCLE_DIAMETER / 2);
				setCenterY(CIRCLE_DIAMETER / 2);
			}
		}


	}


	/*this is to say my code is exactly same as instructor.
 Than also i don't know why i am not getting the output.
 i have mailed my query but did not get reply.
i have done the assignment task properly.
also my file and help is stop  working after i created on jar file of this project.
i am also unable to import event.Actionevent , event.eventHandelar and scene.parant after making one jar file.
i request your forum to please understand queries properly.
Thankyou */
