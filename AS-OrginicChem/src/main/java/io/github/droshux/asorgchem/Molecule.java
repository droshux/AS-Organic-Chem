package io.github.droshux.asorgchem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Molecule {
    public String IUPACname;
    public ArrayList<FunctionalGroup> Groups = new ArrayList<>();

    final private String[] ChainNames = {"methan", "ethan", "propan", "butan", "pentan", "hexan", "heptan", "octan", "nonan", "decan"};
    final private String[] Halogens = {"flouro", "chloro", "bromo", "iodo", "astato"};

    Molecule(@NotNull String IUPACname) {
        this.IUPACname = IUPACname.toLowerCase(Locale.ROOT);
        try {
            if (CheckForAlkene() != null) Groups.add(CheckForAlkene());
            Groups.addAll(CheckForAlcohol());
            if (CheckForCarboxylicAcidOrAldehyde() != null) Groups.add(CheckForCarboxylicAcidOrAldehyde());
            Groups.addAll(CheckForKetone());
            Groups.addAll(CheckForHaloAlkanes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Contract(pure = true)
    private @Nullable FunctionalGroup CheckForAlkene() throws Exception {
        FunctionalGroup out = null;
        ArrayList<String> parts = new ArrayList<>();
        for (String dashPart : IUPACname.split("-")) {
            Collections.addAll(parts, dashPart.split(","));
        }
        if (parts.contains("ene")) {
            String testArea = parts.get(parts.indexOf("ene")-2);
            try {
                Integer.parseInt(testArea);
                throw new Exception(IUPACname + " contains more than one alkene group!");
            } catch (NumberFormatException nfe) {
                out = FunctionalGroup.CC;
            }
        }
        return out;
    }

    private @NotNull ArrayList<FunctionalGroup> CheckForAlcohol() throws Exception {
        ArrayList<FunctionalGroup> out = new ArrayList<>();
        String[] ByDash = IUPACname.split("-");
        if (ByDash[ByDash.length - 1].equals("ol")) out.add(FunctionalGroup.OH);
        for (String dashPart : ByDash) {
            if (dashPart.contains("hydroxy")) {
                String ChainName = dashPart.split("y")[2];
                int ChainLength = GetChainLength();
                if (ChainLength == 0 && (ChainName.split("n")[ChainName.split("n").length - 1].equals("a") || ChainName.split("n")[ChainName.split("n").length - 1].equals("ta"))) throw new Exception("Only (main) carbon chain lengths that are up to (and including) 10 are accepted");
                String[] ByComma = ByDash[find(ByDash, dashPart)-1].split(",");
                for (String number : ByComma) {
                    if (Integer.parseInt(number) == 1 || Integer.parseInt(number) == ChainLength) out.add(FunctionalGroup.OH);
                    else out.add(FunctionalGroup.OHS);
                }
            }
        }
        return out;
    }

    private @Nullable FunctionalGroup CheckForCarboxylicAcidOrAldehyde() {
        String[] ByN = IUPACname.split("n");
        return switch (ByN[ByN.length-1]) {
            case "oic acid" -> FunctionalGroup.COOH;
            case "al" -> FunctionalGroup.CHO;
            default -> null;
        };
    }

    private @NotNull ArrayList<FunctionalGroup> CheckForKetone() {
        ArrayList<FunctionalGroup> out = new ArrayList<>();
        String[] ByDash = IUPACname.split("-");
        for (String DashPart : ByDash) if (DashPart.contains("oxo")) for (int i = 0; i < ByDash[find(ByDash, DashPart) - 1].split(",").length; i++) out.add(FunctionalGroup.O);
        return out;
    }

    private @NotNull ArrayList<FunctionalGroup> CheckForHaloAlkanes() {
        ArrayList<FunctionalGroup> out = new ArrayList<>();
        String[] ByDash = IUPACname.split("-");
        for (String Halogen : Halogens) for (String Part : ByDash) if (Part.contains(Halogen)) for (int i = 0; i < ByDash[find(ByDash, Part) - 1].split(",").length; i++) out.add(FunctionalGroup.HA);
        return out;
    }

    @Contract(pure = true)
    public static<T> int find(T @NotNull [] array, T target) {
        for (int i = 0; i < array.length; i++) if (target.equals(array[i])) return i;
        return -1;
    }

    public int GetChainLength() {
        int out = 0;
        String[] ByDash = IUPACname.split("-");
        for (String part : ByDash) for (String name : ChainNames) if (part.contains(name)) return find(ChainNames, name);
        return out;
    }
}