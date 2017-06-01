package com.prestu.gambler.games;

public class GameDimension {

    private int width;
    private int height;
    private Tile tile;

    private class Tile {

        private int size;

        Tile(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public GameDimension(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        tile = new Tile(tileSize);
    }

    public GameDimension(int width, int height) {
        this.width = width;
        this.height = height;
        tile = new Tile(1);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getTileSize() {
        return tile.getSize();
    }

    public void setTileSize(int tileSize) {
        tile.setSize(tileSize);
    }
}
