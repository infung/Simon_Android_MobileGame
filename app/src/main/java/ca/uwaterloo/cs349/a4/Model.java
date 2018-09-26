package ca.uwaterloo.cs349.a4;



import java.util.Observable;
import java.util.Observer;

/**
 * Created by Yin on 2017-12-02.
 */

public class Model extends Observable {
    private static final Model myInstance = new Model();
    static Model getInstance() {return myInstance;}
    Simon simon;
    private boolean systemMode;
    private int num_buttons;
    private int levels;
    private int score;

    Model() {
        num_buttons = 4;
        levels = 1;
        score = 0;
        systemMode = true;
        simon = new Simon(num_buttons);
    }

    public void reset() {
        simon = new Simon(num_buttons);
    }

    public void setSystemMode(boolean m) {
        systemMode = m;
        setChanged();
        notifyObservers();
    }
    public boolean getMode() {return systemMode;}

    public void setButtonsNum(int n) {
        num_buttons = n;
        simon = new Simon(num_buttons);
        setChanged();
        notifyObservers();
    }
    public int getButtonsNum(){return num_buttons;}

    public void setLevels(int l) {
        levels = l;
        setChanged();
        notifyObservers();
    }
    public int getLevels(){return levels;}

    public void setScore(int s) {
        score = s;
        setChanged();
        notifyObservers();
    }
    public int getScore(){return score;}

    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }
}
