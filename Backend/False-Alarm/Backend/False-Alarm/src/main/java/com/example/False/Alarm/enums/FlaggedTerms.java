package com.example.False.Alarm.enums;

public enum FlaggedTerms {
    RAPE, MURDER, ROBBERY, KIDNAP, ASSAULT,
    TERRORIST, VIOLENCE, WEAPON, GUN, KNIFE,
    BOMB, THREAT, ABDUCTION, DRUG, EXPLOSIVE,
    BLACKMAIL, MOLEST, HARASS, SHOOT, STAB,
    TRAFFICKING, SMUGGLE, EXTORT, CYBERCRIME, POISON,
    SUICIDE, TORTURE, GRENADE, BULLET, ACID,
    MANSLAUGHTER, GENOCIDE, HATE_CRIME, RIOT, ARSON,
    BEHEAD, EXECUTE, SLAUGHTER, TERROR, HOSTAGE;

    public static String[] getAllTerms() {
        FlaggedTerms[] values = FlaggedTerms.values();
        String[] terms = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            terms[i] = values[i].name().replace("_", " ").toLowerCase();
        }
        return terms;
    }
}
