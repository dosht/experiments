from pybrain.structure import FeedForwardNetwork
from pybrain.structure import FullConnection
from pybrain.datasets import SupervisedDataSet
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.structure import *
import numpy as np


def construct_ff_network(input_layer, h_layers, output_layer):
    n = FeedForwardNetwork()
    # arch
    n.addInputModule(input_layer)
    for h in h_layers:
        n.addModule(h)

    n.addOutputModule(output_layer)

    # connections
    n.addConnection(FullConnection(input_layer, h_layers[0]))
    for (h1, h2) in zip(h_layers[0:-1], h_layers[1:]):
        n.addConnection(FullConnection(h1, h2))

    n.addConnection(FullConnection(h_layers[-1], output_layer))

    # initilize
    n.sortModules()
    return n


def create_dataset(X, y, n_labels=None):
    n_features = X.shape[1]
    n_labels = n_labels or len(set(y))
    ds = SupervisedDataSet(n_features, n_labels)
    for (xi, l) in zip(X, y):
        label = np.zeros(n_labels)
        label[l] = 1
        ds.addSample(xi, label)

    return ds


def fit(n, ds, num_iterations=100):
    for _ in xrange(num_iterations):
        BackpropTrainer(n, ds).train()


def predict(n, x):
    return n.activate(x).argmax()


def score(n, X, y):
    m = len(y)
    correct_predictions_count = 0.0
    for (xi, yi) in zip(X, y):
        if predict(n, xi) == yi:
            correct_predictions_count += 1

    return correct_predictions_count / m


def main():
    print 'start'
    # load
    from sklearn.datasets import load_iris
    data = load_iris()
    X = data.data
    y = data.target

    # Preprocessing
    from sklearn.preprocessing import normalize
    X_normailzed = normalize(X, norm='l2', axis=1, copy=True)


    n = construct_ff_network(
        LinearLayer(4),
        [
            TanhLayer(6),
            TanhLayer(6),
            TanhLayer(6),
            TanhLayer(6),
        ],
        LinearLayer(3))
    ds = create_dataset(X_normailzed, y)
    fit(n, ds, num_iterations=10)

    print n.activate(X_normailzed[1, :])
    print "end"


if __name__ == '__main__':
    main()
