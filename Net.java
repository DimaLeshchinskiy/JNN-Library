import java.io.*;
import java.util.ArrayList;

public class Net {

    private Layer[] layers;
    private ArrayList<Weight> weights = new ArrayList<Weight>();

    public Net(int...neurons)
    {
        CreateNet(neurons);
        ConnectNeurons();
    }

    public Net()
    {
        LoadNet();
    }

    public int getInputsCount()
    {
        return layers[0].GetSize();
    }

    public void SetInput(int index, double value)
    {
        Neuron neuron = layers[0].GetNeuron(index);
        neuron.setValue(value);
        neuron.setMode("input");
    }

    public void Run()
    {
        System.out.println("RUN");

        Null();

        for(Layer layer: layers)
            for (int i = 0; i < layer.GetSize(); i++)
                layer.GetNeuron(i).Push();
    }

    public int GetIndexOutput()
    {

        int index_of_active_neuron = 0;
        double value_of_active_neuron = 0;

        for (int i = 0; i < layers[layers.length - 1].GetSize(); i++)
        {
            double new_value = layers[layers.length - 1].GetNeuron(i).getValue();

            System.out.println(i + " - " + new_value);

            if(value_of_active_neuron < new_value)
            {
                value_of_active_neuron = new_value;
                index_of_active_neuron = i;
            }
        }

        return index_of_active_neuron;
    }

    public double GetValueOutput()
    {
        return layers[layers.length - 1].GetNeuron(GetIndexOutput()).getValue();
    }


    public void Train(double step, int...value)
    {
        System.out.println("TRAIN\n");

        //error count
        countErrorLastLayer(value);

        //delta push up
        for (int i = layers.length - 2; i > 0; i-- )
            for (int j = 0; j < layers[i].GetSize(); j++)
                layers[i].GetNeuron(j).countDeltaHiddenLayer();

        //weights recount

        for(Layer layer: layers)
            for(int i = 0; i < layer.GetSize(); i++)
                layer.GetNeuron(i).Train(step);

    }

    public void SaveNet() throws Exception
    {

        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dima\\IdeaProjects\\Neural network\\src\\Net.txt"));

        String line = "";

        for (Layer layer: layers)
        {
            line += layer.GetSize() + " ";
        }


        writer.write(line + '\n');

        line = "";

        for (Weight weight: weights)
            line += weight.getValue() + " ";

        writer.write(line + '\n');

        line = "";

        for (Layer layer: layers)
            for (int i = 0; i < layer.GetSize(); i++)
                line += layer.GetNeuron(i).getBias() + " ";

        writer.write(line + '\n');

        writer.close();
    }

    private void countErrorLastLayer(int...value)
    {
        for (int i = 0; i < layers[layers.length - 1].GetSize(); i++)
        {
            Neuron neuron =  layers[layers.length - 1].GetNeuron(i);

            neuron.setMode("output");
            neuron.countDeltaLastLayer(value[i]);
        }
    }

    private void Null()
    {
        for (int i = 1; i < layers.length; i++)
        {
            for (int j = 0; j < layers[i].GetSize(); j++)
            {
                layers[i].GetNeuron(j).setValue(0);
                layers[i].GetNeuron(j).setDelta(0);
            }

        }
    }

    private void LoadNet()
    {

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "C:\\Users\\Dima\\IdeaProjects\\Neural network\\src\\Net.txt"));
            String[] line = reader.readLine().split(" ");
            int[] count = new int[line.length];

            for (int i = 0; i < line.length; i++)
                count[i] = Integer.parseInt(line[i]);

            CreateNet(count);

            line = reader.readLine().split(" ");
            double[] weight = new double[line.length];

            for (int i = 0; i < line.length; i++)
                weight[i] = Double.parseDouble(line[i]);

            ConnectNeuronsWithWeight(weight);

            line = reader.readLine().split(" ");
            double[] bias = new double[line.length];

            for (int i = 0; i < line.length; i++)
                bias[i] = Double.parseDouble(line[i]);

            AddBias(bias);


            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AddBias(double[] bias)
    {
        for (Layer layer: layers)
            for (int i = 0; i < layer.GetSize(); i++)
                layer.GetNeuron(i).setBias(bias[i]);
    }

    private void CreateNet(int...neurons)
    {
        layers = new Layer[neurons.length];

        for (int i = 0; i < neurons.length; i++) {

            Layer layer = CreateLayer(neurons[i]);

            FillLayer(layer, neurons[i], i);
        }
    }

    private void ConnectNeurons() {

        for (int i = 0; i < layers.length - 1; i++) {

            for (int j = 0; j < layers[i].GetSize(); j++) {

                ArrayList<Weight> loc_weights = new ArrayList<>();

                for (int k = 0; k < layers[i + 1].GetSize(); k++) {

                    Weight weight = new Weight(layers[i].GetNeuron(j), layers[i + 1].GetNeuron(k));
                    weights.add(weight);

                    loc_weights.add(weight);
                }
                layers[i].GetNeuron(j).weights = loc_weights;
            }
        }
    }

    private void ConnectNeuronsWithWeight(double[] weight_value ) {

        int index = 0;

        for (int i = 0; i < layers.length - 1; i++) {

            for (int j = 0; j < layers[i].GetSize(); j++) {

                ArrayList<Weight> loc_weights = new ArrayList<>();

                for (int k = 0; k < layers[i + 1].GetSize(); k++) {

                    Weight weight = new Weight(layers[i].GetNeuron(j), layers[i + 1].GetNeuron(k));
                    weight.setValue(weight_value[index]);
                    weights.add(weight);
                    loc_weights.add(weight);

                    index++;
                }

                layers[i].GetNeuron(j).weights = loc_weights;
            }
        }
    }

    private Layer CreateLayer(int size)
    {
        return new Layer(size);
    }


    private void FillLayer(Layer layer, int count, int index)
    {
        for (int j = 0; j < count; j++)
        {
            layer.AddToLayer(new Neuron(), j);
        }

        layers[index] = layer;
    }

}
