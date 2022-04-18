package io.github.droshux.asorgchem;

public class Program {
    public static void main(String[] args) {
        Molecule t = new Molecule("2,3-chloro-6-iodononan-1-ol");
        for (FunctionalGroup g : t.Groups) {
            System.out.println(g);
        }
    }
}
