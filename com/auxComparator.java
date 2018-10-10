package com;

import java.util.Arrays;
import java.util.Comparator;


class auxComparator implements Comparator<Integer> {
        double[] scores;
        public auxComparator(double[] a) {
                scores = a;
        }
        @Override
        public int compare(Integer o1, Integer o2) {
                return Double.compare(scores[o1], scores[o2]);
        }
}