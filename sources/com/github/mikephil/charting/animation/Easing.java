package com.github.mikephil.charting.animation;

public class Easing {

    public enum EasingOption {
        Linear,
        EaseInQuad,
        EaseOutQuad,
        EaseInOutQuad,
        EaseInCubic,
        EaseOutCubic,
        EaseInOutCubic,
        EaseInQuart,
        EaseOutQuart,
        EaseInOutQuart,
        EaseInSine,
        EaseOutSine,
        EaseInOutSine,
        EaseInExpo,
        EaseOutExpo,
        EaseInOutExpo,
        EaseInCirc,
        EaseOutCirc,
        EaseInOutCirc,
        EaseInElastic,
        EaseOutElastic,
        EaseInOutElastic,
        EaseInBack,
        EaseOutBack,
        EaseInOutBack,
        EaseInBounce,
        EaseOutBounce,
        EaseInOutBounce
    }

    public static EasingFunction getEasingFunctionFromOption(EasingOption easing) {
        switch (easing) {
            case EaseInQuad:
                return EasingFunctions.EaseInQuad;
            case EaseOutQuad:
                return EasingFunctions.EaseOutQuad;
            case EaseInOutQuad:
                return EasingFunctions.EaseInOutQuad;
            case EaseInCubic:
                return EasingFunctions.EaseInCubic;
            case EaseOutCubic:
                return EasingFunctions.EaseOutCubic;
            case EaseInOutCubic:
                return EasingFunctions.EaseInOutCubic;
            case EaseInQuart:
                return EasingFunctions.EaseInQuart;
            case EaseOutQuart:
                return EasingFunctions.EaseOutQuart;
            case EaseInOutQuart:
                return EasingFunctions.EaseInOutQuart;
            case EaseInSine:
                return EasingFunctions.EaseInSine;
            case EaseOutSine:
                return EasingFunctions.EaseOutSine;
            case EaseInOutSine:
                return EasingFunctions.EaseInOutSine;
            case EaseInExpo:
                return EasingFunctions.EaseInExpo;
            case EaseOutExpo:
                return EasingFunctions.EaseOutExpo;
            case EaseInOutExpo:
                return EasingFunctions.EaseInOutExpo;
            case EaseInCirc:
                return EasingFunctions.EaseInCirc;
            case EaseOutCirc:
                return EasingFunctions.EaseOutCirc;
            case EaseInOutCirc:
                return EasingFunctions.EaseInOutCirc;
            case EaseInElastic:
                return EasingFunctions.EaseInElastic;
            case EaseOutElastic:
                return EasingFunctions.EaseOutElastic;
            case EaseInOutElastic:
                return EasingFunctions.EaseInOutElastic;
            case EaseInBack:
                return EasingFunctions.EaseInBack;
            case EaseOutBack:
                return EasingFunctions.EaseOutBack;
            case EaseInOutBack:
                return EasingFunctions.EaseInOutBack;
            case EaseInBounce:
                return EasingFunctions.EaseInBounce;
            case EaseOutBounce:
                return EasingFunctions.EaseOutBounce;
            case EaseInOutBounce:
                return EasingFunctions.EaseInOutBounce;
            default:
                return EasingFunctions.Linear;
        }
    }

    private static class EasingFunctions {
        public static final EasingFunction EaseInBack = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input;
                return position * position * ((2.70158f * position) - 1.70158f);
            }
        };
        public static final EasingFunction EaseInBounce = new EasingFunction() {
            public float getInterpolation(float input) {
                return 1.0f - EasingFunctions.EaseOutBounce.getInterpolation(1.0f - input);
            }
        };
        public static final EasingFunction EaseInCirc = new EasingFunction() {
            public float getInterpolation(float input) {
                return -(((float) Math.sqrt((double) (1.0f - (input * input)))) - 1.0f);
            }
        };
        public static final EasingFunction EaseInCubic = new EasingFunction() {
            public float getInterpolation(float input) {
                return input * input * input;
            }
        };
        public static final EasingFunction EaseInElastic = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 0.0f) {
                    return 0.0f;
                }
                float position = input;
                if (position == 1.0f) {
                    return 1.0f;
                }
                float position2 = position - 1.0f;
                return -(((float) Math.pow(2.0d, (double) (10.0f * position2))) * ((float) Math.sin((((double) (position2 - ((0.3f / 6.2831855f) * ((float) Math.asin(1.0d))))) * 6.283185307179586d) / ((double) 0.3f))));
            }
        };
        public static final EasingFunction EaseInExpo = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 0.0f) {
                    return 0.0f;
                }
                return (float) Math.pow(2.0d, (double) (10.0f * (input - 1.0f)));
            }
        };
        public static final EasingFunction EaseInOutBack = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.0f) {
                    float s = 1.70158f * 1.525f;
                    return position * position * (((1.0f + s) * position) - s) * 0.5f;
                }
                float position2 = position - 2.0f;
                float s2 = 1.70158f * 1.525f;
                return ((position2 * position2 * (((1.0f + s2) * position2) + s2)) + 2.0f) * 0.5f;
            }
        };
        public static final EasingFunction EaseInOutBounce = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input < 0.5f) {
                    return EasingFunctions.EaseInBounce.getInterpolation(2.0f * input) * 0.5f;
                }
                return (EasingFunctions.EaseOutBounce.getInterpolation((2.0f * input) - 1.0f) * 0.5f) + 0.5f;
            }
        };
        public static final EasingFunction EaseInOutCirc = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.0f) {
                    return -0.5f * (((float) Math.sqrt((double) (1.0f - (position * position)))) - 1.0f);
                }
                float position2 = position - 2.0f;
                return (((float) Math.sqrt((double) (1.0f - (position2 * position2)))) + 1.0f) * 0.5f;
            }
        };
        public static final EasingFunction EaseInOutCubic = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.0f) {
                    return 0.5f * position * position * position;
                }
                float position2 = position - 2.0f;
                return ((position2 * position2 * position2) + 2.0f) * 0.5f;
            }
        };
        public static final EasingFunction EaseInOutElastic = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 0.0f) {
                    return 0.0f;
                }
                float position = input / 0.5f;
                if (position == 2.0f) {
                    return 1.0f;
                }
                float s = (0.45000002f / 6.2831855f) * ((float) Math.asin(1.0d));
                if (position < 1.0f) {
                    float position2 = position - 1.0f;
                    return -0.5f * ((float) Math.sin((((double) ((1.0f * position2) - s)) * 6.283185307179586d) / ((double) 0.45000002f))) * ((float) Math.pow(2.0d, (double) (10.0f * position2)));
                }
                float position3 = position - 1.0f;
                return (((float) Math.pow(2.0d, (double) (-10.0f * position3))) * ((float) Math.sin((((double) ((position3 * 1.0f) - s)) * 6.283185307179586d) / ((double) 0.45000002f))) * 0.5f) + 1.0f;
            }
        };
        public static final EasingFunction EaseInOutExpo = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 0.0f) {
                    return 0.0f;
                }
                if (input == 1.0f) {
                    return 1.0f;
                }
                float position = input / 0.5f;
                if (position < 1.0f) {
                    return ((float) Math.pow(2.0d, (double) (10.0f * (position - 1.0f)))) * 0.5f;
                }
                return ((-((float) Math.pow(2.0d, (double) (-10.0f * (position - 1.0f))))) + 2.0f) * 0.5f;
            }
        };
        public static final EasingFunction EaseInOutQuad = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.0f) {
                    return 0.5f * position * position;
                }
                float position2 = position - 1.0f;
                return -0.5f * (((position2 - 2.0f) * position2) - 1.0f);
            }
        };
        public static final EasingFunction EaseInOutQuart = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.0f) {
                    return 0.5f * position * position * position * position;
                }
                float position2 = position - 2.0f;
                return -0.5f * ((((position2 * position2) * position2) * position2) - 2.0f);
            }
        };
        public static final EasingFunction EaseInOutSine = new EasingFunction() {
            public float getInterpolation(float input) {
                return -0.5f * (((float) Math.cos(3.141592653589793d * ((double) input))) - 1.0f);
            }
        };
        public static final EasingFunction EaseInQuad = new EasingFunction() {
            public float getInterpolation(float input) {
                return input * input;
            }
        };
        public static final EasingFunction EaseInQuart = new EasingFunction() {
            public float getInterpolation(float input) {
                return input * input * input * input;
            }
        };
        public static final EasingFunction EaseInSine = new EasingFunction() {
            public float getInterpolation(float input) {
                return (-((float) Math.cos(((double) input) * 1.5707963267948966d))) + 1.0f;
            }
        };
        public static final EasingFunction EaseOutBack = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input - 1.0f;
                return (position * position * ((2.70158f * position) + 1.70158f)) + 1.0f;
            }
        };
        public static final EasingFunction EaseOutBounce = new EasingFunction() {
            public float getInterpolation(float input) {
                float position = input;
                if (position < 0.36363637f) {
                    return 7.5625f * position * position;
                }
                if (position < 0.72727275f) {
                    float position2 = position - 0.54545456f;
                    return (7.5625f * position2 * position2) + 0.75f;
                } else if (position < 0.90909094f) {
                    float position3 = position - 0.8181818f;
                    return (7.5625f * position3 * position3) + 0.9375f;
                } else {
                    float position4 = position - 0.95454544f;
                    return (7.5625f * position4 * position4) + 0.984375f;
                }
            }
        };
        public static final EasingFunction EaseOutCirc = new EasingFunction() {
            public float getInterpolation(float input) {
                float input2 = input - 1.0f;
                return (float) Math.sqrt((double) (1.0f - (input2 * input2)));
            }
        };
        public static final EasingFunction EaseOutCubic = new EasingFunction() {
            public float getInterpolation(float input) {
                float input2 = input - 1.0f;
                return (input2 * input2 * input2) + 1.0f;
            }
        };
        public static final EasingFunction EaseOutElastic = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 0.0f) {
                    return 0.0f;
                }
                float position = input;
                if (position == 1.0f) {
                    return 1.0f;
                }
                return (((float) Math.pow(2.0d, (double) (-10.0f * position))) * ((float) Math.sin((((double) (position - ((0.3f / 6.2831855f) * ((float) Math.asin(1.0d))))) * 6.283185307179586d) / ((double) 0.3f)))) + 1.0f;
            }
        };
        public static final EasingFunction EaseOutExpo = new EasingFunction() {
            public float getInterpolation(float input) {
                if (input == 1.0f) {
                    return 1.0f;
                }
                return -((float) Math.pow(2.0d, (double) ((1.0f + input) * -10.0f)));
            }
        };
        public static final EasingFunction EaseOutQuad = new EasingFunction() {
            public float getInterpolation(float input) {
                return (-input) * (input - 2.0f);
            }
        };
        public static final EasingFunction EaseOutQuart = new EasingFunction() {
            public float getInterpolation(float input) {
                float input2 = input - 1.0f;
                return -((((input2 * input2) * input2) * input2) - 1.0f);
            }
        };
        public static final EasingFunction EaseOutSine = new EasingFunction() {
            public float getInterpolation(float input) {
                return (float) Math.sin(((double) input) * 1.5707963267948966d);
            }
        };
        public static final EasingFunction Linear = new EasingFunction() {
            public float getInterpolation(float input) {
                return input;
            }
        };

        private EasingFunctions() {
        }
    }
}
