import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import { LoginMessage } from 'Plugins/DoctorAPI/LoginMessage';
import './main.css';

const Login: React.FC = () => {
    const history = useHistory();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault(); // Prevent form from refreshing the page on submit
        const loginMessage = new LoginMessage(username, password);
        try {
            const response = await axios.post(loginMessage.getURL(), JSON.stringify(loginMessage), {
                headers: { 'Content-Type': 'application/json' },
            });
            setMessage('Login successful.');
            // Redirect to another page or update state based on success
            history.push('/dashboard'); // assuming a route to a dashboard or similar
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                setMessage('Login failed: ' + error.response.data);
            } else {
                setMessage('Login failed: ' + error.message);
            }
        }
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>Login</h1>
            </header>
            <main className="App-main">
                <form onSubmit={handleLogin} className="login-form">
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
                    <button type="submit" className="App-button">Login</button>
                </form>
                {message && <p>{message}</p>}
            </main>
        </div>
    );
};

export default Login;