package rust.pong.AI;

import rust.pong.Ball;
import rust.pong.PlayerBatObstacle;

/**
 * Created by Lernom on 19.09.2017.
 */

public class BasicAI extends AAIController {

    public double Speed = 30;

    public BasicAI(double speed, PlayerBatObstacle bat){
        Bat = bat;
        Speed = speed;
    }

    @Override
    public void CalculateStep(Ball ball, double deltaTime) {

        double delta = ball.Collider.P.X - (Bat.Collider.X + Bat.Collider.W / 2);

        Bat.Collider.X += Math.signum(delta) * Speed * deltaTime;
    }
}
