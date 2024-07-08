import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import 'main.css'; // Assuming the same CSS file for consistency

const Register: React.FC = () => {
    const history = useHistory();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleRegister = async (event: React.FormEvent) => {
        event.preventDefault(); // Prevent form from refreshing the page on submit
        try {
            // Assume the registration API endpoint is '/api/register'
            const response = await axios.post('/api/register', { username, password });
            setMessage('Registration successful. You can now log in.');
            // Redirect to login or other appropriate page after registration
            history.push('/login');
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                setMessage('Registration failed: ' + error.response.data);
            } else {
                setMessage('Registration failed: ' + error.message);
            }
        }
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>Register</h1>
            </header>
            <main className="App-main">
                <form onSubmit={handleRegister} className="login-form">
                    <label htmlFor="username">Username:</label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                    <label htmlFor="password">Password:</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    <button type="submit" className="App-button">Register</button>
                </form>
                {message && <p>{message}</p>}
            </main>
        </div>
    );
};

export default Register;