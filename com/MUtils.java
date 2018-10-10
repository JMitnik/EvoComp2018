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

class MUtils {
    // simply transpose, will not change a's value

    /**
     * Returns a new Simplematrix with the columns and rows of a transposes.
     * @param a
     * @return
     */
    public static SimpleMatrix transpose(SimpleMatrix a) {
        return a.transpose();
    }
    
    public static SimpleMatrix getSymmetricM(SimpleMatrix a) {
        return a.transpose().mult(a);
    }
    

    public static double normF(SimpleMatrix x) {
        return x.normF();
    }


    // multiply a*b, without alter any of their values

    /**
     * Returns a new Simplematrix with the columns of 'a' multiplied with 'b'.
     * - Does not alter the original matrices
     * 
     * @pre Matrix a and b have dimensions matchin the following: (n * m) * (m * p)
     * @post A matrix with dimensions (n * p) has been returned
     * @param a
     * @param b
     * @return SimpleMatrix
     */
    public static SimpleMatrix multiply(SimpleMatrix a, SimpleMatrix b) {
        return a.mult(b);
    }

    // get the inside DDRM array

    /**
     * Returns a Procedural DDDRM reprsentation of 'a' for more low-level operations.
     * @param a
     * @return DMatrixRaj
     */
    public static DMatrixRMaj getDDRM(SimpleMatrix a) {
        return a.getDDRM();
    }

    // EVD only for symmetric matrix
    // B and D is the eigenvector matrix and the eigenvalue matrix, created out of the function
    // this funciton will not change a's value, meantime create side effect of filling B and D with results

    /**
     * Decomposes the matrix A into a matrix with its eigen-values on the diagonal (D) and
     * its eigen-vectors as columns of a matrix B.
     * - Does not change A, changes B and D in-place.
     * @param a
     * @param B
     * @param D
     */
    public static void EVDFactorize(SimpleMatrix a, SimpleMatrix B, SimpleMatrix D) {
        SimpleMatrix aCopy = a.copy();
        EigenDecomposition_F64<DMatrixRMaj> eig = DecompositionFactory_DDRM.eig(a.numRows(), true, true);
        eig.decompose(aCopy.getDDRM());
        makeDiag(eig, D);
        makeEigenVectors(eig, B);
        return;
    }

    public static SimpleMatrix getHalfInverse(SimpleMatrix a) {
        SimpleMatrix aCopy = a.copy();
        EigenDecomposition_F64<DMatrixRMaj> eig = DecompositionFactory_DDRM.eig(a.numRows(), true, true);
        eig.decompose(aCopy.getDDRM());
        SimpleMatrix DInvSqrt = new SimpleMatrix(a.numRows(), a.numRows());
        SimpleMatrix B = new SimpleMatrix(a.numRows(), a.numRows());
        makeInverseSqrtDiag(eig, DInvSqrt);
        makeEigenVectors(eig, B);

        return B.mult(DInvSqrt).mult(B.transpose());
    }
    /**
     * Returns a new vector(n=1, m=dim) drawn from N(0, cov).
     * @param cov
     * @param rand
     * @return
     */
    public static SimpleMatrix randomNormal(SimpleMatrix cov, Random rand) {
        return SimpleMatrix.randomNormal(cov, rand).transpose();
    }
    
    public static SimpleMatrix randomDDRM(int numRows, int numCols, double minVal, double maxVal, Random rand) {
      return SimpleMatrix.random_DDRM(numRows, numCols, minVal, maxVal, rand);
    }
    /**
     * Returns a new matrix with the concatenated columns of A and B
     * Does not mutate the original matrices
     * @param A
     * @param B
     * @return SimpleMatrix
     */
    public static SimpleMatrix concatColumns(SimpleMatrix A, SimpleMatrix B) {
        return A.concatColumns(B);
    }

    /**
     * Returns a new matrix with the concatenated rows of A and B
     * Does not mutate the original matrices
     * @param A
     * @param B
     * @return SimpleMatrix
     */
    public static SimpleMatrix concatRows(SimpleMatrix A, SimpleMatrix B) {
        return A.concatRows(B);
    }

    /**
     * Returns the rows of matrix A, from 'begin' to 'end'.
     * Equivalent to A[begin:end, :]
     * @param A
     * @param begin
     * @param end
     * @return
     */
    public static SimpleMatrix rows(SimpleMatrix A, int begin, int end) {
        return A.rows(begin, end);
    }


    /**
     * Returns the columns of matrix A, from 'begin' to 'end'.
     * Equivalent to A[:, begin:end]
     * @param A
     * @param begin
     * @param end
     * @return
     */
    public static SimpleMatrix cols(SimpleMatrix A, int begin, int end) {
        return A.cols(begin, end);
    }

    /**
     * Sets the cell-value of matrix A at 'row' and 'col' to 'val'
     * @param A
     * @param row
     * @param col
     * @param val
     */
    public static void set(SimpleMatrix A, int row, int col, double val) {
        A.set(row, col, val);
    }

    /**
     * Sets a rectangle of A to be values of B. 
     * - Where i and j denote the start coordinates.
     * - And B denotes the sub-matrix to insert from those coordinates
     * Equivalent to A[i:i+B.numRows, j:j+B.numCols]=B
     * @param i
     * @param j
     * @param A
     * @param B
     */
    public static void setValuesInBulk(int i, int j, SimpleMatrix A, SimpleMatrix B) {
        A.insertIntoThis(i, j, B);
        return;
    }


    // plus alpha*B to A, without change the value of parameter, eq to A+=alpha*B

    /**
     * Creates a new Simplematrix with the values of A plus a scalar alpha * matrix B.
     * @pre A and B are the same dimensions
     * @param alpha
     * @param A
     * @param B
     * @return
     */
    public static SimpleMatrix plus(double alpha, SimpleMatrix A, SimpleMatrix B) {
        return A.plus(alpha, B);
    }

    public static SimpleMatrix scale(double alpha, SimpleMatrix A) {
        return A.scale(alpha);
    }

        /**
         * Adds a vector 'b' to a specific row of index 'row' of a matrix.
         * Equivalent to A[row, :]+=b
         * Does this inplace, and mutates the original matrix!
         * @param A
         * @param row
         * @param b
         */
        public static void addVectorToRow(SimpleMatrix A, int row,
                                          SimpleMatrix b) {
      A.insertIntoThis(row, 0, A.rows(row, row + 1).plus(b));
      return;
    }

    public static SimpleMatrix diag(int dim) {
        SimpleMatrix res = new SimpleMatrix(dim, dim);
        for(int i=0; i<dim;i++){
            res.set(i, i, 1);
        }
        return res;
    }
    
    /**
     * Returns a matrix with the eigen-values of eigen-decomposition 'eig' in the diagonals of D.
     * @param eig
     * @param D
     */
    private static void makeDiag(EigenDecomposition_F64<DMatrixRMaj> eig, SimpleMatrix D) {
        int numEigen = eig.getNumberOfEigenvalues();
        for (int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
            D.set(i, i, eig.getEigenvalue(i).real);
        }
        return;
    }
    /**
     * Returns a matrix with the inverse sqrt eigen-values of eigen-decomposition 'eig' in the diagonals of D.
     * @param eig
     * @param D
     */
    private static void makeInverseSqrtDiag(EigenDecomposition_F64<DMatrixRMaj> eig, SimpleMatrix D) {
        int numEigen = eig.getNumberOfEigenvalues();
        for (int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
            D.set(i, i, Math.sqrt(1/(eig.getEigenvalue(i).real)));
        }
        return;
    }

    /**
     * Returns a matrix with the eigen-vectors belonging to the eigen-values of 'eig' as the columns of B.
     * @param eig
     * @param B
     */
    private static void makeEigenVectors(EigenDecomposition_F64<DMatrixRMaj> eig, SimpleMatrix B) {
        int numEigen = eig.getNumberOfEigenvalues();

        for (int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
            B.insertIntoThis(0, i, SimpleMatrix.wrap(eig.getEigenVector(i)));
        }
        return;
    }

    private static SimpleMatrix meanIncremental(SimpleMatrix a, SimpleMatrix b, double i) {
                // System.out.println(i);
                // a.scale(i / (i + 1)).print();
                return a.scale(i / (i + 1)).plus(1 / (i + 1), b);
    }
    public static void main(String args[]) {
        Random rand = new Random(42);
        // first method to generate a new SimpleMatrix
        SimpleMatrix a = SimpleMatrix.random_DDRM(2, 10, -5, 5, rand);
        SimpleMatrix b = SimpleMatrix.random_DDRM(10, 2, -5, 5, rand);
        // System.out.println(b.numRows());
        SimpleMatrix mean = new SimpleMatrix(2, 10);
        mean = meanIncremental(mean, a, 0);
        mean = meanIncremental(mean, b.transpose(), 1);
        a.print();
        b.transpose().print();
        mean.print();
        transpose(a);
        // a.print();
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