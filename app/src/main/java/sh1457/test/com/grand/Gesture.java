package sh1457.test.com.grand;

import java.util.LinkedList;
import java.util.Collection;

import android.util.Log;


class Gesture {
    private static final int uX = 3;
    private static final int uY = 3;
    private static final String TAG = "Gesture";

    private int gid;
    private int dX, dY, mX, mY;
    private int eCode;
    private MoveList<Move> ml;
    private int[][] gm = new int[uX][uY];

    Gesture() {
        this.gid=0;
        this.dX=0;
        this.dY=0;
        this.mX=0;
        this.mY=0;
        this.eCode=0;
        this.ml=new MoveList<>();

        for (int i=0; i<uX; i++) {
            for (int j=0; j<uY; j++) {
                this.gm[i][j]=0;
            }
        }
    }

    Gesture(int gid, String gstring) {
        this.gid=gid;
        this.dX=0;
        this.dY=0;
        this.mX=0;
        this.mY=0;
        this.eCode=1;
        this.ml=new MoveList<>();

        setGestureMatrix(gstring);
    }

    int getID() {
        return this.gid;
    }

    void setID(int gid) {
        this.gid=gid;
    }

    int getECode() {
        return this.eCode;
    }

    String getString() {
        String s="";

        for (int i=0; i<uX; i++) {
            for (int j=0; j<uY; j++) {
                s += this.gm[i][j] + "|";
            }
        }

        return s;
    }

    private void setGestureMatrix() {
        int iX, iY;

        //Set initial co-ordinates
        iX = (mX > 0) ? 0 : -mX;
        iY = (mY > 0) ? 0 : -mY;

        gm[iX][iY]=1;

        //Set remaining co-ordinates
        for (int i=0; i < ml.size(); i++) {
            Move m = ml.get(i);
            iX += m.getV_X();
            iY += m.getV_Y();
            if (gm[iX][iY] > 0) {
                gm[iX][iY] += (i+2)*10;
            }else {
                gm[iX][iY] = i+2;
            }
        }
    }

    void setGestureMatrix(String g) {
        int iX=0, iY=0;
        int i=0, j=0;

        for (String d: g.split("|")) {
            try {
                gm[i][j] = Integer.parseInt(d);
            }catch (NumberFormatException e) {
                Log.e(TAG, "");
            }finally {
                gm[i][j] = 0;
            }

            if (gm[i][j]%10 == 1) {
                iX=i;
                iY=j;
            }

            j++;

            if (j>=uX) {
                j=0;
                i++;
                if (i>=uY) {
                    break;
                }
            }
        }

        //Set movelist
        int tX, tY;
        int cnt=0;

        i=iX;
        j=iY;

        tX=i-1;
        tY=j-1;

        do {
            cnt++;
            if (tX >= 0 && tX < uX) {
                if (tY >= 0 && tY < uY) {
                    if (gm[i+tX][j+tY]%10 == (gm[i][j]%10 + 1)) {
                        this.dX+=tX;
                        this.dY+=tY;

                        if (this.dX < 0 && Math.abs(this.dX) > Math.abs(this.mX)) {
                            this.mX=this.dX;
                        }
                        if (this.dX > 0 && Math.abs(this.dX) > Math.abs(this.mX)) {
                            this.mX=this.dX;
                        }

                        this.ml.add(new Move(tX, tY, this.dX, this.dY));

                        i+=tX;
                        j+=tY;

                        tX=i-1;
                        tY=j-1;
                    }else {
                        if (tX == i+1 && tY == j+1) {
                            break;
                        }
                    }
                }else {
                    tY++;
                }
            }else {
                tX++;
            }
        }while(cnt < uX*uY);
    }

    private void validateGesture(Move m) {
        //No movement
        if (m.getV_X() == 0 && m.getV_Y() == 0) {
            this.eCode=1;
        }

        //Retrace
        if (this.ml.size() > 0) {
            Move inverse=new Move(-(m.getV_X()), -(m.getV_Y()), m.getD_X(), m.getD_Y());
            if (inverse.equals(this.ml.getLast())) {
                this.eCode=2;
            }
        }

        //Out of Grid
        if (Math.abs(dX + m.getV_X()) > uX || Math.abs(dY + m.getV_Y()) > uY) {
            this.eCode=3;
        }

        //Overlap
    }

    void recordMove(int x, int y) {
        Move current = new Move(x, y, this.dX, this.dY);

        this.validateGesture(current);

        if (this.eCode == 0) {
            this.dX += current.getV_X();
            this.dY += current.getV_Y();

            this.ml.add(current);
        }else {
            if (this.eCode==1) {
                setGestureMatrix();
            }else if(this.eCode==2) {
                Log.e(TAG, "GestureError: IllegalLastMove::Retrace!");
            }else if (this.eCode==3) {
                Log.e(TAG, "GestureError: IllegalLastMove::OutOfBounds!");
            }else if (this.eCode==4) {
                Log.e(TAG, "GestureError: IllegalLastMove::Overlap!");
            }else {
                Log.e(TAG, "Invalid eCode!\n");
            }
        }
    }
}

class MoveList<E> extends LinkedList<E> {
    @Override
    public boolean add(E e) {
        if (this.contains(e)) {
            return false;
        }else {
            return super.add(e);
        }
    }

    @Override
    public void add(int index, E element) {
        if (this.contains(element)) {
            return;
        }else {
            super.add(index, element);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Collection<E> copy = new LinkedList<E>(collection);
        copy.removeAll(this);
        return super.addAll(copy);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        Collection<E> copy = new LinkedList<E>(collection);
        copy.removeAll(this);
        return super.addAll(index, copy);
    }
}

class Move {
    private int vX;
    private int vY;
    private int dX;
    private int dY;

    public Move() {
        this.vX = 0;
        this.vY = 0;
        this.dX = 0;
        this.dY = 0;
    }

    Move(int vX, int vY, int dX, int dY) {
        this.vX = vX;
        this.vY = vY;
        this.dX = dX;
        this.dY = dY;
    }

    int getV_X() {
        return this.vX;
    }

    int getV_Y() {
        return this.vY;
    }

    int getD_X() {
        return this.dX;
    }

    int getD_Y() {
        return this.dY;
    }

    boolean equals(Move m) {
        return (this.vX == m.getV_X() && this.vY == m.getV_Y() && this.dX == m.getD_X() && this.dY == m.getD_Y());
    }

    public String toString() {
        outer:
        switch(this.vX+1) {
            case 0:
                switch(this.vY+1) {
                    case 0:  return "(\u2190\u2193)";
                    case 1:  return "\u2190";
                    case 2:  return "(\u2190\u2191)";
                    default: break outer;
                }
            case 1:
                switch(this.vY+1) {
                    case 0:  return "\u2193";
                    case 1:  return " ";
                    case 2:  return "\u2191";
                    default: break outer;
                }
            case 2:
                switch(this.vY+1) {
                    case 0:  return "(\u2192\u2193)";
                    case 1:  return "\u2192";
                    case 2:  return "(\u2192\u2191)";
                    default: break outer;
                }
            default: break;
        }
        return "x";
    }
}