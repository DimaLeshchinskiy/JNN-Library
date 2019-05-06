public class Weight {

    private Neuron from;
    private Neuron to;

    private double value;

    private double last_value = 0;

    public Weight(Neuron from, Neuron to)
    {
        this.from = from;
        this.to = to;

        value = Math.random() * 0.2 - 0.1;
    }

    public Weight(Neuron from, Neuron to, double weight)
    {
        this.from = from;
        this.to = to;

        value = weight;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getDeltaNextNeuron(){return to.getDelta();}

    public void Push()
    {
        double new_value = to.getValue() + from.getValue() * this.value;

        to.setValue(new_value);

        //System.out.println(from + " - " + getValue() + " - " + to);
        //System.out.println(from.getValue() + " - " + getValue() + " - " + to.getValue() + "\n");

    }
}
