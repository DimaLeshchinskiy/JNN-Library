public class Layer {
    private  Neuron[] neurons;

    public Layer(int count)
    {
        neurons = new Neuron[count];
    }

    public void AddToLayer(Neuron neuron, int index)
    {
        neurons[index] = neuron;
    }

    public int GetSize()
    {
        return neurons.length;
    }
    public Neuron GetNeuron(int index)
    {
        return neurons[index];
    }


}
