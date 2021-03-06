# SparkRocks - Parallel rock slicing implementation on Apache Spark
> Cambridge Berkeley - Geomechanics

[![License](https://img.shields.io/badge/License-GPL%20v2-blue.svg)](https://raw.githubusercontent.com/cb-geo/spark-rocks/master/LICENSE.md)
[![Scaladoc](http://javadoc-badge.appspot.com/com.github.nscala-time/nscala-time_2.11.svg?label=scaladoc)](https://cb-geo.github.io/spark-rocks)
[![](https://img.shields.io/github/issues-raw/cb-geo/spark-rocks.svg)](https://github.com/cb-geo/spark-rocks/issues)
[![Build Status](https://travis-ci.org/cb-geo/spark-rocks.svg?branch=master)](https://travis-ci.org/cb-geo/spark-rocks)
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.166103.svg)](https://doi.org/10.1016/j.compgeo.2017.05.001)

Please post any general questions about using the code to the [DEM Forum](https://forum.cb-geo.com/c/dem)

# Overview

`SparkRocks` is a parallel fractured rock mass generator that runs on Apache
Spark. The block cutting algorithm is based on a subdivision approach and linear
programming optimization as described in *A new rock slicing method based on
linear programming* by Boon et al. (2015). It can be run both locally or on the
cloud and on any operating system. A complete description of the parallel block cutting
implementation within `SparkRocks` is given in [*Parallel and scalable block
system generation*](https://doi.org/10.1016/j.compgeo.2017.05.001) by Gardner et al. (2017).


# Usage

Before running `SparkRocks`, Apache Spark needs to be installed on your
system. Apache Spark can be downloaded from
[here](http://spark.apache.org/downloads.html). Once Apache Spark is installed,
`SparkRocks` is run by submitting `sparkrocks-assembly-1.0.jar` to Spark. Spark
manages the execution and deployment of SparkRocks so the user does not need to
do any additional work to scale analyses to larger scale problems. Documentation
on how to deploy Spark locally or on the cloud is provided at [Submitting
Applications](http://spark.apache.org/docs/latest/submitting-applications.html).
The examples that follow assume SparkRocks is being run on Amazon EMR.

## Command line arguments

`SparkRocks` is run from the command line using only a few, straightforward
arguments as follows:

```
spark-submit path/to/sparkRocks-assembly-1.0.jar [required inputs] [optional inputs]
```

* Required inputs:

`-i <path/to/input/file>`

This provides the path the input file that is described below

`-n <integer number of partitions>`

Number of partitions to divide the input rock volume into before initiating parallel computations.

One or both of:
`--vtkOut <path/to/output>` `--jsonOut <path/to/output>`

These flags specify which outputs are desired and the directory where to save them. `SparkRocks`
can output either in `JSON` or an intermediate format that is easily converted to `VTK` using
[`VisualRocks`](https://github.com/cb-geo/visual-rocks).

* Optional inputs:

`--minRadius <value>`

Minimum inscribed radius of blocks that should be generated

`--maxAspectRatio <value>`

Maximum aspect ratio of blocks that should be generated

`-f`

Flag to force analysis to continue if specified number of partitions is not found

`--help`

Prints usage text
 
## Input

The required inputs are kept as simple as possible. The global origin for the
block generation needs to be specified as well as a bounding box. The bounding
box is simply a rectangular prism that bounds the entire rock volume that is to
be subdivided into blocks. It is specified by two vertices delineating its
maximum extents. The input rock volume and the joint sets that will cut also
need to be provided. The rock volume is described by the faces that bound
it. These are specified by providing the strike and dip for each face as well as
a point located in the face.

The beginning of the joint input data is indicated by an empty line. The joint
sets are specified by their strike, dip, persistence and spacing as well as the
friction angle and cohesion associated with the joint set. Presently, the
`JointGenerator` is only able to generate *persistent* joints so the persistence
input should be given as 100.0. Given these parameters, a full set of joints is
generated within the bounds delineated by the bounding box. This set of joints
is then used to cut the blocks in the input rock volume.

The following is a simple example of what an input file should look like. (Note:
the comments in parentheses are not part of the actual input file and are only
included for clarity):

```
0.0 0.0 0.0 (Global Origin)
-2.0 -2.0 -2.0 (Minimum Extent) 2.0 2.0 2.0 (Maximum Extent)
0.0 90.0 0.0 -1.0 0.0 30.0 0.0 (Bounding face, strike of 0 and dip of 90 degrees)
0.0 90.0 0.0 1.0.0 0.0 30.0 0.0 (Bounding face, strike of 0 and dip of 90 degrees)
90.0 90.0 -1.0 0.0 0.0 30.0 0.0 (Bounding face, strike of 90 and dip of 90 degrees)
90.0 90.0 1.0 0.0 0.0 30.0 0.0 (Bounding face, strike of 90 and dip of 90 degrees)
0.0 0.0 0.0 0.0 -1.0 30.0 0.0 (Bounding face, strike of 0 and dip of 0 degrees)
0.0 0.0 0.0 0.0 1.0 30.0 0.0 (Bounding face, strike of 0 and dip of 0 degrees)
(This line should be left blank to show transition to joint input data)
34.0 23.0 1.0 100.0 30.0 0.0 (First joint set, strike of 34 and dip of 23 degrees, spacing of 1.0m)
192.0 47.0 0.7 100.0 30.0 0.0 (Second joint set, strike of 192 and dip of 47 degrees, spacing of 0.7m)
321.0 62.0 0.4 100.0 30.0 0.0 (Third joint set, strike of 321 and dip of 62 degrees, spacing of 0.4m)
```

For the faces that bound the volume, notice that the point located within the
face is specified after the dip, followed by the friction angle and
cohesion. Notice that for all faces and joints, the friction angle and cohesion
are specified as 30.0 degrees and 0.0 force/area. As mentioned previously, all
joints are specified as 100% persistent.

## Output

Currently, `SparkRocks` exports the generated rock mass in two formats.  The
first is JavaScript Object Notation (JSON), which is a simple and standard means
of encoding data and is commonly used to exchange data between soft- ware
applications over the web. JSON was chosen because it is widely used and is
supported in most programming languages. This makes it easy for other soft- ware
tools, like DEM simulators, to use the 3D model generated by `SparkRocks`.  The 3D
model can also be exported in the more specialized Visualization Toolkit (VTK)
format. This enables visualization in tools such as [`ParaView`](http://www.paraview.org/)
(which, like `SparkRocks`, is open source and free to use). It is important to
note that `SparkRocks` is not limited to these two formats, as the system is
modular in design. Augmenting the software to export blocks in new format
involves writing code that essentially amounts to defining a single function
that converts a collection of blocks into the necessary form.

When generating `VTK` format, it is necessary to process the output files with
[`VisualRocks`](https://github.com/cb-geo/visual-rocks). This is a simple
Python script that converts the generated outputs into `.vtp` format so that
it can be directly imported into `ParaView`.

# Future Work

The current version of `SparkRocks` generates a fractured rock mass for persistent
joints; however, non-persistent joints are a common occurrence in natural
rock. Future work should include a stochastic joint generator that can capture
the variation in strike, dip, spacing and persistence of joint sets. The inter-
section code currently implemented in SparkRocks is able to account for the
non-persistence of joints, but the code that generates the joint sets should be
expanded to produce stochastic realizations such that natural variability in the
rock mass can be considered.

# Acknowledgments

This research was supported in part by the National Science Foundation (NSF)
grant CMMI-1363354 and the Edward G. Cahill and John R. Cahill Endowed Chair
funds.
