package com.snek.fancyplayershops.events.data;


public enum DisplayRemovalReason {
    /* The display was picked up by the owner            */ PICKED_UP,
    /* The display was deleted by the owner              */ DELETED,
}

//TODO don't include test displays in the stats. save a parameter that identifies test displays (and also serialize it)