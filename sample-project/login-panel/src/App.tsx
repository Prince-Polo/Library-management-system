import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { MainComponent } from './Pages/Main';
import Login from './Pages/Login';
import Register from './Pages/Register';
import PatientLogin from 'Pages/PatientLogin';
import AddPatient from './Pages/AddPatient';
import AnotherPage from './Pages/AnotherPage';
import './main.css'; // Assuming this is where your global styles are

function App() {
    return (
        <Router>
            <Switch>
                <Route path="/" exact component={MainComponent} />
                <Route path="/login" component={Login} />
                <Route path="/register" component={Register} />
                <Route path="/patient-login" component={PatientLogin} />
                <Route path="/add-patient" component={AddPatient} />
                <Route path="/another" component={AnotherPage} />
            </Switch>
        </Router>
    );
}

export default App;