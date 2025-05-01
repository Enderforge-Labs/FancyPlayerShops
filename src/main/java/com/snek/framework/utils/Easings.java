package com.snek.framework.utils;

import org.jetbrains.annotations.NotNull;








/**
 * A utility class containing a collection of common easing functions.
 */
public abstract class Easings {
    private Easings() {}

    public static final @NotNull Easing linear       = new Easing(Easings::_linear      );
    public static final @NotNull Easing sineIn       = new Easing(Easings::_sineIn      );
    public static final @NotNull Easing sineOut      = new Easing(Easings::_sineOut     );
    public static final @NotNull Easing sineInOut    = new Easing(Easings::_sineInOut   );

    public static final @NotNull Easing quadIn       = new Easing(Easings::_quadIn      );
    public static final @NotNull Easing quadOut      = new Easing(Easings::_quadOut     );
    public static final @NotNull Easing quadInOut    = new Easing(Easings::_quadInOut   );
    public static final @NotNull Easing cubicIn      = new Easing(Easings::_cubicIn     );
    public static final @NotNull Easing cubicOut     = new Easing(Easings::_cubicOut    );
    public static final @NotNull Easing cubicInOut   = new Easing(Easings::_cubicInOut  );
    public static final @NotNull Easing quartIn      = new Easing(Easings::_quartIn     );
    public static final @NotNull Easing quartOut     = new Easing(Easings::_quartOut    );
    public static final @NotNull Easing quartInOut   = new Easing(Easings::_quartInOut  );
    public static final @NotNull Easing quintIn      = new Easing(Easings::_quintIn     );
    public static final @NotNull Easing quintOut     = new Easing(Easings::_quintOut    );
    public static final @NotNull Easing quintInOut   = new Easing(Easings::_quintInOut  );

    public static final @NotNull Easing expIn        = new Easing(Easings::_expIn       );
    public static final @NotNull Easing expOut       = new Easing(Easings::_expOut      );
    public static final @NotNull Easing expInOut     = new Easing(Easings::_expInOut    );
    public static final @NotNull Easing circIn       = new Easing(Easings::_circIn      );
    public static final @NotNull Easing circOut      = new Easing(Easings::_circOut     );
    public static final @NotNull Easing circInOut    = new Easing(Easings::_circInOut   );
    public static final @NotNull Easing bounceIn     = new Easing(Easings::_bounceIn    );
    public static final @NotNull Easing bounceOut    = new Easing(Easings::_bounceOut   );
    public static final @NotNull Easing bounceInOut  = new Easing(Easings::_bounceInOut );
    public static final @NotNull Easing elasticIn    = new Easing(Easings::_elasticIn   );
    public static final @NotNull Easing elasticOut   = new Easing(Easings::_elasticOut  );
    public static final @NotNull Easing elasticInOut = new Easing(Easings::_elasticInOut);
    public static final @NotNull Easing backIn       = new Easing(Easings::_backIn      );
    public static final @NotNull Easing backOut      = new Easing(Easings::_backOut     );
    public static final @NotNull Easing backInOut    = new Easing(Easings::_backInOut   );




    // Linear
    private static double _linear    (final double x) { return x; }


    // Sinusoidal
    private static double _sineIn    (final double x) { return 1 - Math.cos((x * Math.PI)      / 2); }
    private static double _sineOut   (final double x) { return     Math.sin((x * Math.PI)      / 2); }
    private static double _sineInOut (final double x) { return   -(Math.cos( x * Math.PI) - 1) / 2 ; }


    // Quadratic
    private static double _quadIn    (final double x) { return     Math.pow(    x, 2); }
    private static double _quadOut   (final double x) { return 1 - Math.pow(1 - x, 2); }
    private static double _quadInOut (final double x) { return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2; }


    // Cubic
    private static double _cubicIn    (final double x) { return     Math.pow(    x, 3); }
    private static double _cubicOut   (final double x) { return 1 - Math.pow(1 - x, 3); }
    private static double _cubicInOut (final double x) { return x < 0.5 ? 4 * Math.pow(x, 3) : 1 - Math.pow(-2 * x + 2, 3) / 2; }


    // Quartic
    private static double _quartIn    (final double x) { return     Math.pow(    x, 4); }
    private static double _quartOut   (final double x) { return 1 - Math.pow(1 - x, 4); }
    private static double _quartInOut (final double x) { return x < 0.5 ? 8 * Math.pow(x, 4) : 1 - Math.pow(-2 * x + 2, 4) / 2; }


    // Quintic
    private static double _quintIn   (final double x) { return     Math.pow(    x, 5); }
    private static double _quintOut  (final double x) { return 1 - Math.pow(1 - x, 5); }
    private static double _quintInOut(final double x) { return x < 0.5 ? 16 * Math.pow(x, 5) : 1 - Math.pow(-2 * x + 2, 5) / 2; }




    // Circular
    private static double _circIn   (final double x) { return 1 - Math.sqrt(1 - Math.pow(x,     2)); }
    private static double _circOut  (final double x) { return     Math.sqrt(1 - Math.pow(x - 1, 2)); }
    private static double _circInOut(final double x) {
        if (x < 0.5) return (1 - Math.sqrt(1 - Math.pow(+2 * x,     2))    ) / 2;
        else         return (    Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2;
    }



    // Exponential
    private static double _expOut  (final double x) { return  Utils.doubleEquals(x, 1, 0.001) ? 1 : 1 - Math.pow(2, -10 *  x     ); }
    private static double _expIn   (final double x) { return  Utils.doubleEquals(x, 0, 0.001) ? 0 :     Math.pow(2, +10 * (x - 1)); }
    private static double _expInOut(final double x) {
        if(Utils.doubleEquals(x, 0, 0.001)) return 0;
        if(Utils.doubleEquals(x, 1, 0.001)) return 1;

        if(x < 0.5) return     0.5 * Math.pow(2, +20 * x - 10);
        else        return 1 - 0.5 * Math.pow(2, -20 * x + 10);
    }








    // Bounce
    private static double _bounceIn  (final double x) {
        return 1 - _bounceOut(1 - x);
    }
    private static double _bounceOut (double x) {
        final double n = 7.5625;
        final double d = 2.75;
        if     (x < 1   / d)                   return n * x * x;
        else if(x < 2   / d) { x -=   1.5 / d; return n * x * x + 0.75;     }
        else if(x < 2.5 / d) { x -=  2.25 / d; return n * x * x + 0.9375;   }
        else                 { x -= 2.625 / d; return n * x * x + 0.984375; }
    }
    private static double _bounceInOut (final double x) {
        return x < 0.5 ? (1 - _bounceOut(1 - 2 * x)) / 2 : (1 + _bounceOut(2 * x - 1)) / 2;
    }








    // Elastic
    private static double _elasticIn(final double x) {
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 :
                -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * ((2 * Math.PI) / 3))
            )
        ;
    }
    private static double _elasticOut(final double x) {
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 :
                Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1
            )
        ;
    }
    private static double _elasticInOut(final double x) {
        final double c = Math.sin(20 * x - 11.125) * (2 * Math.PI) / 4.5;
        return
            Utils.doubleEquals(x, 0, 0.001) ? 0 : (
            Utils.doubleEquals(x, 1, 0.001) ? 1 : (
                x < 0.5 ?
                -(Math.pow(2, +20 * x - 10) * c) / 2 :
                +(Math.pow(2, -20 * x + 10) * c) / 2 + 1
            ))
        ;
    }







    // Back
    private static double _backIn(final double x) {
        final double s = 1.70158;
        return x * x * ((s + 1) * x - s);
    }
    private static double _backOut(double x) {
        final double s = 1.70158;
        x -= 1;
        return x * x * ((s + 1) * x + s) + 1;
    }
    private static double _backInOut(double x) {
        final double s = 1.70158;
        if (x < 0.5)
            return (x * x * ((s * 1.525 + 1) * x - s * 1.525)) / 2;
        else {
            x -= 1;
            return (x * x * ((s * 1.525 + 1) * x + s * 1.525) + 2) / 2;
        }
    }
}
