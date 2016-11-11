package de.amr.demos.maze.swingapp.app;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Dimension;
import java.awt.EventQueue;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.view.MazeWindow;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.rendering.swing.SwingGridCanvas;
import de.amr.easy.maze.misc.MazeUtils;

/**
 * This application visualizes different maze generation algorithms and path finders. The grid size
 * and display style can be changed interactively.
 * 
 * @author Armin Reichert
 */
public class MazeDemoApp {

	public static void main(String... args) {
		MazeUtils.setLAF("Nimbus");
		EventQueue.invokeLater(MazeDemoApp::new);
	}

	public final MazeDemoModel model;

	final SettingsWindow settingsWindow;
	final MazeWindow mazeWindow;

	private Thread taskThread;
	private volatile boolean taskStopped;

	public MazeDemoApp() {
		model = new MazeDemoModel();
		model.setGridCellSizes(128, 64, 32, 16, 8, 4, 2);
		model.setGridCellSize(32);
		model.setPassageThicknessPct(25);
		model.setGenerationStart(CENTER);
		model.setPathFinderStart(TOP_LEFT);
		model.setPathFinderTarget(BOTTOM_RIGHT);
		model.setGenerationAnimated(true);
		model.setHidingControlsWhenRunning(false);
		model.setLongestPathHighlighted(false);
		model.setDelay(0);
		Dimension size = MazeUtils.maxGridDimensionForDisplay(model.getGridCellSize());
		model.setGrid(new ObservableGrid<>(size.width, size.height, UNVISITED));

		settingsWindow = new SettingsWindow(this);
		settingsWindow.setAlwaysOnTop(true);
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setVisible(true);

		mazeWindow = new MazeWindow(this);
		mazeWindow.setVisible(true);
	}

	public SwingGridCanvas canvas() {
		return mazeWindow.getCanvas();
	}

	public ObservableGrid<TraversalState, Integer> grid() {
		return model.getGrid();
	}

	public void showMessage(String msg) {
		settingsWindow.getControlPanel().showMessage(msg + "\n");
	}

	public void showSettingsWindow() {
		settingsWindow.setVisible(true);
		settingsWindow.requestFocus();
	}

	public void updateCanvas() {
		mazeWindow.invalidateCanvas();
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageThicknessPct(percent);
		canvas().clear();
		canvas().invalidateRenderer();
		canvas().render();
	}

	public void setDelay(int delay) {
		model.setDelay(delay);
		canvas().setDelay(delay);
	}

	void startTask(Runnable task) {
		taskStopped = false;
		taskThread = new Thread(task);
		taskThread.start();
	}

	void stopTask() {
		taskStopped = true;
		try {
			taskThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	boolean isTaskRunning() {
		return taskThread != null && taskThread.isAlive();
	}

	boolean isTaskStopped() {
		return taskStopped;
	}
}