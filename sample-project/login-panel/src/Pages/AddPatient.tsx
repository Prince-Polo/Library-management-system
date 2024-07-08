import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import { AddPatientMessage } from 'Plugins/DoctorAPI/AddPatientMessage';
import 'main.css';

const AddPatient: React.FC = () => {
    const history = useHistory();
    const [doctorName, setDoctorName] = useState('');
    const [patientName, setPatientName] = useState('');
    const [message, setMessage] = useState('');

    const handleAddPatient = async (event: React.FormEvent) => {
        event.preventDefault(); // Prevent form from refreshing the page on submit
        const addPatientMessage = new AddPatientMessage(doctorName, patientName);
        try {
            const response = await axios.post(addPatientMessage.getURL(), JSON.stringify(addPatientMessage), {
                headers: { 'Content-Type': 'application/json' },
            });
            setMessage('Patient added successfully.');
            // Redirect to another page or update state based on success
            history.push('/patients'); // assuming a route to a patients list or similar
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                setMessage('Adding patient failed: ' + error.response.data);
            } else {
                setMessage('Adding patient failed: ' + error.message);
            }
        }
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>Add Patient</h1>
            </header>
            <main className="App-main">
                <form onSubmit={handleAddPatient} className="login-form">
                    <label htmlFor="doctorName">Doctor Name:</label>
                    <input
                        id="doctorName"
                        type="text"
                        value={doctorName}
                        onChange={e => setDoctorName(e.target.value)}
                    />
                    <label htmlFor="patientName">Patient Name:</label>
                    <input
                        id="patientName"
                        type="text"
                        value={patientName}
                        onChange={e => setPatientName(e.target.value)}
                    />
                    <button type="submit" className="App-button">Add Patient</button>
                </form>
                {message && <p>{message}</p>}
            </main>
        </div>
    );
};

export default AddPatient;