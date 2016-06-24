package com.example.admin.npuzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {
    private int NUM_TILES=3;


    public void setNUM_TILES(int NUM_TILES) {
        this.NUM_TILES = NUM_TILES;
    }

    public int getNUM_TILES() {
        return NUM_TILES;
    }

    public int steps;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    public PuzzleBoard previousboard;
    PuzzleTile finaltile = null;
    PuzzleBoard(Bitmap bitmap, int parentWidth,int NUM_TILES) {
        this.NUM_TILES=NUM_TILES;
        int w = parentWidth;
        int th = w/NUM_TILES;
        int six = (2*w)/NUM_TILES;
        tiles = new ArrayList<>();

        //imageView.setImageBitmap(imgs[3]);
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,w/NUM_TILES, w/NUM_TILES, true);
        int count=0;
        for(int i=0;i<NUM_TILES;i++){
            for(int j=0;j<NUM_TILES;j++) {
                int ypt =  i*th;
                int xpt = j*th;
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap,xpt,ypt, th, th);
                //Log.d("oncreate", String.valueOf(count));
                if(i==NUM_TILES-1 && j==NUM_TILES-1){
                    finaltile = new PuzzleTile(bitmap1,count);
                    tiles.add(null);
                }
                else{
                    tiles.add(count,new PuzzleTile(bitmap1,count));
                    count++;
                }
            }
        }

      /* imgs[0] = Bitmap.createBitmap(bitmap, 0, 0, th,th);

        imgs[1] = Bitmap.createBitmap(bitmap, th, 0, th,th);

        imgs[2] = Bitmap.createBitmap(bitmap,six, 0, th,th);
        imgs[3] = Bitmap.createBitmap(bitmap, 0, th, th,th);
        imgs[4] = Bitmap.createBitmap(bitmap, th, th, th,th);
        imgs[5] = Bitmap.createBitmap(bitmap, six,th ,th,th);
        imgs[6] = Bitmap.createBitmap(bitmap, 0, six, th,th);

        imgs[7] = Bitmap.createBitmap(bitmap, th, six,th,th);
        imgs[8] = Bitmap.createBitmap(bitmap, six,six,th,th);

         tilel = new PuzzleTile(imgs[(NUM_TILES*NUM_TILES)-1],(NUM_TILES*NUM_TILES)-1);
       // tiles.clear();
        tiles.add(0,new PuzzleTile(imgs[0],0));

        tiles.add(1,new PuzzleTile(imgs[1],1));
        tiles.add(2,new PuzzleTile(imgs[2],2));
        tiles.add(3,new PuzzleTile(imgs[3],3));
        tiles.add(4,new PuzzleTile(imgs[4],4));
        tiles.add(5,new PuzzleTile(imgs[5],5));
        tiles.add(6,new PuzzleTile(imgs[6],6));
        tiles.add(7,new PuzzleTile(imgs[7],7));
        tiles.add(8,null);
        Log.d("on","innnnnn");*/

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        NUM_TILES= otherBoard.NUM_TILES;
        steps= otherBoard.steps+1;
        previousboard = otherBoard;
    }
    public PuzzleBoard getPreviousboard(){
        return previousboard;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {

        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {

        ArrayList<PuzzleBoard> neighbour = new ArrayList<>();
        PuzzleBoard curr = new PuzzleBoard(this);
        for(int i=0;i<NUM_TILES*NUM_TILES;i++) {
            if (tiles.get(i) == null) {
                //Log.d("oncreate",String.valueOf(NUM_TILES)+"in neighbours");
                int tilex = i % NUM_TILES;
                int tiley = i / NUM_TILES;
                //Log.d("oncreate",String.valueOf(tilex)+String.valueOf(tiley)+"in neighbours giving tilex and tiley");

                for (int[] delta : NEIGHBOUR_COORDS) {
                    int nullX = tilex + delta[0];
                    int nullY = tiley + delta[1];
                    if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES) {
                        // Log.d("oncreate",String.valueOf(nullX)+String.valueOf(nullY)+"in neighbours giving nullx and nully");

                        PuzzleBoard curr1 = new PuzzleBoard(this);
                        //Log.d("oncreate",String.valueOf(curr1.getNUM_TILES())+" in nei");
                        curr1.swapTiles(XYtoIndex(tilex, tiley), XYtoIndex(nullX, nullY));
                        neighbour.add(new PuzzleBoard(curr1));

                    }

                }
                break;
            }
        }
        // Log.d("oncreate",String.valueOf(neighbour.size())+"in neighbours giving ret size ");

        return neighbour;
    }

    public int priority() {
        int mdist=0;
        for(int i=0;i<NUM_TILES*NUM_TILES;i++){
            PuzzleTile temp;
            temp = tiles.get(i);
            if(temp!=null) {
                int tilex = i % NUM_TILES;
                int tiley = i / NUM_TILES;
                int n = temp.getNumber();
                int goalx = n % NUM_TILES;
                int goaly = n / NUM_TILES;
                mdist += (Math.abs(tilex) - Math.abs(goalx)) + (Math.abs(tiley) - Math.abs(goaly));
            }
        }
        mdist+=steps;
        return mdist;
    }

}
