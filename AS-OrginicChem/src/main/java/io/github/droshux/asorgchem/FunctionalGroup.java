package io.github.droshux.asorgchem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum FunctionalGroup {
    OH("Hydroxyl", "Alcohol"),
    OHS("Hydroxyl (Secondary)", "Secondary Alcohol"),
    CHO("Formyl", "Aldehyde"),
    CC("Alkenyl", "Alkene"),
    COOH("Carboxyl", "Carboxylic Acid"),
    HA("Halo", "Haloalkane"),
    O("Carbonyl", "Ketone");

    public final String Name;
    public final String Family;
    FunctionalGroup(String Name, String Family) {
        this.Name = Name;this.Family = Family;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return Family;
    }
}
