package rust.pong.Colliders;

import rust.pong.Vector;

/**
 * Created by Lernom on 12.09.2017.
 */

public class CollisionTestResult {

    public boolean Collided;

    public Vector Point;

    public Vector Normal;

    public  CollisionTestResult(boolean result, Vector p, Vector n){
        Point = p;
        Normal = n;
        Collided = result;
    }
}
