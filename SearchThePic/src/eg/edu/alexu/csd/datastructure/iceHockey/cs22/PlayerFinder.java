package eg.edu.alexu.csd.datastructure.iceHockey.cs22;

import java.awt.*;

public class PlayerFinder implements IPlayersFinder {
    public java.awt.Point[] findPlayers(String[] photo, int team, int threshold) {
        if(photo.length == 0 ) return null;
        int row = photo.length;
        int colm = photo[0].length();
        int [][] people = new int[row][colm];
        for (int i = 0; i < row; i++){ // extracting the wanted important pixels
            for (int j = 0; j < colm; j++){
                if (Character.getNumericValue(photo[i].charAt(j)) == team){
                    people[i][j] = team;
                }
                else {
                    people[i][j] = 0;
                }
            }
        }
        int z = 0,tx,ty;
        Point [] answer = new Point[50];
        for(int i = 0; i < answer.length; i++){ //initializing a points array with value -1
            answer[i] = new Point(-1,-1);
        }
        for (int i = 0; i < row; i++){ //checking for chains of pixels
            for (int  j = 0; j < colm; j++){
                int[] a = new int[]{0,0,0,i,j};
                a = check(people,i,j,team,a);
                if (a[0] >= (threshold/4.0)){ // data(chain,yMax,xMax,yMin,xMin)
                    tx = a[2] + a[4] + 1;
                    ty = a[1] + a[3] + 1;
                    answer[z] = new Point(tx,ty);
                    z++;
                }
            }
        }
        for (int i = 0; i < answer.length; i++){ //sort the points array
            for (int j = i+1; j < answer.length; j++){
                if(answer[i].x > answer[j].x){
                    Point temp = new Point();
                    temp = answer[i];
                    answer[i] = answer[j];
                    answer[j] = temp;
                }
                else if (answer[i].x == answer[j].x){
                    if(answer[i].y > answer[j].y){
                        Point temp = new Point();
                        temp = answer[i];
                        answer[i] = answer[j];
                        answer[j] = temp;
                    }
                }
            }
        }
        z = 0;
        for (int i = 0; i < answer.length; i++){ //getting the valid points number
            if (answer[i].x != -1){
                z++;
            }
        }
        Point[] result = new Point[z];
        z = 0;
        for (int i = 0; i < answer.length; i++){
            if (answer[i].x != -1){
                result[z] = answer [i];
                z++;
            }
        }
        return result;
    }
    public static int[] check(int[][] pic, int row,int colm, int team,int [] data){
        if(row < pic.length && row >= 0 && colm < pic[0].length && colm >= 0){
            if(pic[row][colm] != team) {
                data = new int[]{0,0,0,0};
                return data;
            }
            pic[row][colm] = -1;
            if(row > data[1]) data[1] = row;//rowMax = yMax
            if(colm > data[2]) data[2] = colm;//colmMax = xMax
            if(row < data[3]) data[3] = row;//rowMin = yMin
            if(colm < data[4]) data[4] = colm;//colmMin = xMin
            int[] left = check(pic,row-1,colm,team,data);
            int[] right = check(pic,row+1,colm,team,data);
            int[] up = check(pic,row,colm-1,team,data);
            int[] down = check(pic,row,colm+1,team,data);
            data[0] = 1 + left[0] + right[0] +up[0] + down[0];
            return data;
        }
        else{
            data = new int[]{0,0,0,0};
            return data;
        }
    }
}

