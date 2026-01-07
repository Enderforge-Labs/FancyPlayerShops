package com.snek.fancyplayershops.events.data;

public enum DisplayCreationReason {
    /* A new display was manually placed by the player        */ NEW,
    /* A snapshot was restored by the player                  */ SNAPSHOT,
}

//TODO don't include test displays in the stats. save a parameter that identifies test displays (and also serialize it)