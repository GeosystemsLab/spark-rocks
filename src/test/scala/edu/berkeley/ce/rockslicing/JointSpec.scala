package edu.berkeley.ce.rockslicing

import org.scalatest._
import scala.math.sqrt

class JointSpec extends FunSuite {
  // Sums the difference between all elements in a boundary list
  private def totalDifference(l1: Seq[((Double,Double,Double), Double)],
                              l2: Seq[((Double,Double,Double), Double)]): Double = {
    val differences = l1.zip(l2) map { case (((a,b,c),d), ((x,y,z),w)) =>
      math.abs(x-a) + math.abs(y-b) + math.abs(z-c) + math.abs(w-d)
    }
    differences.sum
  }

  val boundaries = List(((1.0, 0.0, 0.0), 1.0),  // x = 1
                    ((-1.0, 0.0, 0.0), 0.0),     // -x = 0
                    ((0.0, 1.0, 0.0), 1.0),      // y = 1
                    ((0.0, -1.0, 0.0), 0.0))     // -y = 0

  val boundaries2 = List(((1.0, 0.0, 0.0), 1.0),  // x = 1
                    ((-1.0, 0.0, 0.0), 1.0),     // -x = 1
                    ((0.0, 1.0, 0.0), 1.0),      // y = 1
                    ((0.0, -1.0, 0.0), 1.0))     // -y = 1

  test("Dip direction for horizontal joint should be pi/2.0 exactly") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
                      phi=0, cohesion=0, shape=Nil)
    assert(joint.dipDirection == math.Pi/2.0)
  }

  test("Dip direction for vertical joint should be pi/2.0") {
    val joint = Joint((0.0, -1.0, 0.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
                      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - math.Pi/2.0) < NumericUtils.EPSILON)
  }

  test("Dip direction for vertical joint should be pi") {
    val joint = Joint((-1.0, 0.0, 0.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - math.Pi) < NumericUtils.EPSILON)
  }

  test("Dip direction for vertical joint should be 3.0*pi/2.0") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - 3.0*math.Pi/2.0) < NumericUtils.EPSILON)
  }

  test("Dip direction for non-vertical joint should be pi/2.0") {
    val joint = Joint((0.0,-1.0/sqrt(2.0), 1.0/sqrt(2.0)), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - math.Pi/2.0) < NumericUtils.EPSILON)
  }

  test("Dip direction for non-vertical joint should be 3.0*pi/4.0") {
    val joint = Joint((-1.0/sqrt(3.0),-1.0/sqrt(3.0), 1.0/sqrt(3.0)),
                      localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - 3.0*math.Pi/4.0) < NumericUtils.EPSILON)
  }

  test("Dip direction for non-vertical joint should be 7.0*pi/4.0") {
    val joint = Joint((1.0/sqrt(3.0), 1.0/sqrt(3.0), 1.0/sqrt(3.0)), localOrigin=(0.0,0.0,-1.0),
                      center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipDirection - 7.0*math.Pi/4.0) < NumericUtils.EPSILON)
  }

  test("Dip angle for horizontal joint should be 0.0 exactly") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(joint.dipAngle == 0.0)
  }

  test("Dip angle for vertical joint should be pi/2.0 exactly") {
    val joint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(joint.dipAngle == math.Pi/2.0)
  }

  test("Dip angle for joint with positive z-component in normal should be pi/6.0") {
    val joint = Joint((1.0/sqrt(1.0 + sqrt(3.0)), 0.0, sqrt(3.0)/sqrt(1.0 + sqrt(3.0))),
                      localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipAngle - math.Pi/6.0) < NumericUtils.EPSILON)
  }

  test("Dip angle for joint with negative z-component in normal should be pi/6.0") {
    val joint = Joint((0.0, 1.0/sqrt(1.0 + sqrt(3.0)), -sqrt(3.0)/sqrt(1.0 + sqrt(3.0))),
                       localOrigin=(0.0,0.0,-1.0), center=(0.0,0.0,0.0),
      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.dipAngle - math.Pi/6.0) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/2.0 centered " +
       "at origin with positive z component in normal") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0,0.0,0.0),
                       phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
        List(((1.0, 0.0, 0.0), 1.0),
             ((-1.0, 0.0, 0.0), 0.0),
             ((0.0, -1.0, 0.0), 1.0),
             ((0.0, 1.0, 0.0), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/2.0 centered " +
       "at origin with negative z component in normal") {
    val joint = Joint((0.0, 0.0, -1.0), localOrigin=(0.0,0.0,0.0), center=(0.0,0.0,0.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
      List(((1.0, 0.0, 0.0), 1.0),
        ((-1.0, 0.0, 0.0), 0.0),
        ((0.0, -1.0, 0.0), 1.0),
        ((0.0, 1.0, 0.0), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/2.0 centered at (1.0, 2.0, 3.0)") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(1.0,2.0,3.0),
                       phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
        List(((1.0, 0.0, 0.0), 2.0),
          ((-1.0, 0.0, 0.0), -1.0),
          ((0.0, -1.0, 0.0), -1.0),
          ((0.0, 1.0, 0.0), 2.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of 0.0 centered at origin") {
    val joint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 0.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
        List(((0.0, 1.0, 0.0), 1.0),
          ((0.0, -1.0, 0.0), 0.0),
          ((0.0, 0.0, -1.0), 1.0),
          ((0.0, 0.0, 1.0), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of 3*pi/2 centered at origin") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 0.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
      List(((-1.0, 0.0, 0.0), 1.0),
        ((1.0, 0.0, 0.0), 0.0),
        ((0.0, 0.0, -1.0), 1.0),
        ((0.0, 0.0, 1.0), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/4 centered at origin") {
    val joint = Joint((1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), localOrigin=(0.0,0.0,0.0),
                      center=(0.0, 0.0, 0.0), phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
      List(((1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), 1.0),
        ((-1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), 0.0),
        ((0.0, 0.0, -1.0), 1.0),
        ((0.0, 0.0, 1.0), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/4 centered at (1.0, 2.0, 3.0") {
    val joint = Joint((1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), localOrigin=(0.0,0.0,0.0),
      center=(1.0, 2.0, 3.0), phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
      List(((1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), 1.0 + sqrt(1.5*1.5 + 1.5*1.5)),
        ((-1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), -sqrt(1.5*1.5 + 1.5*1.5)),
        ((0.0, 0.0, -1.0), -2.0),
        ((0.0, 0.0, 1.0), 3.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("Local-to-global coordinates check: Joint with dip direction of pi/2 and " +
       "dip angle of pi/6 centered at origin") {
    val joint = Joint((0.0, -math.sin(math.Pi/6.0), math.cos(math.Pi/6.0)), localOrigin=(0.0,0.0,0.0),
                      center=(0.0, 0.0, 0.0), phi=0, cohesion=0, shape=boundaries)
    val newBoundaries: List[((Double,Double,Double), Double)] =
      List(((1.0, 0.0, 0.0), 1.0),
        ((-1.0, 0.0, 0.0), 0.0),
        ((0.0, -math.cos(math.Pi/6.0), -math.sin(math.Pi/6.0)), 1.0),
        ((0.0, math.cos(math.Pi/6.0), math.sin(math.Pi/6.0)), 0.0))
    assert(totalDifference(joint.globalCoordinates, newBoundaries) < NumericUtils.EPSILON)
  }

  test("The joint should have distance 1/2.0") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 1/2.0),
                      phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.d - 1/2.0) < NumericUtils.EPSILON)
  }

  test("The joint with non-zero origin should have distance 1/2.0") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,1/2.0), center=(0.0, 0.0, 1.0),
       phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.d - 1/2.0) < NumericUtils.EPSILON)
  }

  test("The plane x/sqrt(2.0) - z/sqrt(2.0) = 1 at center (0.0,0.0,1.0) should have distance 1.0 from local origin") {
    val joint = Joint((1.0/sqrt(2.0), 0.0, -1.0/sqrt(2.0)),
                      localOrigin=(-1.0/sqrt(2.0),0.0,1.0 + 1.0/sqrt(2.0)), center=(0.0, 0.0, 1.0),
       phi=0, cohesion=0, shape=Nil)
    assert(math.abs(joint.d - 1.0) < NumericUtils.EPSILON)
  }

  test("Joint should have updated distance 1.0 from new local origin") {
    val joint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 0.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newJoint = joint.updateJoint(-1.0, 0.0, 0.0)
    assert(math.abs(newJoint.d - 1.0) < NumericUtils.EPSILON)
  }

  test("Joint should have updated distance -8.0 from new local origin") {
    val joint = Joint((1.0, 0.0, 0.0), localOrigin=(-1.0,0.0,0.0), center=(0.0, 0.0, 0.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newJoint = joint.updateJoint(8.0, 0.0, 0.0)
    assert(math.abs(newJoint.d + 8.0) < NumericUtils.EPSILON)
  }

  test("Joint should have updated distance -5.0 from new local origin") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,1.0), center=(1.0, 2.0, 3.0),
                      phi=0, cohesion=0, shape=boundaries)
    val newJoint = joint.updateJoint(8.0, 0.0, 8.0)
    assert(math.abs(newJoint.d + 5.0) < NumericUtils.EPSILON)
  }

  test("Oblique joint should have updated distance of 1/sqrt(2.0)") {
    val joint = Joint((1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), localOrigin = (0.0, 0.0, 0.0),
                      center = (1.0, 0.0, 0.0), phi = 0.0, cohesion = 0.0, shape = Nil)
    val newJoint = joint.updateJoint(0.0, 0.0, 0.0)
    assert(math.abs(newJoint.d - 1.0/sqrt(2.0)) < NumericUtils.EPSILON)
  }

  test("Oblique joint should have updated distance of 0.0") {
    val joint = Joint((-1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), localOrigin = (0.0, 0.0, 0.0),
                      center = (1.0, 0.0, 0.0), phi = 0.0, cohesion = 0.0, shape = Nil)
    val newJoint = joint.updateJoint(1.0, 0.0, 0.0)
    assert(math.abs(newJoint.d) < NumericUtils.EPSILON)
  }

  test("Joint bounding sphere should have origin (0.5, -0.5, 0.0) and radius sqrt(0.5^2 + 0.5^2)") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0,0.0,0.0),
                       phi=0, cohesion=0, shape=boundaries)
    val expectedBoundingSphere = ((0.5, -0.5, 0.0), math.sqrt(math.pow(0.5, 2) + math.pow(0.5, 2)))
    val jointBS = joint.boundingSphere.get
    assert(expectedBoundingSphere == jointBS)
  }

  test("Joint bounding sphere should have origin (0.0, 0.0, 0.0) and radius sqrt(2)") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0,0.0,0.0),
                       phi=0, cohesion=0, shape=boundaries2)
    val expectedBoundingSphere = ((0.0, 0.0, 0.0), math.sqrt(2))
    val jointBS = joint.boundingSphere.get
    assert(expectedBoundingSphere == jointBS)
  }

  test("Joint bounding sphere should have origin (0.5, 0.0, -0.5) and radius sqrt(0.5^2 + 0.5^2)") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0,0.0,0.0),
                       phi=0, cohesion=0, shape=boundaries)
    val expectedBoundingSphere = ((0.5, 0.0, -0.5), math.sqrt(math.pow(0.5, 2) + math.pow(0.5, 2)))
    val jointBS = joint.boundingSphere.get
    assert(expectedBoundingSphere == jointBS)
  }
}
