import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor

# Read in data and display first 5 rows
#features = pd.read_csv('../inf3995-02/BIXI_with_weather/combined_csv.csv')
#features = pd.read_csv('../inf3995-02/BIXI_with_weather/Test.csv')

def random_forest(station):
    
    return "La station a predire est: " + station
    
    """    
    print(features.head(5))
    print('The shape of our features is:', features.shape)
    features.describe()
    features = features.filter(items=['start_date', 'start_hour', 'start_month', 'start_day', 'start_station_code',
                                      'end_hour', 'end_station_code', 'end_month', 'end_day', 'duration_sec',
                                      'Description', 'Temperature', 'Pressure','Wind_speed'])
    # Separation of Description
    features = pd.get_dummies(features)
    print (features.iloc[:,5:].head(5))
    # Labels are the values we want to predict
    labels = np.array(features['duration_sec'])
    # Remove the labels from the features
    # axis 1 refers to the columns
    features= features.drop('duration_sec', axis = 1)
    # Saving feature names for later use
    feature_list = list(features.columns)
    # Convert to numpy array
    features = np.array(features)
    
    """
    
    """
    
    # Split the data into training and testing sets
    train_features, test_features, train_labels, test_labels = train_test_split(features, labels, test_size = 0.25, random_state = 42)
    print('Training Features Shape:', train_features.shape)
    print('Training Labels Shape:', train_labels.shape)
    print('Testing Features Shape:', test_features.shape)
    print('Testing Labels Shape:', test_labels.shape)
    # The baseline predictions are the historical averages
    baseline_preds = test_features[:, feature_list.index('duration_sec')]
    baseline_errors = abs(baseline_preds - test_labels)
    print('Average baseline error: ', round(np.mean(baseline_errors), 2))
    """
    """
    # Instantiate model with 1000 decision trees
    rf = RandomForestRegressor(n_estimators = 1000, random_state = 20)
    # Train the model on training data
    rf.fit(train_features, train_labels);
    
    # Use the forest's predict method on the test data
    predictions = rf.predict(test_features)
    # Calculate the absolute errors
    errors = abs(predictions - test_labels)
    print('Mean Absolute Error:', round(np.mean(errors), 2), 'seconde.')
    
    from sklearn.tree import export_graphviz
    import pydot
    # Pull out one tree from the forest
    tree = rf.estimators_[5]
    # Import tools needed for visualization
    from sklearn.tree import export_graphviz
    import pydot
    # Pull out one tree from the forest
    tree = rf.estimators_[5]
    # Export the image to a dot file
    export_graphviz(tree, out_file = 'tree.dot', feature_names = feature_list, rounded = True, precision = 1)
    
    # Limit depth of tree to 3 levels
    rf_small = RandomForestRegressor(n_estimators=10, max_depth = 3)
    rf_small.fit(train_features, train_labels)
    # Extract the small tree
    tree_small = rf_small.estimators_[5]
    # Save the tree as a png image
    export_graphviz(tree_small, out_file = 'small_tree.dot', feature_names = feature_list, rounded = True, precision = 1)
    (graph, ) = pydot.graph_from_dot_file('small_tree.dot')
    graph.write_png('small_tree1.png');
    """
    return True

def gestion_erreur():
    return "Pas encore implémenter"
    
from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def home():
    return "<h1>HELLO ENGIN 3 <h1>"

@app.route("/envoi", methods=['POST'])
def home_test():
    return "<h1>You are in Engin3<h1>",200

@app.route("/prediction/usage/<station>", methods=['POST', 'GET'])
def prediction_station(station):
    if request.method == 'GET':
        station = {station}.pop()
        data = random_forest(str(station))
        return data, 200
    else:
        return "Mauvaise requete", 400
    
@app.route("/prediction/erreur/", methods=['POST', 'GET'])
def erreur():
    if request.method == 'GET':
        data = gestion_erreur()
        return data, 200 
    else:
        return "Mauvaise requete", 400


if __name__ == "__main__":
    #app.run()
    app.run(port=5003)

