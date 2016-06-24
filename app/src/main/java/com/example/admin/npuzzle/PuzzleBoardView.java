package com.example.admin.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();
    Bitmap b ;
    int width;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        width = getWidth();
        b = Bitmap.createScaledBitmap(imageBitmap, width, width, false);
        puzzleBoard = new PuzzleBoard(b, width,3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (puzzleBoard != null) {

            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    //puzzleBoard.finaltile.draw(canvas,n-1,n-1);
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            int k=20;
            //k=1;
            Random rn = new Random();
            ArrayList<PuzzleBoard> arr = new ArrayList<>();
            while (k>0) {
                int s;
                ArrayList<PuzzleBoard> nei = puzzleBoard.neighbours();
                //Log.d("oncreate",String.valueOf(nei.size())+" in shuffle");
                int l = nei.size();

                if (l > 0) {
                    s = rn.nextInt(l);
                    arr.add(nei.get(s));
                    puzzleBoard = nei.get(s);
                    invalidate();
                }
                k--;
            }


            puzzleBoard.reset();
            //invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {

                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        ArrayList<PuzzleBoard> lis=null;
        PuzzleBoard p ;
        PuzzleBoardComparator pc = new PuzzleBoardComparator();
        Set<PuzzleBoard> unique_neighbours = new LinkedHashSet<>();
        PriorityQueue<PuzzleBoard> puzzleBoardPriorityQueue = new PriorityQueue<PuzzleBoard>(10, pc);
        puzzleBoard.steps = 0;
        puzzleBoard.previousboard = null;
        unique_neighbours.add(puzzleBoard);
        puzzleBoardPriorityQueue.add(puzzleBoard);
        while (!puzzleBoardPriorityQueue.isEmpty()) {
            p = puzzleBoardPriorityQueue.poll();
            if (!p.resolved()) {
                ArrayList<PuzzleBoard> nei = p.neighbours();
                int i, l = nei.size();
                for(i=0;i<l;i++) {
                    if(unique_neighbours.add(nei.get(i)))
                        if(!nei.get(i).equals(p.previousboard))
                            puzzleBoardPriorityQueue.add(nei.get(i));
                }
            }


            else {
                Log.d("oncreate",String.valueOf(puzzleBoardPriorityQueue.size())+"    "+String.valueOf(unique_neighbours.size()));
                lis = new ArrayList<>();
                PuzzleBoard pre, tmp;
                puzzleBoardPriorityQueue.clear();
                pre = p.getPreviousboard();
                lis.add(pre);

                while (pre.getPreviousboard() != null) {
                    tmp = pre.getPreviousboard();
                    lis.add(tmp);
                    pre = tmp;
                }
                Log.d("oncreate",String.valueOf(lis.size()));
                Collections.reverse(lis);

                break;
            }
        }
        animation = lis;
        invalidate();


    }
    public void set_Num_tiles(int i){
        if(i==1){

            b = Bitmap.createScaledBitmap(b, width, width, false);
            //puzzleBoard.setNUM_TILES(puzzleBoard.getNUM_TILES()+1);
            Log.d("oncreate",String.valueOf(puzzleBoard.getNUM_TILES()+1)+" in inc button");
            puzzleBoard = new PuzzleBoard(b,width,puzzleBoard.getNUM_TILES()+1);
            invalidate();
        }
        else{
            if(puzzleBoard.getNUM_TILES()==2){
                Toast.makeText(activity, "Can' decrease any more", Toast.LENGTH_LONG).show();
            }
            else {
                b = Bitmap.createScaledBitmap(b, width, width, false);
                //puzzleBoard.setNUM_TILES(puzzleBoard.getNUM_TILES()-1);
                puzzleBoard = new PuzzleBoard(b, width, puzzleBoard.getNUM_TILES() - 1);
                invalidate();
            }
        }
    }
}




