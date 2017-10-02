package rust.pong;

/**
 * Created by Lernom on 10.09.2017.
 */

public class VectorMath {

    private static double Epsilon = 1e-10;

    public static boolean IsZero(double d)
    {
        return Math.abs(d) < Epsilon;
    }

    public static Vector LineSegmentsIntersection(Vector p, Vector p2, Vector q, Vector q2)
    {
        Vector intersection = new Vector();

        Vector r = p2.Minus(p);
        Vector s = q2.Minus(q);
        double rxs = r.Cross(s);
        double qpxr = (q.Minus(p)).Cross(r);

        // If r x s = 0 and (q - p) x r = 0, then the two lines are collinear.
        if (IsZero(rxs) && IsZero(qpxr))
            return intersection;

        // 3. If r x s = 0 and (q - p) x r != 0, then the two lines are parallel and non-intersecting.
        if (IsZero(rxs) && !IsZero(qpxr))
            return intersection;

        // t = (q - p) x s / (r x s)
        double t = (q.Minus(p)).Cross(s)/rxs;

        // u = (q - p) x r / (r x s)

        double u = (q.Minus(p)).Cross(r)/rxs;

        // 4. If r x s != 0 and 0 <= t <= 1 and 0 <= u <= 1
        // the two line segments meet at the point p + t r = q + u s.
        if (!IsZero(rxs) && (0 <= t && t <= 1) && (0 <= u && u <= 1))
        {
            // We can calculate the intersection point using either t or u.
            intersection = p.Add(r.Multiply(t));

            // An intersection was found.
            return intersection;
        }

        // 5. Otherwise, the two line segments are not parallel but do not intersect.
        return intersection;
    }
}
