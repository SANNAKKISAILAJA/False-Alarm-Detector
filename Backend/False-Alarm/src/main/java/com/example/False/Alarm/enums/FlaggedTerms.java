package com.example.False.Alarm.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FlaggedTerms {
    RAPE, MURDER, ROBBERY, KIDNAP, ASSAULT,
    TERRORIST, VIOLENCE, WEAPON, GUN, KNIFE,
    BOMB, THREAT, ABDUCTION, DRUG, EXPLOSIVE,
    BLACKMAIL, MOLEST, HARASS, SHOOT, STAB,
    TRAFFICKING, SMUGGLE, EXTORT, CYBERCRIME, POISON,
    SUICIDE, TORTURE, GRENADE, BULLET, ACID,
    MANSLAUGHTER, GENOCIDE, HATE_CRIME, RIOT, ARSON,
    BEHEAD, EXECUTE, SLAUGHTER, TERROR, HOSTAGE;

    public static List<String> getAllTerms() {
        return Arrays.stream(values())
            .map(term -> term.name().replace("_", " ").toLowerCase())
            .collect(Collectors.toList());
    }
}
