import java.util.ArrayList;

public class Neuron {

    ArrayList<Weight> weights = new ArrayList<>();

    private String mode = "";

    private double value = 0;

    private double delta;

    private double bias = 1;

    private double last_value = 0;


    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode()
    {
        return this.mode;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void countDeltaLastLayer(double trainer){this.delta = 2 * (trainer - getValue());}

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double sigmoid(double x)
    {
        return 1/(1+Math.pow(Math.E, x * -1));
    }

    public void Activate()
    {
        setValue(sigmoid(getValue() + getBias()));
    }

    public void Push()
    {
        if (mode != "input")
            Activate();

        for (Weight weight: weights)
            weight.Push();
    }


    public void Train(double step)
    {
        this.bias += step * getDelta();

        for (Weight weight:weights){
            weight.setValue(weight.getValue() + step * weight.getDeltaNextNeuron() * getValue());}
    }

    public void countDeltaHiddenLayer()
    {
        double dif = getValue() * (1 - getValue());

        double delta_multiple_weight_next_layer = 0;

        for (Weight weight:weights)
            delta_multiple_weight_next_layer += weight.getValue() * weight.getDeltaNextNeuron() * dif;

        this.delta = delta_multiple_weight_next_layer;

    }
}