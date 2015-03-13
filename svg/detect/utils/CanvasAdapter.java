package svg.detect.utils;

import ivangro.freespace.ICanvas;
import svg.core.SVGConfig;
import svg.core.SVGRepository;

/**
 * Class to adapt the SVGRepository to a canvas.
 * This class is employed to detect the biggest empty space inside the repository.
 * @author Ivan Guerrero
 */
public class CanvasAdapter implements ICanvas {
    private SVGRepository repository;
    private boolean[][] visited;
    private int height, width;
    
    public CanvasAdapter(SVGRepository repository) {
        this.repository = repository;
        this.height = SVGConfig.CANVAS_HEIGHT;
        this.width = SVGConfig.CANVAS_WIDTH;
        visited = new boolean[width][height];
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean isEmpty(int x, int y) {
        return repository.getElementAt(x, y) == null;
    }

    @Override
    public void setVisited(int x, int y) {
        visited[x][y] = true;
    }

    @Override
    public boolean isVisited(int x, int y) {
        return visited[x][y];
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return repository.getElementAt(x, y) != null;
    }
}