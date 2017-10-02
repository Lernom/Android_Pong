package rust.pong.AI;

import rust.pong.Ball;
import rust.pong.PlayerBatObstacle;

/**
 * Created by Lernom on 19.09.2017.
 */

public abstract class AAIController {

    public PlayerBatObstacle Bat;

    public abstract void CalculateStep(Ball ball, double deltaTime);
}
