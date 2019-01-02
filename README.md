Leaf Clasification - android application for the classification of plants by leaves.
With this app you can:
- form your library of leaves
- share a library of leaves with friends
- use third party leaf libraries
- train the neural network on the collected leaf library
- recognize plants by leaves using a neural network

This application is suitable for learning, lectures of botany or just for fun in the nature.

The problem of rapid classification of plants has long been known and completely unresolved to this day due to the large number of different plants, the complexity and similarity of the leaves and the individual characteristics of the species.

The task of developing an automatic plant classification system has great potential due to its practical relevance and the possibility of its application in many agricultural and pharmaceutical enterprises. It is equally important to observe populations of rare plant species in botanical gardens, and so on.

The used data set, which was created by Pedro F. B. Silva and Andre R. S. Marsal, using samples of letters collected by Rubim Almeida da Silva at the University of Porto, Portugal, and a data set consisting of works performed by James Cope, Charles Mallah and James Orwell, Kingston University, London
https://archive.ics.uci.edu/ml/datasets/leaf
https://archive.ics.uci.edu/ml/datasets/One-hundred+plant+species+leaves+data+set

The first window allows the user to download a photo or image from the device memory using the "Load Image" button. With the help of these sliders, the user is able to select options for analyzing the image and creating a simplified form of the leaf. The latest buttons "Find Tokens" and "Recognize" launchs token search procedures using the specified parameters and class recognition using the trained neural network and the letter tokens received in the previous step.

The second window for storing, reviewing and customizing the collection of leaves used to learn the neural network. The button "Add Class" is used for adds a class to the system, and then it becomes possible to download leaf images of this class. Each image is subject to processing and searching on it leaf tokens. The search takes place in the same scenario as in the first window. Each class can be edited or deleted using the control buttons. Also is possible to save the collection of leaves and the trained neural network into the configuration file.

The last window is the neural network configuration window. The user has the opportunity to set training options, the number of steps for training and monitor the change in the value of the error in the graph. The "Train network" button allows you to begin the process of learning the neural network.

In docs folder is located documentation of work and two xml files with datasets. You can download ot into application woth button "Load Library".