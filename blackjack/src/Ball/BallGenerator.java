package Ball;

import java.util.Random;

public class BallGenerator {

    public Ball generate() {

        Ball ball = new Ball();

        Random random = new Random();
        int ballNumber = random.nextInt(36);
        ball.setNumber(ballNumber);
        ball.setColour(determinateColour(ballNumber));
        ball.setEven(oddOrEven(ballNumber));

        return ball;

    }

    private String determinateColour(int numberLandedOn) {

        String colour;

        if (numberLandedOn == 0) {

            colour = ("Green " + numberLandedOn);

        } else if (numberLandedOn % 2 == 0) {

            colour = ("Black " + numberLandedOn);

        }

        else {

            colour = ("Red " + numberLandedOn);

        }

        return colour;

    }

    private boolean oddOrEven(int numberLandedOn) {

        boolean even = false;

        if (numberLandedOn == 0) {

            even = true;

        }

        if (numberLandedOn  % 2 == 0) {

            even = true;

        }

        if (numberLandedOn % 2 == 1) {

            even = false;

        }

        return even;

    }
}
