package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Goal {
    ArrayList<SolverVariable> variables = new ArrayList<>();

    /* access modifiers changed from: package-private */
    public SolverVariable getPivotCandidate() {
        int count = this.variables.size();
        SolverVariable candidate = null;
        int strength = 0;
        for (int i = 0; i < count; i++) {
            SolverVariable element = this.variables.get(i);
            for (int k = 5; k >= 0; k--) {
                float value = element.strengthVector[k];
                if (candidate == null && value < 0.0f && k >= strength) {
                    strength = k;
                    candidate = element;
                }
                if (value > 0.0f && k > strength) {
                    strength = k;
                    candidate = null;
                }
            }
        }
        return candidate;
    }

    private void initFromSystemErrors(LinearSystem system) {
        this.variables.clear();
        for (int i = 1; i < system.mNumColumns; i++) {
            SolverVariable variable = system.mCache.mIndexedVariables[i];
            for (int j = 0; j < 6; j++) {
                variable.strengthVector[j] = 0.0f;
            }
            variable.strengthVector[variable.strength] = 1.0f;
            if (variable.mType == SolverVariable.Type.ERROR) {
                this.variables.add(variable);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateFromSystem(LinearSystem system) {
        initFromSystemErrors(system);
        int count = this.variables.size();
        for (int i = 0; i < count; i++) {
            SolverVariable element = this.variables.get(i);
            if (element.definitionId != -1) {
                ArrayLinkedVariables variables2 = system.getRow(element.definitionId).variables;
                int size = variables2.currentSize;
                for (int j = 0; j < size; j++) {
                    SolverVariable var = variables2.getVariable(j);
                    if (var != null) {
                        float value = variables2.getVariableValue(j);
                        for (int k = 0; k < 6; k++) {
                            float[] fArr = var.strengthVector;
                            fArr[k] = fArr[k] + (element.strengthVector[k] * value);
                        }
                        if (!this.variables.contains(var)) {
                            this.variables.add(var);
                        }
                    }
                }
                element.clearStrengths();
            }
        }
    }

    public String toString() {
        String representation = "Goal: ";
        for (int i = 0; i < this.variables.size(); i++) {
            representation = representation + this.variables.get(i).strengthsToString();
        }
        return representation;
    }
}
