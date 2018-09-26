package ca.uwaterloo.cs349.a4;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yin on 2017-12-03.
 */

public class Simon {
    public enum State{START, COMPUTER, HUMAN, LOSE, WIN };
    State state;
    int score;
    public int length;
    int buttons;
    List<Integer> sequence = new ArrayList<Integer>();
    int index;

    void init(int _buttons){
        length = 1;
        buttons = _buttons;
        state = State.START;
        score = 0;
        System.out.printf("[DEBUG] starting %d button game\n", buttons);
    }

    Simon(int _buttons) {
        init(_buttons);
    }
    int getNumButtons() { return buttons; }

    int getScore() { return score; }

    State getState() { return state; }

    String getStateAsString() {
        switch (getState()) {
            case START:
                return "START";
            case COMPUTER:
                return "COMPUTER";
            case HUMAN:
                return "HUMAN";
            case LOSE:
                return "LOSE";
            case WIN:
                return "WIN";
            default:
                return "Unkown State";
        }
    }

    void newRound() {
        System.out.printf("[DEBUG] newRound, Simon::state" + getStateAsString() + "\n");
        if (state == State.LOSE) {
            System.out.printf("[DEBUG] reset length and score after loss\n");
            length = 1;
            score = 0;
        }
        sequence.clear();
        System.out.printf("[DEBUG] new sequence: ");
        for (int i = 0; i < length; i++) {
            int b = 0 + (int)(Math.random() * (buttons));
            sequence.add(b);
            System.out.printf("%d ", b);
        }
        System.out.printf("\n");
        index = 0;
        state = State.COMPUTER;
    }
    // call this to get next button to show when computer is playing
    int nextButton() {
        if (state != State.COMPUTER) {
            System.out.printf("[WARNING] nextButton called in " + getStateAsString() + "\n");
            return -1;
        }
        // this is the next button to show in the sequence
        int button = sequence.get(index);
        System.out.printf("[DEBUG] nextButton:  index %d button %d\n", index, button);
        // advance to next button
        index++;
        // if all the buttons were shown, give
        // the human a chance to guess the sequence
        if (index >= sequence.size()) {
            index = 0;
            state = State.HUMAN;
        }
        return button;
    }

    boolean verifyButton(int button) {
        if (state != State.HUMAN) {
            System.out.printf("[WARNING] verifyButton called in " + getStateAsString() + "\n");
            return false;
        }
        // did they press the right button?
        boolean correct = (button == sequence.get(index));
        System.out.printf("[DEBUG] verifyButton: index %d, pushed %d, sequence %d", index, button, sequence.get(index));
        // advance to next button
        index++;
        // pushed the wrong buttons
        if (!correct) {
            state = State.LOSE;
            System.out.printf(", wrong\n");
            System.out.printf("[DEBUG] state is now " + getStateAsString() + "\n");
            // they got it right
        } else {
            System.out.printf(", correct.\n");
            // if last button, then the win the round
            if (index == sequence.size()) {
                state = State.WIN;
                // update the score and increase the difficulty
                score++;
                length++;
                System.out.printf("[DEBUG] state is now " + getStateAsString() + "\n");
                System.out.printf("[DEBUG] new score %d, length increased to %d \n", score, length);
            }
        }
        return correct;
    }
}
