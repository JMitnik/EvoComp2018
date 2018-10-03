package com;

import java.util.Random;

import org.ejml.simple.*;
// import org.ejml.ops.CommonOps;
import org.ejml.data.*;
import org.ejml.interfaces.decomposition.*;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;

// several notes before you use this library or the operators I write here:
// 1. Pay attention to the side effect of these methods. Basically, those with
// the return value to be SimpleMatrix, are methods without the side effect,
// i.e. does not change the value of the parameter matrix passed in, such as
// `transpose`, `concatColumns`. And those with void return value, is typically
// with the side effect on at least one of the parameter, such as
// `EVDFactorize`.
// 2. Another thing to note is, the slice of matrix, for example,
// the result of calling `rows` and `cols`, is not the reference of the original
// matrix's underlying value, instead it is a brand new matrix whose values are
// copied from its mom, so please dont call the `rows` or `cols` and add a
// vector on it without envalue the original m again, and wrongly assuming the
// original matrix has been changed, its result could be disasterous. For your
// ease I create a shortcut `addVectorToRow` to do so and also acting as an
// example.
// 3. Wheather the operation is inplace? some of the operation might not be,
// such as `plus`. Be careful when you use a matrix as data member and dont
// assume the original matrix has been added to the value as you expect. The
// `addVectorToRow`, though, is inplace.

class sampleUtilityClassForEJML {
    // simply transpose, will not change a's value
    public static SimpleMatrix transpose(SimpleMatrix a) {
        return a.transpose();
    }
    // multiply a*b, without alter any of their values
    public static SimpleMatrix multiply(SimpleMatrix a, SimpleMatrix b) {
        return a.mult(b);
    }

    // get the inside DDRM array
    public static DMatrixRMaj getDDRM(SimpleMatrix a) {
        return a.getDDRM();
    }

    // EVD only for symmetric matrix
    // B and D is the eigenvector matrix and the eigenvalue matrix, created out of the function
    // this funciton will not change a's value, meantime create side effect of filling B and D with results
    public static void EVDFactorize(SimpleMatrix a, SimpleMatrix B, SimpleMatrix D) {
        SimpleMatrix aCopy = a.copy();
        EigenDecomposition_F64<DMatrixRMaj> eig = DecompositionFactory_DDRM.eig(a.numRows(), true, true);
        eig.decompose(aCopy.getDDRM());
        makeDiag(eig, D);
        makeEigenVectors(eig, B);
        return;
    }

    // returns a vector of shape (dim, 1) drawn from the N(0, cov).
    public static SimpleMatrix randomNormal(SimpleMatrix cov, Random rand) {
        return SimpleMatrix.randomNormal(cov, rand);
    }
    // concat A and B column-wise, without change any of their value
    public static SimpleMatrix concatColumns(SimpleMatrix A, SimpleMatrix B) {
        return A.concatColumns(B);
    }
    // concat A and B row-wise, without change any of their value
    public static SimpleMatrix concatRows(SimpleMatrix A, SimpleMatrix B) {
        return A.concatRows(B);
    }
    // extract rows of A, equivalent to A[begin:end, :]
    public static SimpleMatrix rows(SimpleMatrix A, int begin, int end) {
        return A.rows(begin, end);
    }
    // extract columns of A, equivalent to A[:, begin:end]

    public static SimpleMatrix cols(SimpleMatrix A, int begin, int end) {
        return A.cols(begin, end);
    }

    public static void set(SimpleMatrix A, int row, int col, double val) {
        A.set(row, col, val);
    }
    // set a rectangle part of A to be the values of B, eq to A[i:i+B.numRows, j:j+B.numCols]=B
    public static void setValuesInBulk(int i, int j, SimpleMatrix A, SimpleMatrix B) {
        A.insertIntoThis(i, j, B);
        return;
    }
    // plus alpha*B to A, without change the value of parameter, eq to A+=alpha*B
    public static SimpleMatrix plus(double alpha, SimpleMatrix A, SimpleMatrix B) {
        return A.plus(alpha, B);
    }
    // inplace version of add a vector to some row of matrix, eq to A[row, :]+=b
    public static void addVectorToRow(SimpleMatrix A, int row, SimpleMatrix b) {
        A.insertIntoThis(row, 0, A.rows(row, row + 1).plus(b));
        return;
    }

    private static void makeDiag(EigenDecomposition_F64<DMatrixRMaj> eig, SimpleMatrix D) {
        int numEigen = eig.getNumberOfEigenvalues();
        for (int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
            D.set(i, i, eig.getEigenvalue(i).real);
        }
        return;
    }

    private static void makeEigenVectors(EigenDecomposition_F64<DMatrixRMaj> eig, SimpleMatrix B) {
        int numEigen = eig.getNumberOfEigenvalues();
        for (int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
            B.insertIntoThis(0, i, SimpleMatrix.wrap(eig.getEigenVector(i)));
        }
        return;
    }

    public static void main(String args[]) {
        Random rand = new Random(42);
        // first method to generate a new SimpleMatrix
        SimpleMatrix a = SimpleMatrix.random_DDRM(2, 10, -5, 5, rand);
        SimpleMatrix b = SimpleMatrix.random_DDRM(10, 2, -5, 5, rand);
        // System.out.println(b.numRows());
        a.print();
        transpose(a);
        a.print();
        // second method, to generate from a calculation result
        SimpleMatrix c = multiply(a, b);
        c.print();
        a.print();
        randomNormal(multiply(a, transpose(a)), rand).print();
        // third method, directly call the new operator with passing the numRows and numCols
        SimpleMatrix B = new SimpleMatrix(2, 2);
        SimpleMatrix D = new SimpleMatrix(2, 2);
        EVDFactorize(c, B, D);
        B.print();
        D.print();
        concatColumns(B, D).print();
        setValuesInBulk(0, 0, B, D);
        B.print();
        D.print();
        plus(2.0, B, D);
        B.print();
        SimpleMatrix t = rows(B, 0, 1);
        t.print();
        set(t, 0, 0, 42.0);
        t.print();
        B.print();
        addVectorToRow(B, 0, t);
        B.print();
        // System.out.println(dec.getEigenvalue(0));
        // dec.getEigenVector().print();
    }
}