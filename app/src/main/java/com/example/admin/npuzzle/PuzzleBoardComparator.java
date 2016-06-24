package com.example.admin.npuzzle;

import android.util.Log;

import java.util.Comparator;

/**
 * Created by Admin on 15-06-2016.
 */
public class PuzzleBoardComparator implements Comparator<PuzzleBoard> {


    @Override
    public int compare(PuzzleBoard p1 ,PuzzleBoard p2) {
        //Log.d("oncreate",String.valueOf(p1.priority()+" dis"));

        // if(p1.priority()<p2.priority())
        //  return -1;
        //else if(p1.priority()>p2.priority())
        return p1.priority()-p2.priority();
        //return 0;

    }


}

