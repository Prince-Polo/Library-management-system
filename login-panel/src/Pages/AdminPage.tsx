import React, { useState } from 'react';
import { StudentInfoMessage } from 'Plugins/CommonUtils/API';

interface Data {
    name: string;
    email: string;
    number: string;
    role: string;
}

const AdminPage: React.FC = () => {
    const [data, setData] = useState<Data | null>(null);
    const [number, setNumber] = useState<string>('');
    const [error, setError] = useState<string | null>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNumber(e.target.value);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setData(null);

        try {
            const message = new StudentInfoMessage(number);
            const response = await fetch(message.getURL(), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(message),
            });

            const result = await response.json();

            if (response.ok) {
                setData(result);
            } else {
                setError(result.error || 'Failed to fetch student info');
            }
        } catch (error) {
            setError('Failed to fetch student info');
            console.error('Error fetching data:', error);
        }
    };

    const containerStyle: React.CSSProperties = {
        background: '#f9f9f9',
        border: '1px solid #ddd',
        padding: '20px',
        borderRadius: '5px',
        maxWidth: '400px',
        margin: '20px auto',
    };

    const headingStyle: React.CSSProperties = {
        marginBottom: '20px',
    };

    const paragraphStyle: React.CSSProperties = {
        margin: '10px 0',
    };

    return (
        <div style={containerStyle}>
            <h2 style={headingStyle}>Admin Page</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="number">Student Number:</label>
                    <input
                        type="text"
                        id="number"
                        name="number"
                        value={number}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">Fetch Student Info</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {data && (
                <div>
                    <h2 style={headingStyle}>User Information</h2>
                    <p style={paragraphStyle}><strong>Name:</strong> {data.name}</p>
                    <p style={paragraphStyle}><strong>Email:</strong> {data.email}</p>
                    <p style={paragraphStyle}><strong>Number:</strong> {data.number}</p>
                    <p style={paragraphStyle}><strong>Role:</strong> {data.role}</p>
                </div>
            )}
        </div>
    );
};

export default AdminPage;