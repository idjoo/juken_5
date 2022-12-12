package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintWidget;

public class Optimizer {
    static void applyDirectResolutionHorizontalChain(ConstraintWidgetContainer container, LinearSystem system, int numMatchConstraints, ConstraintWidget widget) {
        float currentPosition;
        ConstraintWidget firstWidget = widget;
        int widgetSize = 0;
        ConstraintWidget previous = null;
        int count = 0;
        float totalWeights = 0.0f;
        while (widget != null) {
            if (!(widget.getVisibility() == 8)) {
                count++;
                if (widget.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widgetSize = widgetSize + widget.getWidth() + (widget.mLeft.mTarget != null ? widget.mLeft.getMargin() : 0) + (widget.mRight.mTarget != null ? widget.mRight.getMargin() : 0);
                } else {
                    totalWeights += widget.mHorizontalWeight;
                }
            }
            previous = widget;
            widget = widget.mRight.mTarget != null ? widget.mRight.mTarget.mOwner : null;
            if (widget != null && (widget.mLeft.mTarget == null || !(widget.mLeft.mTarget == null || widget.mLeft.mTarget.mOwner == previous))) {
                widget = null;
            }
        }
        int lastPosition = 0;
        if (previous != null) {
            lastPosition = previous.mRight.mTarget != null ? previous.mRight.mTarget.mOwner.getX() : 0;
            if (previous.mRight.mTarget != null && previous.mRight.mTarget.mOwner == container) {
                lastPosition = container.getRight();
            }
        }
        float spreadSpace = ((float) (lastPosition - 0)) - ((float) widgetSize);
        float split = spreadSpace / ((float) (count + 1));
        ConstraintWidget widget2 = firstWidget;
        float currentPosition2 = 0.0f;
        if (numMatchConstraints == 0) {
            currentPosition2 = split;
        } else {
            split = spreadSpace / ((float) numMatchConstraints);
        }
        while (widget2 != null) {
            int left = widget2.mLeft.mTarget != null ? widget2.mLeft.getMargin() : 0;
            int right = widget2.mRight.mTarget != null ? widget2.mRight.getMargin() : 0;
            if (widget2.getVisibility() != 8) {
                float currentPosition3 = currentPosition2 + ((float) left);
                system.addEquality(widget2.mLeft.mSolverVariable, (int) (0.5f + currentPosition3));
                if (widget2.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    currentPosition = currentPosition3 + ((float) widget2.getWidth());
                } else if (totalWeights == 0.0f) {
                    currentPosition = currentPosition3 + ((split - ((float) left)) - ((float) right));
                } else {
                    currentPosition = currentPosition3 + ((((widget2.mHorizontalWeight * spreadSpace) / totalWeights) - ((float) left)) - ((float) right));
                }
                system.addEquality(widget2.mRight.mSolverVariable, (int) (0.5f + currentPosition));
                if (numMatchConstraints == 0) {
                    currentPosition += split;
                }
                currentPosition2 = currentPosition + ((float) right);
            } else {
                float position = currentPosition2 - (split / 2.0f);
                system.addEquality(widget2.mLeft.mSolverVariable, (int) (0.5f + position));
                system.addEquality(widget2.mRight.mSolverVariable, (int) (0.5f + position));
            }
            ConstraintWidget previous2 = widget2;
            widget2 = widget2.mRight.mTarget != null ? widget2.mRight.mTarget.mOwner : null;
            if (!(widget2 == null || widget2.mLeft.mTarget == null || widget2.mLeft.mTarget.mOwner == previous2)) {
                widget2 = null;
            }
            if (widget2 == container) {
                widget2 = null;
            }
        }
    }

    static void applyDirectResolutionVerticalChain(ConstraintWidgetContainer container, LinearSystem system, int numMatchConstraints, ConstraintWidget widget) {
        float currentPosition;
        ConstraintWidget firstWidget = widget;
        int widgetSize = 0;
        ConstraintWidget previous = null;
        int count = 0;
        float totalWeights = 0.0f;
        while (widget != null) {
            if (!(widget.getVisibility() == 8)) {
                count++;
                if (widget.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widgetSize = widgetSize + widget.getHeight() + (widget.mTop.mTarget != null ? widget.mTop.getMargin() : 0) + (widget.mBottom.mTarget != null ? widget.mBottom.getMargin() : 0);
                } else {
                    totalWeights += widget.mVerticalWeight;
                }
            }
            previous = widget;
            widget = widget.mBottom.mTarget != null ? widget.mBottom.mTarget.mOwner : null;
            if (widget != null && (widget.mTop.mTarget == null || !(widget.mTop.mTarget == null || widget.mTop.mTarget.mOwner == previous))) {
                widget = null;
            }
        }
        int lastPosition = 0;
        if (previous != null) {
            lastPosition = previous.mBottom.mTarget != null ? previous.mBottom.mTarget.mOwner.getX() : 0;
            if (previous.mBottom.mTarget != null && previous.mBottom.mTarget.mOwner == container) {
                lastPosition = container.getBottom();
            }
        }
        float spreadSpace = ((float) (lastPosition - 0)) - ((float) widgetSize);
        float split = spreadSpace / ((float) (count + 1));
        ConstraintWidget widget2 = firstWidget;
        float currentPosition2 = 0.0f;
        if (numMatchConstraints == 0) {
            currentPosition2 = split;
        } else {
            split = spreadSpace / ((float) numMatchConstraints);
        }
        while (widget2 != null) {
            int top = widget2.mTop.mTarget != null ? widget2.mTop.getMargin() : 0;
            int bottom = widget2.mBottom.mTarget != null ? widget2.mBottom.getMargin() : 0;
            if (widget2.getVisibility() != 8) {
                float currentPosition3 = currentPosition2 + ((float) top);
                system.addEquality(widget2.mTop.mSolverVariable, (int) (0.5f + currentPosition3));
                if (widget2.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    currentPosition = currentPosition3 + ((float) widget2.getHeight());
                } else if (totalWeights == 0.0f) {
                    currentPosition = currentPosition3 + ((split - ((float) top)) - ((float) bottom));
                } else {
                    currentPosition = currentPosition3 + ((((widget2.mVerticalWeight * spreadSpace) / totalWeights) - ((float) top)) - ((float) bottom));
                }
                system.addEquality(widget2.mBottom.mSolverVariable, (int) (0.5f + currentPosition));
                if (numMatchConstraints == 0) {
                    currentPosition += split;
                }
                currentPosition2 = currentPosition + ((float) bottom);
            } else {
                float position = currentPosition2 - (split / 2.0f);
                system.addEquality(widget2.mTop.mSolverVariable, (int) (0.5f + position));
                system.addEquality(widget2.mBottom.mSolverVariable, (int) (0.5f + position));
            }
            ConstraintWidget previous2 = widget2;
            widget2 = widget2.mBottom.mTarget != null ? widget2.mBottom.mTarget.mOwner : null;
            if (!(widget2 == null || widget2.mTop.mTarget == null || widget2.mTop.mTarget.mOwner == previous2)) {
                widget2 = null;
            }
            if (widget2 == container) {
                widget2 = null;
            }
        }
    }

    static void checkMatchParent(ConstraintWidgetContainer container, LinearSystem system, ConstraintWidget widget) {
        if (container.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
            int left = widget.mLeft.mMargin;
            int right = container.getWidth() - widget.mRight.mMargin;
            system.addEquality(widget.mLeft.mSolverVariable, left);
            system.addEquality(widget.mRight.mSolverVariable, right);
            widget.setHorizontalDimension(left, right);
            widget.mHorizontalResolution = 2;
        }
        if (container.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
            int top = widget.mTop.mMargin;
            int bottom = container.getHeight() - widget.mBottom.mMargin;
            system.addEquality(widget.mTop.mSolverVariable, top);
            system.addEquality(widget.mBottom.mSolverVariable, bottom);
            if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top);
            }
            widget.setVerticalDimension(top, bottom);
            widget.mVerticalResolution = 2;
        }
    }

    static void checkHorizontalSimpleDependency(ConstraintWidgetContainer container, LinearSystem system, ConstraintWidget widget) {
        float position;
        int left;
        int right;
        if (widget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            widget.mHorizontalResolution = 1;
        } else if (container.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
            int left2 = widget.mLeft.mMargin;
            int right2 = container.getWidth() - widget.mRight.mMargin;
            system.addEquality(widget.mLeft.mSolverVariable, left2);
            system.addEquality(widget.mRight.mSolverVariable, right2);
            widget.setHorizontalDimension(left2, right2);
            widget.mHorizontalResolution = 2;
        } else if (widget.mLeft.mTarget == null || widget.mRight.mTarget == null) {
            if (widget.mLeft.mTarget != null && widget.mLeft.mTarget.mOwner == container) {
                int left3 = widget.mLeft.getMargin();
                int right3 = left3 + widget.getWidth();
                widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                system.addEquality(widget.mLeft.mSolverVariable, left3);
                system.addEquality(widget.mRight.mSolverVariable, right3);
                widget.mHorizontalResolution = 2;
                widget.setHorizontalDimension(left3, right3);
            } else if (widget.mRight.mTarget != null && widget.mRight.mTarget.mOwner == container) {
                widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                int right4 = container.getWidth() - widget.mRight.getMargin();
                int left4 = right4 - widget.getWidth();
                system.addEquality(widget.mLeft.mSolverVariable, left4);
                system.addEquality(widget.mRight.mSolverVariable, right4);
                widget.mHorizontalResolution = 2;
                widget.setHorizontalDimension(left4, right4);
            } else if (widget.mLeft.mTarget != null && widget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                SolverVariable target = widget.mLeft.mTarget.mSolverVariable;
                widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                int left5 = (int) (target.computedValue + ((float) widget.mLeft.getMargin()) + 0.5f);
                int right5 = left5 + widget.getWidth();
                system.addEquality(widget.mLeft.mSolverVariable, left5);
                system.addEquality(widget.mRight.mSolverVariable, right5);
                widget.mHorizontalResolution = 2;
                widget.setHorizontalDimension(left5, right5);
            } else if (widget.mRight.mTarget == null || widget.mRight.mTarget.mOwner.mHorizontalResolution != 2) {
                boolean hasLeft = widget.mLeft.mTarget != null;
                boolean hasRight = widget.mRight.mTarget != null;
                if (!hasLeft && !hasRight) {
                    if (widget instanceof Guideline) {
                        Guideline guideline = (Guideline) widget;
                        if (guideline.getOrientation() == 1) {
                            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                            if (guideline.getRelativeBegin() != -1) {
                                position = (float) guideline.getRelativeBegin();
                            } else if (guideline.getRelativeEnd() != -1) {
                                position = (float) (container.getWidth() - guideline.getRelativeEnd());
                            } else {
                                position = ((float) container.getWidth()) * guideline.getRelativePercent();
                            }
                            int value = (int) (0.5f + position);
                            system.addEquality(widget.mLeft.mSolverVariable, value);
                            system.addEquality(widget.mRight.mSolverVariable, value);
                            widget.mHorizontalResolution = 2;
                            widget.mVerticalResolution = 2;
                            widget.setHorizontalDimension(value, value);
                            widget.setVerticalDimension(0, container.getHeight());
                            return;
                        }
                        return;
                    }
                    widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                    widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                    int left6 = widget.getX();
                    int right6 = left6 + widget.getWidth();
                    system.addEquality(widget.mLeft.mSolverVariable, left6);
                    system.addEquality(widget.mRight.mSolverVariable, right6);
                    widget.mHorizontalResolution = 2;
                }
            } else {
                SolverVariable target2 = widget.mRight.mTarget.mSolverVariable;
                widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
                widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
                int right7 = (int) ((target2.computedValue - ((float) widget.mRight.getMargin())) + 0.5f);
                int left7 = right7 - widget.getWidth();
                system.addEquality(widget.mLeft.mSolverVariable, left7);
                system.addEquality(widget.mRight.mSolverVariable, right7);
                widget.mHorizontalResolution = 2;
                widget.setHorizontalDimension(left7, right7);
            }
        } else if (widget.mLeft.mTarget.mOwner == container && widget.mRight.mTarget.mOwner == container) {
            int leftMargin = widget.mLeft.getMargin();
            int rightMargin = widget.mRight.getMargin();
            if (container.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                left = leftMargin;
                right = container.getWidth() - rightMargin;
            } else {
                left = leftMargin + ((int) ((((float) (((container.getWidth() - leftMargin) - rightMargin) - widget.getWidth())) * widget.mHorizontalBiasPercent) + 0.5f));
                right = left + widget.getWidth();
            }
            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
            system.addEquality(widget.mLeft.mSolverVariable, left);
            system.addEquality(widget.mRight.mSolverVariable, right);
            widget.mHorizontalResolution = 2;
            widget.setHorizontalDimension(left, right);
        } else {
            widget.mHorizontalResolution = 1;
        }
    }

    static void checkVerticalSimpleDependency(ConstraintWidgetContainer container, LinearSystem system, ConstraintWidget widget) {
        float position;
        int top;
        int bottom;
        if (widget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            widget.mVerticalResolution = 1;
        } else if (container.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
            int top2 = widget.mTop.mMargin;
            int bottom2 = container.getHeight() - widget.mBottom.mMargin;
            system.addEquality(widget.mTop.mSolverVariable, top2);
            system.addEquality(widget.mBottom.mSolverVariable, bottom2);
            if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top2);
            }
            widget.setVerticalDimension(top2, bottom2);
            widget.mVerticalResolution = 2;
        } else if (widget.mTop.mTarget == null || widget.mBottom.mTarget == null) {
            if (widget.mTop.mTarget != null && widget.mTop.mTarget.mOwner == container) {
                int top3 = widget.mTop.getMargin();
                int bottom3 = top3 + widget.getHeight();
                widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                system.addEquality(widget.mTop.mSolverVariable, top3);
                system.addEquality(widget.mBottom.mSolverVariable, bottom3);
                if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                    widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                    system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top3);
                }
                widget.mVerticalResolution = 2;
                widget.setVerticalDimension(top3, bottom3);
            } else if (widget.mBottom.mTarget != null && widget.mBottom.mTarget.mOwner == container) {
                widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                int bottom4 = container.getHeight() - widget.mBottom.getMargin();
                int top4 = bottom4 - widget.getHeight();
                system.addEquality(widget.mTop.mSolverVariable, top4);
                system.addEquality(widget.mBottom.mSolverVariable, bottom4);
                if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                    widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                    system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top4);
                }
                widget.mVerticalResolution = 2;
                widget.setVerticalDimension(top4, bottom4);
            } else if (widget.mTop.mTarget != null && widget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                SolverVariable target = widget.mTop.mTarget.mSolverVariable;
                widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                int top5 = (int) (target.computedValue + ((float) widget.mTop.getMargin()) + 0.5f);
                int bottom5 = top5 + widget.getHeight();
                system.addEquality(widget.mTop.mSolverVariable, top5);
                system.addEquality(widget.mBottom.mSolverVariable, bottom5);
                if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                    widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                    system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top5);
                }
                widget.mVerticalResolution = 2;
                widget.setVerticalDimension(top5, bottom5);
            } else if (widget.mBottom.mTarget != null && widget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                SolverVariable target2 = widget.mBottom.mTarget.mSolverVariable;
                widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                int bottom6 = (int) ((target2.computedValue - ((float) widget.mBottom.getMargin())) + 0.5f);
                int top6 = bottom6 - widget.getHeight();
                system.addEquality(widget.mTop.mSolverVariable, top6);
                system.addEquality(widget.mBottom.mSolverVariable, bottom6);
                if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                    widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                    system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top6);
                }
                widget.mVerticalResolution = 2;
                widget.setVerticalDimension(top6, bottom6);
            } else if (widget.mBaseline.mTarget == null || widget.mBaseline.mTarget.mOwner.mVerticalResolution != 2) {
                boolean hasBaseline = widget.mBaseline.mTarget != null;
                boolean hasTop = widget.mTop.mTarget != null;
                boolean hasBottom = widget.mBottom.mTarget != null;
                if (!hasBaseline && !hasTop && !hasBottom) {
                    if (widget instanceof Guideline) {
                        Guideline guideline = (Guideline) widget;
                        if (guideline.getOrientation() == 0) {
                            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                            if (guideline.getRelativeBegin() != -1) {
                                position = (float) guideline.getRelativeBegin();
                            } else if (guideline.getRelativeEnd() != -1) {
                                position = (float) (container.getHeight() - guideline.getRelativeEnd());
                            } else {
                                position = ((float) container.getHeight()) * guideline.getRelativePercent();
                            }
                            int value = (int) (0.5f + position);
                            system.addEquality(widget.mTop.mSolverVariable, value);
                            system.addEquality(widget.mBottom.mSolverVariable, value);
                            widget.mVerticalResolution = 2;
                            widget.mHorizontalResolution = 2;
                            widget.setVerticalDimension(value, value);
                            widget.setHorizontalDimension(0, container.getWidth());
                            return;
                        }
                        return;
                    }
                    widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                    widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                    int top7 = widget.getY();
                    int bottom7 = top7 + widget.getHeight();
                    system.addEquality(widget.mTop.mSolverVariable, top7);
                    system.addEquality(widget.mBottom.mSolverVariable, bottom7);
                    if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                        widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                        system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top7);
                    }
                    widget.mVerticalResolution = 2;
                }
            } else {
                SolverVariable target3 = widget.mBaseline.mTarget.mSolverVariable;
                widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
                widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
                int top8 = (int) ((target3.computedValue - ((float) widget.mBaselineDistance)) + 0.5f);
                int bottom8 = top8 + widget.getHeight();
                system.addEquality(widget.mTop.mSolverVariable, top8);
                system.addEquality(widget.mBottom.mSolverVariable, bottom8);
                widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top8);
                widget.mVerticalResolution = 2;
                widget.setVerticalDimension(top8, bottom8);
            }
        } else if (widget.mTop.mTarget.mOwner == container && widget.mBottom.mTarget.mOwner == container) {
            int topMargin = widget.mTop.getMargin();
            int bottomMargin = widget.mBottom.getMargin();
            if (container.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                top = topMargin;
                bottom = top + widget.getHeight();
            } else {
                top = (int) (((float) topMargin) + (((float) (((container.getHeight() - topMargin) - bottomMargin) - widget.getHeight())) * widget.mVerticalBiasPercent) + 0.5f);
                bottom = top + widget.getHeight();
            }
            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
            system.addEquality(widget.mTop.mSolverVariable, top);
            system.addEquality(widget.mBottom.mSolverVariable, bottom);
            if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top);
            }
            widget.mVerticalResolution = 2;
            widget.setVerticalDimension(top, bottom);
        } else {
            widget.mVerticalResolution = 1;
        }
    }
}
