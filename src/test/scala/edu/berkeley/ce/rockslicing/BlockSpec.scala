package edu.berkeley.ce.rockslicing

import org.scalatest._
import scala.math.sqrt

class BlockSpec extends FunSuite {
  val boundingFaces = List(
    Face((-1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0), // -x = 0
    Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),  // x = 1
    Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0), // -y = 0
    Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0),  // y = 1
    Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0), // -z = 0
    Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0)   // z = 1
  )
  val unitCube = Block((0.0, 0.0, 0.0), boundingFaces)

  val boundingFaces2 = List(
    Face((-1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0), // -x = 0
    Face((1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0), // x = 2
    Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0), // -y = 0
    Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0), // y = 2
    Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0), // -z = 0
    Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0) // z = 2
  )
  val twoCube = Block((0.0, 0.0, 0.0), boundingFaces2)

  val boundingFaces3 = List(
    Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0), // -x = 1
    Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),  // x = 1
    Face((0.0, -1.0, 0.0), 1.0, phi=0, cohesion=0), // -y = 1
    Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0),  // y = 1
    Face((0.0, 0.0, -1.0), 1.0, phi=0, cohesion=0), // -z = 1
    Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0)   // z = 1
  )
  val twoCubeNonOrigin = Block((1.0, 1.0, 1.0), boundingFaces3)

  val boundingFaces4 = List(
    Face((-1.0, 0.0, 0.0), 0.5, phi=0, cohesion=0), // -x = 0
    Face((1.0, 0.0, 0.0), 0.5, phi=0, cohesion=0),  // x = 1
    Face((0.0, -1.0, 0.0), 0.5, phi=0, cohesion=0), // -y = 0
    Face((0.0, 1.0, 0.0), 0.5, phi=0, cohesion=0),  // y = 1
    Face((0.0, 0.0, -1.0), 0.5, phi=0, cohesion=0), // -z = 0
    Face((0.0, 0.0, 1.0), 0.5, phi=0, cohesion=0)   // z = 1
  )
  val unitCubeNonOrigin = Block((0.5, 0.5, 0.5), boundingFaces4)

  val boundingFaces6 = List(
    Face((-1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0), // -x = 0
    Face((1.0, 0.0, 0.0), 0.5, phi=0, cohesion=0), // x = 0.5
    Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0), // -y = 0
    Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0), // y = 1
    Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0), // -z = 0
    Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0) // z = 1
  )
  val rectPrism = Block((0.0, 0.0, 0.0), boundingFaces6)

  val boundingFaces7 = List(
    Face((1/math.sqrt(3.0), 1/math.sqrt(3.0), 1/math.sqrt(3.0)), 0.0, phi=0, cohesion=0),
    Face((-1.0, 0.0, 0.0), 0.3, phi=0, cohesion=0),
    Face((0.0, -1.0, 0.0), 0.3, phi=0, cohesion=0),
    Face((0.0, 0.0, -1.0), 0.3, phi=0, cohesion=0)
  )
  val tetrahedralBlock = Block((0.3, 0.3, 0.3), boundingFaces7)

  val jointBounds = List(
    ((1.0, 0.0, 0.0), 1.0),
    ((-1.0, 0.0, 0.0), 0.0),
    ((0.0, 1.0, 0.0), 1.0),
    ((0.0, -1.0, 0.0), 0.0)
  )

  val jointBounds2 = List(
    ((1.0, 0.0, 0.0), 1.0),
    ((-1.0, 0.0, 0.0), 1.0),
    ((0.0, 1.0, 0.0), 1.0),
    ((0.0, -1.0, 0.0), 1.0)
  )

  val jointBounds3 = List(
    ((1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), 1.0),
    ((-1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), 1.0),
    ((-1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), 1.0),
    ((1.0/sqrt(2.0), -1.0/sqrt(2.0), 0.0), 1.0)
  )

  def centroidDifference(c1: (Double, Double, Double), c2: (Double, Double, Double)): Double =
    c1 match {
      case (a, b, c) => c2 match {
        case (x, y, z) => math.abs(x - a) + math.abs(y - b) + math.abs(z - c)
      }
    }

  test("The plane z = 0.5 should intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 1/2.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane z = 0.5 with non-zero origin should intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,-1/4), center=(0.0, 0.0, 1/4.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane -z = 0.5 should intersect the unit cube") {
    val joint = Joint((0.0, 0.0, -1.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 1/2.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane -z = 0.5 with non-zero origin should intersect the unit cube") {
    val joint = Joint((0.0, 0.0, -1.0), localOrigin=(0.0,0.0,-1/4), center=(0.0, 0.0, 1/4.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane z = 2 should not intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.0, 2.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The plane z = 1 with non-zero origin should not intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0,0.0,1.0), center=(0.0, 0.0, 2.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The plane y = 1 should not intersect the unit cube") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 1.0, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The plane y = 1/2.0 with non-zero origin should not intersect the unit cube") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,1/2.0,0.0), center=(0.0, 1.0, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    val translatedJoint = joint.updateJoint(0.0, 0.0, 0.0)
    assert(unitCube.intersects(translatedJoint).isEmpty)
  }

  test("The plane -y = 1 should not intersect the unit cube") {
    val joint = Joint((0.0, -1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 1.0, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The plane y = 0.99 should intersect the unit cube") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.99, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane y = 0.49 with non-zero origin should intersect the unit cube") {
    val joint = Joint((0.0, 1.0, 0.0), localOrigin=(0.0,0.5,0.0), center=(0.0, 0.99, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane -y = 0.99 should intersect the unit cube") {
    val joint = Joint((0.0, -1.0, 0.0), localOrigin=(0.0,0.0,0.0), center=(0.0, 0.99, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane -y = 0.49 with non-zero origin should intersect the unit cube") {
    val joint = Joint((0.0, -1.0, 0.0), localOrigin=(0.0,0.5,0.0), center=(0.0, 0.99, 0.0),
                       phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The plane x/sqrt(2.0) - z/sqrt(2.0) = 1 at non-zero origin should intersect the unit cube") {
    val joint = Joint((1.0/sqrt(2.0), 0.0, -1.0/sqrt(2.0)), localOrigin=(-1.0/sqrt(2.0),0.0,1.0/sqrt(2.0)),
                      center=(0.0, 0.0, 0.0), phi=0, cohesion=0, shape=Nil)
    val translatedJoint = joint.updateJoint(0.0, 0.0, 0.0)
    assert(unitCube.intersects(translatedJoint).isDefined)
  }

  test("The plane x/sqrt(2.0) - z/sqrt(2.0) = 1 at (0.0,0.0,1.0) should not intersect the unit cube") {
    val joint = Joint((1.0/sqrt(2.0), 0.0, -1.0/sqrt(2.0)), localOrigin=(-1.0/sqrt(2.0),0.0,1.0 + 1.0/sqrt(2.0)),
                      center=(0.0, 0.0, 1.0),  phi=0, cohesion=0, shape=Nil)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The plane x/sqrt(2.0) - z/sqrt(2.0) = 1 at global origin should intersect the two cube") {
    val joint = Joint((1.0/sqrt(2.0), 0.0, -1.0/sqrt(2.0)), localOrigin=(-1.0/sqrt(2.0),0.0,1.0/sqrt(2.0)),
                       center=(0.0, 0.0, 0.0), phi=0, cohesion=0, shape=Nil)
    assert(twoCube.intersects(joint).isDefined)
  }

  test("The plane x/sqrt(2.0) - z/sqrt(2.0) = 1 at (0.0,0.0,1.0) should intersect the two cube") {
    val joint = Joint((1.0/sqrt(2.0), 0.0, -1.0/sqrt(2.0)), localOrigin=(-1.0/sqrt(2.0),0.0,1.0 + 1.0/sqrt(2.0)),
                       center=(0.0, 0.0, 1.0),  phi=0, cohesion=0, shape=Nil)
    assert(twoCube.intersects(joint).isDefined)
  }

  test("The non-persistent joint z < 1.0 should not intersect the two cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(2.0, 0.0, 1.0),
                      phi=0.0, cohesion=0.0, shape=jointBounds)
    assert(twoCube.intersects(joint).isEmpty)
  }

  test("The non-persistent joint z < 1.0 should intersect the two cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(1.99, 0.01, 1.0),
                      phi=0.0, cohesion=0.0, shape=jointBounds)
    assert(twoCube.intersects(joint).isDefined)
  }

  test("The non-persistent joint z < 1.0 with non-global origin should not intersect the two cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(3.0, 0.0, 0.0), center=(3.0, 0.0, 1.0),
                      phi=0.0, cohesion=0.0, shape=jointBounds2)
    val translatedJoint = joint.updateJoint(0.0, 0.0, 0.0)
    assert(twoCube.intersects(translatedJoint).isEmpty)
  }

  test("The non-persistent joint z < 1.0 with non-global origin should intersect the two cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(3.0, 0.0, 0.0), center=(2.99, 0.01, 1.0),
                      phi=0.0, cohesion=0.0, shape=jointBounds2)
    assert(twoCube.intersects(joint).isDefined)
  }

  test("The non-persistent joint z < 0 with rotated bounds should not intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(2.0, 2.0, 0.5),
                      phi=0.0, cohesion=0.0, shape=jointBounds3)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The non-persistent joint z < 0 with rotated bounds should JUST not intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0),
                      center=(1.0 + 1.0/sqrt(2.0), 1.0 + 1.0/sqrt(2.0), 0.5),
                      phi=0.0, cohesion=0.0, shape=jointBounds3)
    assert(unitCube.intersects(joint).isEmpty)
  }

  test("The non-persistent joint z < 0 with rotated bounds should intersect the unit cube") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0),
                      center=(1.0 + 1.0/sqrt(2.0), 0.99 + 1.0/sqrt(2.0), 0.5),
                      phi=0.0, cohesion=0.0, shape=jointBounds3)
    assert(unitCube.intersects(joint).isDefined)
  }

  test("The non-persistent joint z < 0 should not intersect the non-global origin two cube ") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(1.0, 1.0, 1.0), center=(1.0, 3.0, 1.0),
                      phi=0.0, cohesion=0.0, shape=jointBounds)
    assert(twoCubeNonOrigin.intersects(joint).isEmpty)
  }

  test("The non-persistent joint z < 0 should intersect the non-global origin two cube ") {
    val joint = Joint((0.0, 0.0, 1.0), localOrigin=(1.0, 1.0, 1.0), center=(1.99, 2.99, 1.0),
      phi=0.0, cohesion=0.0, shape=jointBounds)
    assert(twoCubeNonOrigin.intersects(joint).isDefined)
  }

  test("The unit cube should not contain any redundant faces") {
    assert(unitCube.nonRedundantFaces == boundingFaces)
  }

  test("Adding planes x,y,z = +- 2 to the unit cube should be considered redundant") {
    val redundantBoundingFaces = boundingFaces ++ List(
      Face((-1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0), // -x = 2
      Face((1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0), // x = 2
      Face((0.0, -1.0, 0.0), 2.0, phi=0, cohesion=0), // -y = 2
      Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0), // y = 2
      Face((0.0, 0.0, -1.0), 2.0, phi=0, cohesion=0), // -z = 2
      Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0) // z = 2
    )
    val redundantUnitCube = Block((0.0, 0.0, 0.0), redundantBoundingFaces)

    assert(redundantUnitCube.nonRedundantFaces == boundingFaces)
  }

  test("The two cube should not contain any redundant faces") {
    assert(twoCube.nonRedundantFaces == boundingFaces2)
  }

  test("Adding the boundary -x + 2z = 0 to the two cube should render z = 2 redundant") {
    val newFace = Face((-1.0, 0.0, 2.0), 0.0, phi=0, cohesion=0)
    val redundantBoundingFaces = newFace::boundingFaces2
    val redundant2Cube = Block((1.0, 1.0, 1.0), redundantBoundingFaces)
    val expectedFaces = redundantBoundingFaces.slice(0, redundantBoundingFaces.length - 1)
    assert(redundant2Cube.nonRedundantFaces == expectedFaces)
  }

  test("Moving origin of two-cube to (1,0,0) should only affect x-distances") {
    val newFaces = twoCube.updateFaces((1.0, 0.0, 0.0))
    val newBlock = Block((1.0, 0.0, 0.0), newFaces)
    val testFaces = newBlock.nonRedundantFaces
    val expectedFaces = List(
      Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0),
      Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0),
      Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0),
      Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0)
    )
    assert(testFaces == expectedFaces)
  }

  test("Moving origin of two-cube to (1,1,1) should affect all distances") {
    val newFaces = twoCube.updateFaces((1.0, 1.0, 1.0))
    val newBlock = Block((1.0, 1.0, 1.0), newFaces)
    val testFaces = newBlock.nonRedundantFaces
    val expectedFaces = List(
      Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((0.0, -1.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0),
      Face((0.0, 0.0, -1.0), 1.0, phi=0, cohesion=0),
      Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0)
    )
    assert(testFaces == expectedFaces)
  }

  test("Adding the face -x=1 to a two-cube centered at origin should be considered redundant") {
    val redundantFace = Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val newTwoCube = Block((0.0, 0.0, 0.0), redundantFace::boundingFaces2)
    val testFaces = newTwoCube.nonRedundantFaces
    assert(testFaces == boundingFaces2)
  }

  test("The points of intersection between the four planes should (0.0, 0.0, 0.0) & (0.0, 5.0, 0.0)") {
    val face1 = Face((-1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val face2 = Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0)
    val face4 = Face((0.0, 1.0, 0.0), 5.0, phi=0, cohesion=0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3, face4))
    val face1Verts = List((0.0, 0.0, 0.0), (0.0, 5.0, 0.0))
    val face2Verts = List((0.0, 0.0, 0.0))
    val face3Verts = List((0.0, 0.0, 0.0), (0.0, 5.0, 0.0))
    val face4Verts = List((0.0, 5.0, 0.0))
    val expectedIntersection = Map(
      face1 -> face1Verts,
      face2 -> face2Verts,
      face3 -> face3Verts,
      face4 -> face4Verts
    )
    val vertices = block.findVertices
    assert(vertices == expectedIntersection)
  }

  test("The point of intersection between the three planes should be (1.0, 0.0, 0.0)") {
    val face1 = Face((0.0, 0.0, 1.0), 0.0, phi = 0.0, cohesion = 0.0)
    val face2 = Face((0.0, 1.0, 0.0), 0.0, phi = 0.0, cohesion = 0.0)
    val face3 = Face((1.0/sqrt(2.0), 1.0/sqrt(2.0), 0.0), 1.0/sqrt(2.0), phi = 0.0, cohesion = 0.0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3))
    val face1Verts = List((1.0, 0.0, 0.0))
    val face2Verts = List((1.0, 0.0, 0.0))
    val face3Verts = List((1.0, 0.0, 0.0))
    val expectedIntersetion = Map(
      face1 -> face1Verts,
      face2 -> face2Verts,
      face3 -> face3Verts
    )
    val vertices = block.findVertices
    assert(vertices == expectedIntersetion)
  }

  test("There should be no intersection between the planes") {
    val face1 = Face((1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val face2 = Face((1.0, 0.0, 0.0), 5.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 0.0, phi=0, cohesion=0)
    val block = Block((1.0, 1.0, 1.0), List(face1, face2, face3))
    val expectedIntersection = Map(
      face1 -> Nil,
      face2 -> Nil,
      face3 -> Nil
    )
    val vertices = block.findVertices
    assert(vertices == expectedIntersection)
  }

  test("There should be three entries in the triangulation list ordered in a clockwise fashion") {
    val face1 = Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0)
    val face4 = Face((-1/sqrt(2.0), -1/sqrt(2.0), 0.0), 1/sqrt(2.0), phi=0, cohesion=0)
    val block = Block((1.0, 1.0, 1.0), List(face1, face2, face3, face4))

    val vertices = block.findVertices
    val mesh = block.meshFaces(vertices)
    val expectedVertices = List(Delaunay.Vector2(1.0, 1.0), Delaunay.Vector2(1.0, 0.0), Delaunay.Vector2(0.0, 1.0))

    val expectedTriangulation = Delaunay.Triangulation(expectedVertices).toList
    assert(mesh(face3) == expectedTriangulation)
  }

  test("Centroid should be at (0.0, 0.0, 0.0)") {
    val face1 = Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 1.0, phi=0, cohesion=0)
    val face4 = Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val face5 = Face((0.0, -1.0, 0.0), 1.0, phi=0, cohesion=0)
    val face6 = Face((0.0, 0.0, -1.0), 1.0, phi=0, cohesion=0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3, face4, face5, face6))
    val centroid = block.centroid
    val expectedCentroid = (0.0, 0.0, 0.0)
    assert(centroidDifference(centroid, expectedCentroid) <= NumericUtils.EPSILON)
  }

  test("Centroid should be at (0.5, 0.5, 1.0)") {
    val face1 = Face((1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 1.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0)
    val face4 = Face((-1.0, 0.0, 0.0), 1.0, phi=0, cohesion=0)
    val face5 = Face((0.0, -1.0, 0.0), 1.0, phi=0, cohesion=0)
    val face6 = Face((0.0, 0.0, -1.0), 1.0, phi=0, cohesion=0)
    val block = Block((0.5, 0.5, 0.5), List(face1, face2, face3, face4, face5, face6))
    val centroid = block.centroid
    val expectedCentroid = (0.5, 0.5, 1.0)
    assert(centroidDifference(centroid, expectedCentroid) < NumericUtils.EPSILON)
  }

  test("Centroid should be at (-1.0, -1.0, -1.0)") {
    val face1 = Face((1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 0.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 0.0, phi=0, cohesion=0)
    val face4 = Face((-1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0)
    val face5 = Face((0.0, -1.0, 0.0), 2.0, phi=0, cohesion=0)
    val face6 = Face((0.0, 0.0, -1.0), 2.0, phi=0, cohesion=0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3, face4, face5, face6))
    val centroid = block.centroid
    val expectedCentroid = (-1.0, -1.0, -1.0)
    assert(centroidDifference(centroid, expectedCentroid) < NumericUtils.EPSILON)
  }

  test("New distances should be shifted based on input local origin") {
    val face1 = Face((1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0)
    val face4 = Face((-1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val face5 = Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0)
    val face6 = Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3, face4, face5, face6))
    val updatedFaces = block.updateFaces((2.0, 0.0, 0.0))
    val expectedFace1 = Face((1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val expectedFace2 = Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0)
    val expectedFace3 = Face((0.0, 0.0, 1.0), 2.0, phi=0, cohesion=0)
    val expectedFace4 = Face((-1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0)
    val expectedFace5 = Face((0.0, -1.0, 0.0), 0.0, phi=0, cohesion=0)
    val expectedFace6 = Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0)
    val expectedFaces = List(expectedFace1, expectedFace2, expectedFace3, expectedFace4, expectedFace5, expectedFace6)
    assert(updatedFaces == expectedFaces)
  }

  test("New distances should be shifted based on input local origin - some distances should be negative") {
    val face1 = Face((1.0, 0.0, 0.0), 2.0, phi=0, cohesion=0)
    val face2 = Face((0.0, 1.0, 0.0), 2.0, phi=0, cohesion=0)
    val face3 = Face((0.0, 0.0, -1.0), -2.0, phi=0, cohesion=0)
    val face4 = Face((1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val face5 = Face((0.0, 1.0, 0.0), 0.0, phi=0, cohesion=0)
    val face6 = Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0)
    val block = Block((0.0, 0.0, 0.0), List(face1, face2, face3, face4, face5, face6))
    val updatedFaces = block.updateFaces((2.0, 2.0, 2.0))
    val expectedFace1 = Face((1.0, 0.0, 0.0), 0.0, phi=0, cohesion=0)
    val expectedFace2 = Face((0.0, 1.0, 0.0), 0.0, phi=0, cohesion=0)
    val expectedFace3 = Face((0.0, 0.0, -1.0), 0.0, phi=0, cohesion=0)
    val expectedFace4 = Face((1.0, 0.0, 0.0), -2.0, phi=0, cohesion=0)
    val expectedFace5 = Face((0.0, 1.0, 0.0), -2.0, phi=0, cohesion=0)
    val expectedFace6 = Face((0.0, 0.0, -1.0), 2.0, phi=0, cohesion=0)
    val expectedFaces = List(expectedFace1, expectedFace2, expectedFace3, expectedFace4, expectedFace5, expectedFace6)
    assert(updatedFaces == expectedFaces)
  }

  test("Cutting the two-cube with faces x=0 and z=0 should produce four blocks") {
    val xPlane = Joint((1.0,0.0,0.0), (1.0,1.0,1.0), (1.0,1.0,1.0),
                       phi=0, cohesion=0, shape=Nil)
    val zPlane = Joint((0.0,0.0,1.0), (1.0, 1.0, 1.0), (1.0,1.0,1.0),
                       phi=0, cohesion=0, shape=Nil)
    val xBlocks = twoCube.cut(xPlane)
    val xzBlocks = xBlocks.flatMap(_.cut(zPlane))

    val nonRedundantBlocks = xzBlocks.map { case block @ Block(center, faces) =>
      val nonRedundantFaces = block.nonRedundantFaces
      Block(center, nonRedundantFaces)
    }

    val centroidBlocks = nonRedundantBlocks.map { case block @ Block(center, _) =>
      val centroid = block.centroid
      val updatedFaces = block.updateFaces(centroid)
      Block(centroid, updatedFaces)
    }

    val cleanedBlocks = centroidBlocks.map { case Block(center, faces) =>
      Block(center, faces.map(_.applyTolerance))
    }

    // Names describe block positions when locking from fourth octant to first octant
    val bottomLeft = Block((0.5, 1.0, 0.5), List(
      Face((0.0, 0.0, 1.0), 0.5, 0.0, 0.0),
      Face((1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((-1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((0.0, -1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 0.0, -1.0), 0.5, 0.0, 0.0)
    ))

    val topLeft = Block((0.5, 1.0, 1.5), List(
      Face((0.0, 0.0, -1.0), 0.5, 0.0, 0.0),
      Face((1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((-1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((0.0, -1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 0.0, 1.0), 0.5, 0.0, 0.0)
    ))

    val bottomRight = Block((1.5, 1.0, 0.5), List(
      Face((0.0, 0.0, 1.0), 0.5, 0.0, 0.0),
      Face((-1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((0.0, -1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 0.0, -1.0), 0.5, 0.0, 0.0)
    ))

    val topRight = Block((1.5, 1.0, 1.5), List(
      Face((0.0, 0.0, -1.0), 0.5, 0.0, 0.0),
      Face((-1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((1.0, 0.0, 0.0), 0.5, 0.0, 0.0),
      Face((0.0, -1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 1.0, 0.0), 1.0, 0.0, 0.0),
      Face((0.0, 0.0, 1.0), 0.5, 0.0, 0.0)
    ))

    val expectedBlocks = List(bottomLeft, topLeft, bottomRight, topRight)
    assert(cleanedBlocks == expectedBlocks)
  }

  test("Bounding sphere of the unit cube should have center (0.5, 0.5, 0.5) and radius sqrt(0.5 + 0.5^2)") {
    val expectedBoundingSphere = ((0.5, 0.5, 0.5), math.sqrt(0.5 + math.pow(0.5, 2)))
    val unitCubeBS = ((unitCube.sphereCenterX, unitCube.sphereCenterY, unitCube.sphereCenterZ),
                       unitCube.sphereRadius)
    assert(unitCubeBS == expectedBoundingSphere)
  }

  test("Bounding sphere of the two cube should have center (1.0, 1.0, 1.0) and radius sqrt(3.0)") {
    val expectedBoundingSphere = ((1.0, 1.0, 1.0), math.sqrt(3.0))
    val twoCubeBS = ((twoCube.sphereCenterX, twoCube.sphereCenterY, twoCube.sphereCenterZ),
                     twoCube.sphereRadius)
    assert(twoCubeBS == expectedBoundingSphere)
  }

  test("Bounding sphere and radius for tetrahedral block should be (0.45, 0.45, 0.45) and radius sqrt(0.6075)") {
    val expectedBoundingSphere = ((0.45, 0.45, 0.45), math.sqrt(0.6075))
    val tetBlockBS = ((tetrahedralBlock.sphereCenterX, tetrahedralBlock.sphereCenterY,
                       tetrahedralBlock.sphereCenterZ), tetrahedralBlock.sphereRadius)
    assert(centroidDifference(expectedBoundingSphere._1, tetBlockBS._1) < NumericUtils.EPSILON)
    assert(expectedBoundingSphere._2 - tetBlockBS._2 < NumericUtils.EPSILON)
  }

  test("-z=-0.5 should intersect the two cube - negative joint distance check") {
    val joint = Joint((0.0, 0.0, -1.0), localOrigin=(0.0, 0.0, 0.0), center=(0.0, 0.0, 1/2.0),
      phi=0, cohesion=0, shape=Nil)
    assert(twoCube.intersects(joint).isDefined)
  }

  test("The unit cube should have an inscribable sphere with max radius 0.5") {
    assert(Math.abs(Math.abs(unitCube.maxInscribableRadius) - 0.5) <= NumericUtils.EPSILON)
  }

  test("The two cube should have an inscribable sphere with max radius 1.0") {
    assert(Math.abs(twoCube.maxInscribableRadius - 1.0) <= NumericUtils.EPSILON)
  }

  test("The two cube with non-zero origin should have an inscribable sphere with max radius 1.0") {
    assert(Math.abs(twoCubeNonOrigin.maxInscribableRadius - 1.0) <= NumericUtils.EPSILON)
  }

  test("Rectangular prism should have an inscribable sphere with max radius 0.25") {
    assert(Math.abs(rectPrism.maxInscribableRadius - 0.25) <= NumericUtils.EPSILON)
  }

  test("Cutting the unit cube at x=0.5 with a minimum inscribable radius of 0.25 should produce two children") {
    val cutJoint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0, 0.0, 0.0), center=(0.5, 0.0, 0.0),
      phi = 0, cohesion = 0, shape = Nil)
    assert(unitCube.cut(cutJoint, minSize=0.25).length == 2)
  }

  test("Cutting the unit cube at x=0.5 with a minimum inscribable radius of 0.26 should produce no new children") {
    val cutJoint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0, 0.0, 0.0), center=(0.5, 0.0, 0.0),
      phi = 0, cohesion = 0, shape = Nil)
    val children = unitCube.cut(cutJoint, minSize=0.26)
    assert(children.length == 1)
    assert(children.head == unitCube)
  }

  test("Cutting non-origin two-cube at z=1 with minimum inscribable radius of 0.5 should produce two new children") {
    val cutJoint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(0.0, 0.0, 1.0),
                         phi=0.0, cohesion=0.0, shape=Nil)
    assert(twoCubeNonOrigin.cut(cutJoint, minSize=0.5).length == 2)
  }

  test("Cutting non-origin two-cube at z=1 with minimum inscribable radius of 0.6 should produce no new children") {
    val cutJoint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(0.0, 0.0, 1.0),
                         phi=0.0, cohesion=0.0, shape=Nil)
    val children = twoCubeNonOrigin.cut(cutJoint, minSize=0.6)
    assert(children.length == 1)
    assert(children.head == twoCubeNonOrigin)
  }

  test("Cutting the unit cube at x=0.5 with maximum aspect ratio of 3 should produce two new children") {
    val cutJoint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0, 0.0, 0.0), center=(0.5, 0.0, 0.0),
      phi = 0, cohesion = 0, shape = Nil)
    assert(unitCube.cut(cutJoint, maxAspectRatio=3.0).length == 2)
  }

  test("Cutting the unit cube at x=0.5 with maximum aspect ratio of 2.9 should produce no new children") {
    val cutJoint = Joint((1.0, 0.0, 0.0), localOrigin=(0.0, 0.0, 0.0), center=(0.5, 0.0, 0.0),
      phi = 0, cohesion = 0, shape = Nil)
    val children = unitCube.cut(cutJoint, maxAspectRatio=2.9)
    assert(children.length == 1)
    assert(children.head == unitCube)
  }

  test("Cutting non-origin two-cube at z=1 with maximum aspect ratio of 3 should produce two new children") {
    val cutJoint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(0.0, 0.0, 1.0),
      phi=0.0, cohesion=0.0, shape=Nil)
    assert(twoCubeNonOrigin.cut(cutJoint, maxAspectRatio=3.0).length == 2)
  }

  test("Cutting non-origin two-cube at z=1 with maximum aspect ratio of 2.9 should produce no new children") {
    val cutJoint = Joint((0.0, 0.0, 1.0), localOrigin=(0.0, 0.0, 0.0), center=(0.0, 0.0, 1.0),
      phi=0.0, cohesion=0.0, shape=Nil)
    val children = twoCubeNonOrigin.cut(cutJoint, maxAspectRatio=2.9)
    assert(children.length == 1)
    assert(children.head == twoCubeNonOrigin)
  }

  test("Volume of unit cube should be 1.0") {
    val volume = unitCube.volume
    assert(volume == 1.0)
  }

  test("Volume of two cube should be 8.0") {
    val volume = twoCube.volume
    assert(volume == 8.0)
  }

  test("Volume of two cube with extra plane should should be 4.0") {
    val redundantBoundingFaces = boundingFaces3 ++ List(
      Face((-1.0/math.sqrt(3.0), -1.0/math.sqrt(3.0), -1.0/math.sqrt(3.0)), 0.0, phi=0, cohesion=0)
    )
    val redundantTwoCube = Block((0.0, 0.0, 0.0), redundantBoundingFaces)
    val nonRedundantTwoCube = Block((0.0, 0.0, 0.0), redundantTwoCube.nonRedundantFaces)
    val volume = nonRedundantTwoCube.volume
    assert(volume == 4.0)
  }

  test("Volume of unit cube with extra plane should should be 0.5") {
    val redundantBoundingFaces = boundingFaces4 ++ List(
      Face((1.0/math.sqrt(3.0), 1.0/math.sqrt(3.0), 1.0/math.sqrt(3.0)), 0.0, phi=0, cohesion=0)
    )
    val redundantUnitCube = Block((0.5, 0.5, 0.5), redundantBoundingFaces)
    val nonRedundantUnitCube = Block((0.5, 0.5, 0.5), redundantUnitCube.nonRedundantFaces)
    val volume = nonRedundantUnitCube.volume
    assert(volume == 0.5)
  }

  test("Volume of unit cube with extra plane with negative z-component should be 0.5") {
    val redundantBoundingFaces = boundingFaces4 ++ List(
      Face((-1.0/math.sqrt(3.0), -1.0/math.sqrt(3.0), -1.0/math.sqrt(3.0)), 0.0, phi=0, cohesion=0)
    )
    val redundantUnitCube = Block((0.5, 0.5, 0.5), redundantBoundingFaces)
    val nonRedundantUnitCube = Block((0.5, 0.5, 0.5), redundantUnitCube.nonRedundantFaces)
    val volume = nonRedundantUnitCube.volume
    assert(volume == 0.5)
  }
}
