package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Grid renderer that can be configured without sub-classing.
 * 
 * @author Armin Reichert
 */
public class ConfigurableGridRenderer implements GridRenderer, GridRenderingModel {

	public enum Style {
		WALL_PASSAGE, PEARLS
	};

	private Style style;
	private GridRenderer renderer;

	public IntSupplier fnCellSize;
	public IntSupplier fnPassageWidth;
	public Supplier<Color> fnGridBgColor;
	public BiFunction<Integer, Integer, Color> fnPassageColor;
	public Function<Integer, Color> fnCellBgColor;
	public Function<Integer, String> fnText;
	public IntSupplier fnMinFontSize;
	public Supplier<Font> fnTextFont;
	public Supplier<Color> fnTextColor;

	/**
	 * Creates a renderer with wall-passage style.
	 */
	public ConfigurableGridRenderer() {
		this(Style.WALL_PASSAGE);
	}

	/**
	 * Creates a renderer with the given style.
	 * 
	 * @param style
	 *          the renderer style
	 */
	public ConfigurableGridRenderer(Style style) {
		fnCellSize = () -> 8;
		fnPassageWidth = () -> getCellSize() / 2;
		fnGridBgColor = () -> Color.BLACK;
		fnPassageColor = (cell, dir) -> getCellBgColor(cell);
		fnCellBgColor = cell -> Color.WHITE;
		fnText = cell -> "";
		fnMinFontSize = () -> 6;
		fnTextFont = () -> new Font("Sans", Font.PLAIN, getCellSize() / 2);
		fnTextColor = () -> Color.BLUE;
		setStyle(style);
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
		this.renderer = createRenderer(style);
	}

	private GridRenderer createRenderer(Style style) {
		switch (style) {
		case PEARLS:
			return new PearlsGridRenderer(this);
		case WALL_PASSAGE:
			return new WallPassageGridRenderer(this);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public GridRenderingModel getModel() {
		return this;
	}

	@Override
	public int getCellSize() {
		return fnCellSize.getAsInt();
	}

	@Override
	public int getPassageWidth() {
		return fnPassageWidth.getAsInt();
	}

	@Override
	public Color getGridBgColor() {
		return fnGridBgColor.get();
	}

	@Override
	public Color getPassageColor(int cell, int dir) {
		return fnPassageColor.apply(cell, dir);
	}

	@Override
	public Color getCellBgColor(int cell) {
		return fnCellBgColor.apply(cell);
	}

	@Override
	public String getText(int cell) {
		return fnText.apply(cell);
	}

	@Override
	public int getMinFontSize() {
		return fnMinFontSize.getAsInt();
	}

	@Override
	public Font getTextFont() {
		return fnTextFont.get();
	}

	@Override
	public Color getTextColor() {
		return fnTextColor.get();
	}

	@Override
	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible) {
		renderer.drawPassage(g, grid, passage, visible);
	}

	@Override
	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		renderer.drawCell(g, grid, cell);
	}
}