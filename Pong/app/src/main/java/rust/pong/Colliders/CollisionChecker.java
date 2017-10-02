package rust.pong.Colliders;

import rust.pong.Vector;
import rust.pong.VectorMath;

/**
 * Created by Lernom on 12.09.2017.
 */

public class CollisionChecker {

    //IF ALL ELSE FAILS
    public static Vector CenterVector = new Vector(200,200);

    public static CollisionTestResult BoxCircleCollision(BoxCollider box, CircleCollider circle){
        CollisionTestResult result = pointInRectangle(circle.P, box);
        if(result.Collided)
            return  result;
        return intersectCircle(circle, box);
    }

    private static CollisionTestResult pointInRectangle(Vector P, BoxCollider box){
        return new CollisionTestResult(P.X > box.X && P.X < box.X + box.W && P.Y > box.Y && P.Y < box.Y + box.H, P, P.Minus(CenterVector).Normalize());
    }

    private static CollisionTestResult intersectCircle(CircleCollider circle, BoxCollider box) {
        CollisionTestResult result = IntersectCircleWithLine(circle, new Vector(box.X, box.Y), new Vector(box.X + box.W, box.Y));
        if (result.Collided)
            return result;
        result = IntersectCircleWithLine(circle, new Vector(box.X + box.W, box.Y), new Vector(box.X + box.W, box.Y + box.H));
        if (result.Collided)
            return result;
        result = IntersectCircleWithLine(circle, new Vector(box.X + box.W, box.Y + box.H), new Vector(box.X, box.Y + box.H));
        if (result.Collided)
            return result;
        result = IntersectCircleWithLine(circle, new Vector(box.X, box.Y + box.H), new Vector(box.X, box.Y));
        return result;
    }

    private static CollisionTestResult IntersectCircleWithLine(CircleCollider circle, Vector P1, Vector P2){
        Vector lineVector = P2.Minus(P1);
        Vector pVector = new Vector(-lineVector.Y, lineVector.X).Normalize();
        Vector circleVector = circle.P.Add(pVector.Multiply(circle.R));
        Vector intersection = VectorMath.LineSegmentsIntersection(P1,P2,circle.P,circleVector);
        if(intersection.X != 0 && intersection.Y != 0){
            return new CollisionTestResult(true, intersection, pVector);
        }
        return new CollisionTestResult(false, null, null);
    }

    public static  CollisionTestResult BoxBoxCollision(BoxCollider box1, BoxCollider box2){
        if (box1.Y + box1.H < box2.Y
                || box1.Y > box2.Y + box2.H
                || box1.X + box1.W < box2.X
                || box1.X > box2.X + box2.W) return new CollisionTestResult(false,null,null);
        return new CollisionTestResult(true,null,null);
    }

    public static   CollisionTestResult CircleCircleCollision(CircleCollider circle1, CircleCollider circle2){
        double distance = circle1.P.Minus(circle2.P).Magnitude();
        return new CollisionTestResult(distance < circle1.R + circle2.R, null, null);
    }
}
