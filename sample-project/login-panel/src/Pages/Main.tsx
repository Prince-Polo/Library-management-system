/*
import React from 'react';
import axios, { isAxiosError } from 'axios';
import { API } from 'Plugins/CommonUtils/API';
import { LoginMessage } from 'Plugins/DoctorAPI/LoginMessage';
import { RegisterMessage } from 'Plugins/DoctorAPI/RegisterMessage';
import { PatientLoginMessage } from 'Plugins/PatientAPI/PatientLoginMessage';
import { PatientRegisterMessage } from 'Plugins/PatientAPI/PatientRegisterMessage';
import { AddPatientMessage } from 'Plugins/DoctorAPI/AddPatientMessage';
import { useHistory } from 'react-router';
import './main.css'; // Import the CSS file

export function MainComponent() {
    const history = useHistory();

    const sendPostRequest = async (message: API) => {
        try {
            const response = await axios.post(message.getURL(), JSON.stringify(message), {
                headers: { 'Content-Type': 'application/json' },
            });
            console.log('Response status:', response.status);
            console.log('Response body:', response.data);
        } catch (error) {
            if (isAxiosError(error)) {
                if (error.response && error.response.data) {
                    console.error('Error sending request:', error.response.data);
                } else {
                    console.error('Error sending request:', error.message);
                }
            } else {
                console.error('Unexpected error:', error);
            }
        }
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>预约系统</h1>
            </header>
            <main className="App-main">
                <button className="App-button" onClick={() => sendPostRequest(new LoginMessage('aaaa', 'bbbb'))}>
                    Doctor Login aaaa
                </button>
                <button className="App-button" onClick={() => sendPostRequest(new RegisterMessage('aaaa', 'bbbb'))}>
                    Doctor Register aaaa
                </button>
                <button className="App-button" onClick={() => sendPostRequest(new LoginMessage('aaaab', 'bbbb'))}>
                    Doctor Login aaaab
                </button>
                <button className="App-button" onClick={() => sendPostRequest(new PatientLoginMessage('cccc', 'bbbb'))}>
                    Patient Login cccc
                </button>
                <button className="App-button" onClick={() => sendPostRequest(new PatientRegisterMessage('cccc', 'bbbb'))}>
                    Patient Register cccc
                </button>
                <button className="App-button" onClick={() => sendPostRequest(new AddPatientMessage('aaaa', 'cccc'))}>
                    Add Patient
                </button>
                <button className="App-button" onClick={() => history.push("/another")}>
                    Jump to another page
                </button>
            </main>
        </div>
    );
}

// Ensure the export at the end of the file
export { MainComponent as Main };
*/
import React from 'react';
import { useHistory } from 'react-router-dom';
import '../main.css'; // Import the CSS file

export function MainComponent() {
    const history = useHistory();

    // Function to navigate to different pages with explicit type definition
    const navigateTo = (path: string): void => {
        history.push(path);
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>预约系统</h1>
            </header>
            <main className="App-main">
                <button className="App-button" onClick={() => navigateTo('/login')}>
                    Doctor Login
                </button>
                <button className="App-button" onClick={() => navigateTo('/register')}>
                    Register
                </button>
                <button className="App-button" onClick={() => navigateTo('/patient-login')}>
                    Patient Login
                </button>
                <button className="App-button" onClick={() => navigateTo('/add-patient')}>
                    Add Patient
                </button>
                <button className="App-button" onClick={() => navigateTo('/another')}>
                    Jump to another page
                </button>
            </main>
        </div>
    );
}

// Ensure the export at the end of the file
export { MainComponent as Main };